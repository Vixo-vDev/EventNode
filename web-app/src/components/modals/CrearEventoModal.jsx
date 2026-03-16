import { useState, useRef } from 'react'

function CrearEventoModal({ categorias = [], isLoading, onSubmit }) {
  const fileInputRef = useRef(null)
  const formRef = useRef(null)
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
    idDiploma: '',
    capacidadMaxima: '',
    tiempoCancelacionHoras: '',
    tiempoToleranciaMinutos: '',
    banner: null,
  })

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
    if (!formRef.current?.checkValidity()) {
      formRef.current?.reportValidity()
      setShowError(true)
      setShowSuccess(false)
      return
    }
    try {
      await onSubmit(formData)
      setShowSuccess(true)
      setShowError(false)
    } catch {
      setShowError(true)
      setShowSuccess(false)
    }
  }

  return (
    <div className="modal fade" id="crearEventoModal" tabIndex="-1" aria-labelledby="crearEventoModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <form ref={formRef} noValidate className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <h5 className="fw-bold">Crear Nuevo Evento</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>

          <div className="modal-body px-4 py-3">

            {/* Banner upload */}
            <div className="mb-4">
              <label className="form-label fw-semibold small">Banner del evento</label>
              <input type="file" ref={fileInputRef} accept="image/*" className="d-none" onChange={handleBannerChange} />
              <div
                className="d-flex flex-column align-items-center justify-content-center text-center p-4 rounded-3"
                style={{ border: '2px dashed #dee2e6', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                onClick={handleBannerClick}
              >
                {bannerPreview ? (
                  <img src={bannerPreview} alt="Preview" style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', objectFit: 'cover' }} />
                ) : (
                  <>
                    <i className="bi bi-image text-secondary fs-3 mb-2"></i>
                    <div className="text-secondary small">Haz clic para abrir el explorador y selecciona una imagen</div>
                    <div className="text-secondary small" style={{ fontSize: '11px' }}>Recomendado: 1200×480px (PNG, JPG)</div>
                  </>
                )}
              </div>
            </div>

            {/* Nombre + Ubicación */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Nombre del evento *</label>
                <input
                  type="text"
                  name="nombre"
                  className="form-control"
                  placeholder="Ej: Workshop de IA Generativa"
                  value={formData.nombre}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Ubicación *</label>
                <input
                  type="text"
                  name="ubicacion"
                  className="form-control"
                  placeholder="Ej: Auditorio Central, Aula 101 o Link de Zoom"
                  value={formData.ubicacion}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            {/* Descripción */}
            <div className="mb-3">
              <label className="form-label fw-semibold small">Descripción *</label>
              <textarea
                name="descripcion"
                className="form-control"
                rows="3"
                placeholder="Describe de qué trata el evento..."
                value={formData.descripcion}
                onChange={handleChange}
                required
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
                  required
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Debe ser una fecha futura.</div>
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Fecha y Hora Fin *</label>
                <input
                  type="datetime-local"
                  name="fechaFin"
                  className="form-control"
                  value={formData.fechaFin}
                  onChange={handleChange}
                  required
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Posterior al inicio.</div>
              </div>
            </div>

            {/* Categoría + Diploma + Capacidad */}
            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Categoría *</label>
                <select
                  name="idCategoria"
                  className="form-select"
                  value={formData.idCategoria}
                  onChange={handleChange}
                  required
                >
                  <option value="">Seleccionar...</option>
                  {categorias.map(cat => (
                    <option key={cat.idCategoria} value={cat.idCategoria}>
                      {cat.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Diseño de Diploma</label>
                <select
                  name="idDiploma"
                  className="form-select"
                  value={formData.idDiploma}
                  onChange={handleChange}
                >
                  <option value="1">Jasper Classic</option>
                  <option value="2">Modern Blue</option>
                  <option value="3">Elegant Gold</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Capacidad Máxima *</label>
                <input
                  type="number"
                  name="capacidadMaxima"
                  className="form-control"
                  placeholder="Ej: 100"
                  min="1"
                  value={formData.capacidadMaxima}
                  onChange={handleChange}
                  required
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
                  required
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Horas antes del evento para cancelar inscripción.</div>
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
                  required
                />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Minutos de tolerancia para el check-in.</div>
              </div>
            </div>

            {/* Organizador */}
            <div className="mb-2">
              <label className="form-label fw-semibold small">Organizador (opcional)</label>
              <div className="d-flex flex-wrap align-items-center gap-2 form-control p-2">
                <span className="badge bg-light text-dark d-flex align-items-center gap-1 px-2 py-1 rounded-pill">
                  Nombre 1
                  <button type="button" className="btn-close btn-close-sm" style={{ fontSize: '8px' }}></button>
                </span>
                <input
                  type="text"
                  className="border-0 flex-grow-1 small"
                  placeholder="Escribe y presiona Enter..."
                  style={{ outline: 'none', minWidth: '120px' }}
                />
              </div>
            </div>

          </div>

          <div className="modal-footer border-top px-4 py-3">
            <button type="button" className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal">
              Cancelar
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
                  Creando...
                </>
              ) : (
                'Guardar Evento'
              )}
            </button>
          </div>
        </form>
      </div>

      {/* Success modal */}
      {showSuccess && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content border-0 rounded-4 shadow text-center p-4">
              <div className="mb-3">
                <i className="bi bi-check-circle-fill text-success" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>¡Evento Creado con Éxito!</h5>
              <p className="text-secondary small">Su evento ha sido guardado correctamente.</p>
              <button className="btn btn-primary rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowSuccess(false)} data-bs-dismiss="modal">
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Error modal */}
      {showError && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="modal-dialog modal-dialog-centered modal-sm">
            <div className="modal-content border-0 rounded-4 shadow text-center p-4">
              <div className="mb-3">
                <i className="bi bi-x-circle-fill text-danger" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>Revisa los datos</h5>
              <p className="text-secondary small">Por favor, asegúrate de completar todos los campos obligatorios (*) antes de continuar.</p>
              <button className="btn btn-danger rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowError(false)}>
                Entendido
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default CrearEventoModal
