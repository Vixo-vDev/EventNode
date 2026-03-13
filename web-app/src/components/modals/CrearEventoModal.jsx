function CrearEventoModal() {
  return (
    <div className="modal fade" id="crearEventoModal" tabIndex="-1" aria-labelledby="crearEventoModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <h5 className="fw-bold">Crear Nuevo Evento</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          <div className="modal-body px-4 py-3">
            <div className="mb-4">
              <label className="form-label fw-semibold small">Banner del evento</label>
              <div
                className="d-flex flex-column align-items-center justify-content-center text-center p-4 rounded-3"
                style={{ border: '2px dashed #dee2e6', cursor: 'pointer' }}
              >
                <i className="bi bi-image text-secondary fs-3 mb-2"></i>
                <div className="text-secondary small">Haz clic o arrastra una imagen para el banner</div>
                <div className="text-secondary small" style={{ fontSize: '11px' }}>
                  Recomendado: 1200×480px (PNG, JPG)
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Nombre del evento*</label>
                <input type="text" className="form-control" placeholder="Ej: Workshop de IA Generativa" />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label fw-semibold small">Ubicación*</label>
                <input type="text" className="form-control" placeholder="Ej: Auditorio Central, Aula 101 o Link de Zoom" />
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Descripción*</label>
                <textarea className="form-control" rows="2" placeholder="Describe de qué trata el evento..."></textarea>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Fecha y Hora Inicio*</label>
                <input type="text" className="form-control" placeholder="mm/dd/yyyy, --:-- --" />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Debe ser una fecha futura.</div>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Fecha y Hora Fin*</label>
                <input type="text" className="form-control" placeholder="mm/dd/yyyy, --:-- --" />
                <div className="text-secondary small mt-1" style={{ fontSize: '11px' }}>Posterior al inicio.</div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Categorías*</label>
                <select className="form-select">
                  <option>Seleccionar...</option>
                  <option>Tecnología</option>
                  <option>Ciencia</option>
                  <option>Arte</option>
                  <option>Marketing</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Diseño de Diploma</label>
                <select className="form-select">
                  <option>Jasper Classic</option>
                  <option>Modern Blue</option>
                  <option>Elegant Gold</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label fw-semibold small">Capacidad Máxima*</label>
                <input type="number" className="form-control" placeholder="Ej: 100" />
              </div>
            </div>

            <div className="mb-2">
              <label className="form-label fw-semibold small">Organizador (opcional)</label>
              <div className="d-flex flex-wrap align-items-center gap-2 form-control p-2">
                <span className="badge bg-light text-dark d-flex align-items-center gap-1 px-2 py-1 rounded-pill">
                  Nombre 1
                  <button type="button" className="btn-close btn-close-sm" style={{ fontSize: '8px' }}></button>
                </span>
                <span className="badge bg-light text-dark d-flex align-items-center gap-1 px-2 py-1 rounded-pill">
                  Nombre 2
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
            <button type="button" className="btn btn-primary rounded-pill px-4">
              Guardar Evento
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CrearEventoModal
