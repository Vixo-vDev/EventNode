function CrearEventoModal({ formData, categorias, error, isLoading, onChange, onSubmit }) {
  return (
    <div className="modal fade" id="crearEventoModal" tabIndex="-1" aria-labelledby="crearEventoModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <h5 className="fw-bold">Crear Nuevo Evento</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          <form onSubmit={onSubmit}>
            <div className="modal-body px-4 py-3">
              {error && (
                <div className="alert alert-danger py-2 small" role="alert">
                  {error}
                </div>
              )}

              <div className="row g-3 mb-3">
                <div className="col-12 col-md-6">
                  <label className="form-label fw-semibold small">Nombre del evento *</label>
                  <input
                    type="text"
                    name="nombre"
                    className="form-control"
                    placeholder="Ej: Workshop de IA Generativa"
                    value={formData.nombre}
                    onChange={onChange}
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
                    onChange={onChange}
                    required
                  />
                </div>
              </div>

              <div className="mb-3">
                <label className="form-label fw-semibold small">Descripción *</label>
                <textarea
                  name="descripcion"
                  className="form-control"
                  rows="3"
                  placeholder="Describe de qué trata el evento..."
                  value={formData.descripcion}
                  onChange={onChange}
                  required
                ></textarea>
              </div>

              <div className="row g-3 mb-3">
                <div className="col-12 col-md-6">
                  <label className="form-label fw-semibold small">Fecha y Hora Inicio *</label>
                  <input
                    type="datetime-local"
                    name="fechaInicio"
                    className="form-control"
                    value={formData.fechaInicio}
                    onChange={onChange}
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
                    onChange={onChange}
                    required
                  />
                  <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Posterior al inicio.</div>
                </div>
              </div>

              <div className="row g-3 mb-3">
                <div className="col-12 col-md-4">
                  <label className="form-label fw-semibold small">Categoría *</label>
                  <select
                    name="idCategoria"
                    className="form-select"
                    value={formData.idCategoria}
                    onChange={onChange}
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
                  <label className="form-label fw-semibold small">Capacidad Máxima *</label>
                  <input
                    type="number"
                    name="capacidadMaxima"
                    className="form-control"
                    placeholder="Ej: 100"
                    min="1"
                    value={formData.capacidadMaxima}
                    onChange={onChange}
                    required
                  />
                </div>
                <div className="col-12 col-md-4">
                  <label className="form-label fw-semibold small">Tiempo Cancelación (hrs) *</label>
                  <input
                    type="number"
                    name="tiempoCancelacionHoras"
                    className="form-control"
                    placeholder="Ej: 24"
                    min="1"
                    value={formData.tiempoCancelacionHoras}
                    onChange={onChange}
                    required
                  />
                  <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Horas antes del evento para cancelar inscripción.</div>
                </div>
              </div>

              <div className="row g-3 mb-3">
                <div className="col-12 col-md-6">
                  <label className="form-label fw-semibold small">Tolerancia de entrada (min) *</label>
                  <input
                    type="number"
                    name="tiempoToleranciaMinutos"
                    className="form-control"
                    placeholder="Ej: 15"
                    min="0"
                    value={formData.tiempoToleranciaMinutos}
                    onChange={onChange}
                    required
                  />
                  <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Minutos de tolerancia para el check-in.</div>
                </div>
                <div className="col-12 col-md-6">
                  <label className="form-label fw-semibold small">URL del Banner</label>
                  <input
                    type="text"
                    name="banner"
                    className="form-control"
                    placeholder="Ej: https://ejemplo.com/banner.png (opcional)"
                    value={formData.banner}
                    onChange={onChange}
                  />
                  <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Imagen .jpg, .jpeg o .png</div>
                </div>
              </div>
            </div>

            <div className="modal-footer border-top px-4 py-3">
              <button type="button" className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal">
                Cancelar
              </button>
              <button
                type="submit"
                className="btn btn-primary rounded-pill px-4"
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
      </div>
    </div>
  )
}

export default CrearEventoModal
