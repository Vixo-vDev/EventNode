import { useState, useRef, useEffect } from 'react'
import { eventService } from '../../services/eventService'
import { useTranslation } from '../../i18n/I18nContext'

function EditarEventoModal({ evento, categorias = [], onSubmit }) {
  const { t } = useTranslation()
  const fileInputRef = useRef(null)
  const formRef = useRef(null)
  const [bannerPreview, setBannerPreview] = useState(null)
  const [showSuccess, setShowSuccess] = useState(false)
  const [showError, setShowError] = useState(false)
  const [isLoading, setIsLoading] = useState(false)

  const [formData, setFormData] = useState({
    nombre: '',
    ubicacion: '',
    descripcion: '',
    fechaInicio: '',
    fechaFin: '',
    idCategoria: '',
    capacidadMaxima: '',
    tiempoCancelacionHoras: '',
    tiempoToleranciaMinutos: '',
    banner: null,
  })

  // Organizer state
  const [orgQuery, setOrgQuery] = useState('')
  const [orgSuggestions, setOrgSuggestions] = useState([])
  const [selectedOrgs, setSelectedOrgs] = useState([])
  const [showOrgDropdown, setShowOrgDropdown] = useState(false)
  const [orgError, setOrgError] = useState('')
  const orgInputRef = useRef(null)
  const orgContainerRef = useRef(null)

  // Populate form when evento changes
  useEffect(() => {
    if (evento) {
      const fmtDate = (dt) => {
        if (!dt) return ''
        const str = typeof dt === 'string' ? dt : dt.toString()
        return str.substring(0, 16)
      }

      setFormData({
        nombre: evento.nombre || '',
        ubicacion: evento.ubicacion || '',
        descripcion: evento.descripcion || '',
        fechaInicio: fmtDate(evento.fechaInicio),
        fechaFin: fmtDate(evento.fechaFin),
        idCategoria: evento.idCategoria ? String(evento.idCategoria) : '',
        capacidadMaxima: evento.capacidadMaxima ? String(evento.capacidadMaxima) : '',
        tiempoCancelacionHoras: evento.tiempoCancelacionHoras ? String(evento.tiempoCancelacionHoras) : '',
        tiempoToleranciaMinutos: evento.tiempoToleranciaMinutos != null ? String(evento.tiempoToleranciaMinutos) : '',
        banner: null,
      })
      setBannerPreview(
        evento.banner && evento.banner.startsWith('data:image/') ? evento.banner : null
      )
      setSelectedOrgs([])
      setOrgQuery('')
      setOrgError('')
      setShowSuccess(false)
      setShowError(false)

      // Show modal
      const modalEl = document.getElementById('editarEventoModal')
      if (modalEl && window.bootstrap) {
        // We rely on data-bs-toggle from the button to show the modal to prevent double backdrops.
        // bsModal.show() is removed.
      }
    }
  }, [evento])

  // Organizer search with debounce
  useEffect(() => {
    if (orgQuery.trim().length < 1) {
      setOrgSuggestions([])
      setShowOrgDropdown(false)
      return
    }
    const timer = setTimeout(async () => {
      try {
        const results = await eventService.buscarOrganizadores(orgQuery.trim())
        const filtered = results.filter(
          o => !selectedOrgs.some(s => s.idOrganizador === o.idOrganizador)
        )
        setOrgSuggestions(filtered)
        setShowOrgDropdown(filtered.length > 0)
      } catch {
        setOrgSuggestions([])
        setShowOrgDropdown(false)
      }
    }, 300)
    return () => clearTimeout(timer)
  }, [orgQuery, selectedOrgs])

  // Close dropdown on click outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (orgContainerRef.current && !orgContainerRef.current.contains(e.target)) {
        setShowOrgDropdown(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleSelectOrg = (org) => {
    setSelectedOrgs(prev => [...prev, org])
    setOrgQuery('')
    setShowOrgDropdown(false)
    orgInputRef.current?.focus()
  }

  const handleRemoveOrg = (idOrganizador) => {
    setSelectedOrgs(prev => prev.filter(o => o.idOrganizador !== idOrganizador))
  }

  const handleOrgKeyDown = async (e) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      const trimmed = orgQuery.trim()
      if (!trimmed) return
      try {
        const results = await eventService.buscarOrganizadores(trimmed)
        const available = results.filter(
          o => !selectedOrgs.some(s => s.idOrganizador === o.idOrganizador)
        )
        if (available.length > 0) {
          const exactMatch = available.find(
            o => o.nombre.toLowerCase() === trimmed.toLowerCase()
          )
          handleSelectOrg(exactMatch || available[0])
          setOrgError('')
        } else if (results.length > 0) {
          setOrgError(`"${trimmed}" ya fue agregado.`)
        } else {
          setOrgError(`"${trimmed}" no existe en la base de datos.`)
        }
      } catch {
        setOrgError('Error al buscar en la base de datos.')
      }
      return
    }
    if (e.key === 'Backspace' && orgQuery === '' && selectedOrgs.length > 0) {
      setSelectedOrgs(prev => prev.slice(0, -1))
    }
    if (e.key !== 'Enter' && e.key !== 'Backspace') {
      setOrgError('')
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleBannerClick = () => fileInputRef.current?.click()
  const handleBannerChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setBannerPreview(URL.createObjectURL(file))
      setFormData(prev => ({ ...prev, banner: file }))
    }
  }

  const handleSave = async () => {
    const { nombre, ubicacion, descripcion, fechaInicio, fechaFin, idCategoria, capacidadMaxima, tiempoCancelacionHoras, tiempoToleranciaMinutos } = formData
    if (!nombre.trim() || !ubicacion.trim() || !descripcion.trim() || !fechaInicio || !fechaFin || !idCategoria || !capacidadMaxima || !tiempoCancelacionHoras || !tiempoToleranciaMinutos) {
      setShowError(true)
      setShowSuccess(false)
      return
    }
    setIsLoading(true)
    try {
      await onSubmit({
        ...formData,
        organizadores: selectedOrgs.map(o => o.idOrganizador),
      })
      setShowSuccess(true)
      setShowError(false)
    } catch {
      setShowError(true)
      setShowSuccess(false)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <>
      <div className="modal fade" id="editarEventoModal" tabIndex="-1" aria-labelledby="editarEventoModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <form ref={formRef} noValidate className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <div>
              <h5 className="fw-bold mb-1">{t('editEvent.title')}</h5>
              <p className="text-secondary small mb-0">
                {t('editEvent.subtitle')}
              </p>
            </div>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label={t('common.close')}></button>
          </div>
          <div className="modal-body px-4 py-3">
            {/* Banner */}
            <div className="mb-4">
              <label className="form-label fw-semibold small">{t('editEvent.changeBanner')}</label>
              <input type="file" ref={fileInputRef} accept="image/*" className="d-none" onChange={handleBannerChange} />
              <div className="rounded-3 overflow-hidden position-relative" style={{ cursor: 'pointer' }} onClick={handleBannerClick}>
                {bannerPreview ? (
                  <img
                    src={bannerPreview}
                    alt={t('editEvent.changeBanner')}
                    className="w-100"
                    style={{ height: '180px', objectFit: 'cover' }}
                  />
                ) : (
                  <div
                    className="d-flex flex-column align-items-center justify-content-center text-center p-4"
                    style={{ border: '2px dashed #dee2e6', height: '180px' }}
                  >
                    <i className="bi bi-image text-secondary fs-3 mb-2"></i>
                    <div className="text-secondary small">{t('editEvent.selectBanner')}</div>
                  </div>
                )}
                {bannerPreview && (
                  <div className="position-absolute top-50 start-50 translate-middle bg-dark bg-opacity-50 text-white px-3 py-2 rounded-pill d-flex align-items-center gap-2" style={{ pointerEvents: 'none' }}>
                    <i className="bi bi-camera"></i> {t('editEvent.changeBanner')}
                  </div>
                )}
              </div>
            </div>

            {/* Nombre + Ubicación */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('editEvent.eventName')}</label>
                <input
                  type="text"
                  name="nombre"
                  className="form-control"
                  placeholder={t('editEvent.eventName')}
                  value={formData.nombre}
                  onChange={handleChange}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('editEvent.location')}</label>
                <input
                  type="text"
                  name="ubicacion"
                  className="form-control"
                  placeholder={t('editEvent.location')}
                  value={formData.ubicacion}
                  onChange={handleChange}
                />
              </div>
            </div>

            {/* Descripción */}
            <div className="mb-3">
              <label className="form-label fw-semibold small">{t('editEvent.description')}</label>
              <textarea
                name="descripcion"
                className="form-control"
                rows="3"
                placeholder={t('editEvent.description')}
                value={formData.descripcion}
                onChange={handleChange}
              ></textarea>
            </div>

            {/* Fechas */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Fecha y Hora Inicio *</label>
                <input
                  type="datetime-local"
                  name="fechaInicio"
                  className="form-control"
                  value={formData.fechaInicio}
                  onChange={handleChange}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Fecha y Hora Fin *</label>
                <input
                  type="datetime-local"
                  name="fechaFin"
                  className="form-control"
                  value={formData.fechaFin}
                  onChange={handleChange}
                />
              </div>
            </div>

            {/* Categoría + Capacidad */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('editEvent.category')}</label>
                <select
                  name="idCategoria"
                  className="form-select"
                  value={formData.idCategoria}
                  onChange={handleChange}
                >
                  <option value="">{t('editEvent.selectCategory')}</option>
                  {categorias.map(cat => (
                    <option key={cat.idCategoria} value={cat.idCategoria}>
                      {cat.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('editEvent.maxCapacity')}</label>
                <input
                  type="number"
                  name="capacidadMaxima"
                  className="form-control"
                  placeholder={t('editEvent.capacityPlaceholder')}
                  min="1"
                  value={formData.capacidadMaxima}
                  onChange={handleChange}
                />
              </div>
            </div>

            {/* Cancelación + Tolerancia */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Tiempo Cancelación (hrs) *</label>
                <input
                  type="number"
                  name="tiempoCancelacionHoras"
                  className="form-control"
                  placeholder="Ej: 24"
                  min="1"
                  value={formData.tiempoCancelacionHoras}
                  onChange={handleChange}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Tolerancia de entrada (min) *</label>
                <input
                  type="number"
                  name="tiempoToleranciaMinutos"
                  className="form-control"
                  placeholder="Ej: 15"
                  min="0"
                  value={formData.tiempoToleranciaMinutos}
                  onChange={handleChange}
                />
              </div>
            </div>

            {/* Organizador con búsqueda y etiquetas */}
            <div className="mb-2" ref={orgContainerRef} style={{ position: 'relative' }}>
              <label className="form-label fw-semibold small">{t('editEvent.organizer')}</label>
              <div
                className="d-flex flex-wrap align-items-center gap-2 form-control p-2"
                style={{ minHeight: '38px', cursor: 'text' }}
                onClick={() => orgInputRef.current?.focus()}
              >
                {selectedOrgs.map(org => (
                  <span
                    key={org.idOrganizador}
                    className="badge bg-primary bg-opacity-10 text-primary d-flex align-items-center gap-1 px-2 py-1 rounded-pill"
                    style={{ fontSize: '12px' }}
                  >
                    {org.nombre}
                    <button
                      type="button"
                      className="btn-close btn-close-sm"
                      style={{ fontSize: '7px', filter: 'none', opacity: 0.7 }}
                      onClick={(e) => { e.stopPropagation(); handleRemoveOrg(org.idOrganizador) }}
                    ></button>
                  </span>
                ))}
                <input
                  ref={orgInputRef}
                  type="text"
                  className="border-0 flex-grow-1 small"
                  placeholder={selectedOrgs.length === 0 ? t('editEvent.organizerPlaceholder') : t('editEvent.addAnother')}
                  style={{ outline: 'none', minWidth: '120px', fontSize: '13px' }}
                  value={orgQuery}
                  onChange={(e) => setOrgQuery(e.target.value)}
                  onKeyDown={handleOrgKeyDown}
                  onFocus={() => {
                    if (orgQuery.trim().length > 0 && orgSuggestions.length > 0) {
                      setShowOrgDropdown(true)
                    }
                  }}
                />
              </div>

              {showOrgDropdown && (
                <div
                  className="list-group shadow-sm border rounded-3 mt-1"
                  style={{ position: 'absolute', zIndex: 1050, width: '100%', maxHeight: '180px', overflowY: 'auto' }}
                >
                  {orgSuggestions.map(org => (
                    <button
                      key={org.idOrganizador}
                      type="button"
                      className="list-group-item list-group-item-action py-2 px-3 d-flex align-items-center gap-2"
                      style={{ fontSize: '13px' }}
                      onClick={() => handleSelectOrg(org)}
                    >
                      <i className="bi bi-person-fill text-secondary"></i>
                      <div>
                        <div className="fw-semibold">{org.nombre}</div>
                        {org.correo && <div className="text-secondary" style={{ fontSize: '11px' }}>{org.correo}</div>}
                      </div>
                    </button>
                  ))}
                </div>
              )}

              {orgError && (
                <div className="text-danger mt-1" style={{ fontSize: '11px' }}>
                  <i className="bi bi-exclamation-circle me-1"></i>{orgError}
                </div>
              )}
              <div className="text-secondary mt-1" style={{ fontSize: '11px' }}>
                {t('editEvent.organizerHelp')}
              </div>
            </div>
          </div>

          <div className="modal-footer border-top px-4 py-3">
            <button type="button" className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal">
              {t('common.cancel')}
            </button>
            <button
              type="button"
              className="btn btn-primary rounded-pill px-4 d-flex align-items-center gap-2"
              onClick={handleSave}
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                  {t('editEvent.updating')}
                </>
              ) : (
                <>
                  <i className="bi bi-check-circle"></i>
                  {t('editEvent.updateEvent')}
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
    {showSuccess && (
      <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="bg-white border-0 rounded-4 shadow text-center p-4" style={{ maxWidth: '400px', width: '90%' }}>
              <div className="mb-3">
                <i className="bi bi-check-circle-fill text-success" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>{t('editEvent.eventUpdated')}</h5>
              <p className="text-secondary small">{t('editEvent.changesSaved')}</p>
              <button className="btn btn-primary rounded-pill px-4 mt-2 mx-auto" onClick={() => {
                setShowSuccess(false)
                const modalEl = document.getElementById('editarEventoModal')
                if (modalEl && window.bootstrap) {
                  const bsModal = window.bootstrap.Modal.getInstance(modalEl)
                  if (bsModal) bsModal.hide()
                }
              }}>
                {t('common.accept')}
              </button>
          </div>
        </div>
      )}

      {showError && (
        <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="bg-white border-0 rounded-4 shadow text-center p-4" style={{ maxWidth: '350px', width: '90%' }}>
              <div className="mb-3">
                <i className="bi bi-x-circle-fill text-danger" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>{t('editEvent.reviewData')}</h5>
              <p className="text-secondary small">{t('editEvent.requiredFields')}</p>
              <button className="btn btn-danger rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowError(false)}>{t('common.understood')}</button>
          </div>
        </div>
      )}
    </>
  )
}

export default EditarEventoModal
