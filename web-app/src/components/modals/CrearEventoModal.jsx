import { useState, useRef, useEffect } from 'react'
import { eventService } from '../../services/eventService'
import { useTranslation } from '../../i18n/I18nContext'

function CrearEventoModal({ categorias = [], isLoading, onSubmit }) {
  const { t } = useTranslation()
  const fileInputRef = useRef(null)
  const formRef = useRef(null)
  const closeBtnRef = useRef(null)
  const [bannerPreview, setBannerPreview] = useState(null)
  const [showSuccess, setShowSuccess] = useState(false)
  const [showError, setShowError] = useState(false)
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

  // Estado para organizadores
  const [orgQuery, setOrgQuery] = useState('')
  const [orgSuggestions, setOrgSuggestions] = useState([])
  const [selectedOrgs, setSelectedOrgs] = useState([])
  const [showOrgDropdown, setShowOrgDropdown] = useState(false)
  const orgInputRef = useRef(null)
  const orgContainerRef = useRef(null)

  // Buscar organizadores con debounce
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

  // Cerrar dropdown al hacer click fuera
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

  const [orgError, setOrgError] = useState('')

  const handleOrgKeyDown = async (e) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      const trimmed = orgQuery.trim()
      if (!trimmed) return

      // Siempre consultar la BD en tiempo real al presionar Enter
      try {
        const results = await eventService.buscarOrganizadores(trimmed)
        // Filtrar los que ya están seleccionados
        const available = results.filter(
          o => !selectedOrgs.some(s => s.idOrganizador === o.idOrganizador)
        )

        if (available.length > 0) {
          // Buscar coincidencia exacta primero
          const exactMatch = available.find(
            o => o.nombre.toLowerCase() === trimmed.toLowerCase()
          )
          handleSelectOrg(exactMatch || available[0])
          setOrgError('')
        } else if (results.length > 0) {
          // Existe pero ya está seleccionado
          setOrgError(`"${trimmed}" ya fue agregado.`)
        } else {
          // No existe en la BD
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

  const v = (val) => val && String(val).trim() ? 'is-valid' : ''
  const vTolerance = () => formData.tiempoToleranciaMinutos !== '' ? 'is-valid' : ''

  const handleBannerClick = () => fileInputRef.current?.click()
  const handleBannerChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setBannerPreview(URL.createObjectURL(file))
      setFormData(prev => ({ ...prev, banner: file }))
    }
  }

  const resetForm = () => {
    setFormData({
      nombre: '', ubicacion: '', descripcion: '', fechaInicio: '', fechaFin: '',
      idCategoria: '', capacidadMaxima: '', tiempoCancelacionHoras: '',
      tiempoToleranciaMinutos: '', banner: null,
    })
    setBannerPreview(null)
    setSelectedOrgs([])
    setOrgQuery('')
    setOrgError('')
  }

  const handleSave = async () => {
    // Custom validation — sin tooltips del navegador
    const { nombre, ubicacion, descripcion, fechaInicio, fechaFin, idCategoria, capacidadMaxima, tiempoCancelacionHoras, tiempoToleranciaMinutos } = formData
    if (!nombre.trim() || !ubicacion.trim() || !descripcion.trim() || !fechaInicio || !fechaFin || !idCategoria || !capacidadMaxima || !tiempoCancelacionHoras || !tiempoToleranciaMinutos) {
      setShowError(true)
      setShowSuccess(false)
      return
    }
    try {
      // Incluir IDs de organizadores seleccionados en el formData
      const dataToSubmit = {
        ...formData,
        organizadores: selectedOrgs.map(o => o.idOrganizador),
      }
      await onSubmit(dataToSubmit)
      // Cerrar el modal haciendo click en su botón de cierre
      closeBtnRef.current?.click()
      resetForm()
      setShowSuccess(true)
      setShowError(false)
    } catch {
      setShowError(true)
      setShowSuccess(false)
    }
  }

  return (
    <>
      <div className="modal fade" id="crearEventoModal" tabIndex="-1" aria-labelledby="crearEventoModalLabel" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <form ref={formRef} noValidate className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <h5 className="fw-bold">{t('createEvent.title')}</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label={t('common.close')} ref={closeBtnRef}></button>
          </div>

          <div className="modal-body px-4 py-3">

            {/* Banner upload */}
            <div className="mb-4">
              <label className="form-label fw-semibold small">{t('createEvent.banner')}</label>
              <input type="file" ref={fileInputRef} accept="image/*" className="d-none" onChange={handleBannerChange} />
              <div
                className="d-flex flex-column align-items-center justify-content-center text-center p-4 rounded-3"
                style={{ border: `2px dashed ${bannerPreview ? '#198754' : '#dee2e6'}`, cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                onClick={handleBannerClick}
              >
                {bannerPreview ? (
                  <img src={bannerPreview} alt="Preview" style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', objectFit: 'cover' }} />
                ) : (
                  <>
                    <i className="bi bi-image text-secondary fs-3 mb-2"></i>
                    <div className="text-secondary small">{t('createEvent.bannerInstruction')}</div>
                    <div className="text-secondary small" style={{ fontSize: '11px' }}>{t('createEvent.bannerRecommended')}</div>
                  </>
                )}
              </div>
            </div>

            {/* Nombre + Ubicación */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.eventName')}</label>
                <input
                  type="text"
                  name="nombre"
                  className={`form-control ${v(formData.nombre)}`}
                  placeholder={t('createEvent.eventNamePlaceholder')}
                  value={formData.nombre}
                  onChange={handleChange}
                  maxLength={100}
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.location')}</label>
                <input
                  type="text"
                  name="ubicacion"
                  className={`form-control ${v(formData.ubicacion)}`}
                  placeholder={t('createEvent.locationPlaceholder')}
                  value={formData.ubicacion}
                  onChange={handleChange}
                  maxLength={150}
                />
              </div>
            </div>

            {/* Descripción */}
            <div className="mb-3">
              <label className="form-label fw-semibold small">{t('createEvent.description')}</label>
              <textarea
                name="descripcion"
                className={`form-control ${v(formData.descripcion)}`}
                rows="3"
                placeholder={t('createEvent.descriptionPlaceholder')}
                value={formData.descripcion}
                onChange={handleChange}
                maxLength={300}
              ></textarea>
            </div>

            {/* Fechas */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.startDateTime')}</label>
                <input
                  type="datetime-local"
                  name="fechaInicio"
                  className={`form-control ${v(formData.fechaInicio)}`}
                  value={formData.fechaInicio}
                  onChange={handleChange}
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>{t('createEvent.futureDate')}</div>
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.endDateTime')}</label>
                <input
                  type="datetime-local"
                  name="fechaFin"
                  className={`form-control ${v(formData.fechaFin)}`}
                  value={formData.fechaFin}
                  onChange={handleChange}
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>{t('createEvent.afterStart')}</div>
              </div>
            </div>

            {/* Categoría + Capacidad */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.category')}</label>
                <select
                  name="idCategoria"
                  className={`form-select ${v(formData.idCategoria)}`}
                  value={formData.idCategoria}
                  onChange={handleChange}
                >
                  <option value="">{t('createEvent.selectCategory')}</option>
                  {categorias.map(cat => (
                    <option key={cat.idCategoria} value={cat.idCategoria}>
                      {cat.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.maxCapacity')}</label>
                <input
                  type="number"
                  name="capacidadMaxima"
                  className={`form-control ${v(formData.capacidadMaxima)}`}
                  placeholder={t('createEvent.capacityPlaceholder')}
                  min="1"
                  max="9999"
                  value={formData.capacidadMaxima}
                  onChange={handleChange}
                />
              </div>
            </div>

            {/* Cancelación + Tolerancia */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.cancellationTime')}</label>
                <input
                  type="number"
                  name="tiempoCancelacionHoras"
                  className={`form-control ${v(formData.tiempoCancelacionHoras)}`}
                  placeholder={t('createEvent.cancellationPlaceholder')}
                  min="1"
                  max="720"
                  value={formData.tiempoCancelacionHoras}
                  onChange={handleChange}
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>{t('createEvent.cancellationHelp')}</div>
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">{t('createEvent.toleranceTime')}</label>
                <input
                  type="number"
                  name="tiempoToleranciaMinutos"
                  className={`form-control ${vTolerance()}`}
                  placeholder={t('createEvent.tolerancePlaceholder')}
                  min="0"
                  max="120"
                  value={formData.tiempoToleranciaMinutos}
                  onChange={handleChange}
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>{t('createEvent.toleranceHelp')}</div>
              </div>
            </div>

            {/* Organizador con búsqueda y etiquetas */}
            <div className="mb-2" ref={orgContainerRef} style={{ position: 'relative' }}>
              <label className="form-label fw-semibold small">{t('createEvent.organizer')}</label>
              <div
                className={`d-flex flex-wrap align-items-center gap-2 form-control p-2 ${selectedOrgs.length > 0 ? 'is-valid' : ''}`}
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
                  placeholder={selectedOrgs.length === 0 ? t('createEvent.organizerPlaceholder') : t('createEvent.addAnother')}
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

              {/* Dropdown de sugerencias */}
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
                {t('createEvent.organizerHelp')}
              </div>
            </div>

          </div>

          <div className="modal-footer border-top px-4 py-3">
            <button type="button" className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal">
              {t('common.cancel')}
            </button>
            <button
              type="button"
              className="btn btn-primary rounded-pill px-4"
              onClick={handleSave}
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  {t('createEvent.creating')}
                </>
              ) : (
                t('createEvent.saveEvent')
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
              <h5>{t('createEvent.eventCreated')}</h5>
              <p className="text-secondary small">{t('createEvent.eventSaved')}</p>
              <button className="btn btn-primary rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowSuccess(false)}>
                {t('createEvent.accept')}
              </button>
          </div>
        </div>
      )}

      {/* Error modal */}
      {showError && (
        <div className="position-fixed top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="bg-white border-0 rounded-4 shadow text-center p-4" style={{ maxWidth: '350px', width: '90%' }}>
              <div className="mb-3">
                <i className="bi bi-x-circle-fill text-danger" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>{t('createEvent.reviewData')}</h5>
              <p className="text-secondary small">{t('createEvent.requiredFields')}</p>
              <button className="btn btn-danger rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowError(false)}>
                {t('createEvent.understood')}
              </button>
          </div>
        </div>
      )}
    </>
  )
}

export default CrearEventoModal
