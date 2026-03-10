import eventTechSummit from '../../assets/events/event_tech_summit.png'

function EditarEventoModal() {
  return (
    <div className="modal fade" id="editarEventoModal" tabIndex="-1" aria-labelledby="editarEventoModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <div className="modal-content border-0 rounded-4 shadow">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <div>
              <h5 className="fw-bold mb-1">Editar Evento</h5>
              <p className="text-secondary small mb-0">
                Actualiza la información detallada de tu evento en EventNode.
              </p>
            </div>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          <div className="modal-body px-4 py-3">
            <div className="mb-4">
              <label className="form-label text-uppercase text-secondary small fw-bold">Banner del Evento</label>
              <div className="rounded-3 overflow-hidden">
                <img
                  src={eventTechSummit}
                  alt="Banner del evento"
                  className="w-100"
                  style={{ height: '180px', objectFit: 'cover' }}
                />
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label text-secondary small">Nombre del Evento</label>
                <input type="text" className="form-control" defaultValue="Tech Summit 2023" />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-secondary small">Ubicación</label>
                <div className="input-group">
                  <span className="input-group-text bg-white">
                    <i className="bi bi-geo-alt text-secondary"></i>
                  </span>
                  <input type="text" className="form-control" defaultValue="Centro de Convenciones, Ciudad de México" />
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Descripción</label>
                <textarea className="form-control" rows="4" defaultValue="El evento tecnológico más importante del año, donde expertos de todo el mundo se reúnen para discutir las últimas tendencias en IA, desarrollo web y ciberseguridad."></textarea>
              </div>
              <div className="col-12 col-md-4">
                <div className="mb-3">
                  <label className="form-label text-secondary small">Inicio</label>
                  <input type="text" className="form-control" defaultValue="11/11/2023" />
                </div>
                <div>
                  <label className="form-label text-secondary small">Fin</label>
                  <input type="text" className="form-control" defaultValue="11/17/2023" />
                </div>
              </div>
              <div className="col-12 col-md-4">
                <div className="mb-3">
                  <label className="form-label text-secondary small">Hora Inicio</label>
                  <input type="text" className="form-control" defaultValue="09:00 AM" />
                </div>
                <div>
                  <label className="form-label text-secondary small">Hora Fin</label>
                  <input type="text" className="form-control" defaultValue="06:00 PM" />
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Categoría</label>
                <select className="form-select">
                  <option selected>Tecnología</option>
                  <option>Ciencia</option>
                  <option>Arte</option>
                  <option>Marketing</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Diseño de Diploma</label>
                <select className="form-select">
                  <option selected>Jasper Classic</option>
                  <option>Modern Blue</option>
                  <option>Elegant Gold</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Capacidad Máxima</label>
                <div className="input-group">
                  <span className="input-group-text bg-white">
                    <i className="bi bi-people text-secondary"></i>
                  </span>
                  <input type="number" className="form-control" defaultValue="500" />
                </div>
              </div>
            </div>

            <div className="mb-2">
              <label className="form-label text-secondary small">Organizadores</label>
              <div className="d-flex flex-wrap align-items-center gap-2 form-control p-2">
                <span className="badge bg-primary bg-opacity-10 text-primary d-flex align-items-center gap-1 px-2 py-1 rounded-pill">
                  Nombre 1
                  <button type="button" className="btn-close btn-close-sm" style={{ fontSize: '8px' }}></button>
                </span>
                <span className="badge bg-primary bg-opacity-10 text-primary d-flex align-items-center gap-1 px-2 py-1 rounded-pill">
                  Nombre 2
                  <button type="button" className="btn-close btn-close-sm" style={{ fontSize: '8px' }}></button>
                </span>
                <input
                  type="text"
                  className="border-0 flex-grow-1 small"
                  placeholder="Añadir..."
                  style={{ outline: 'none', minWidth: '80px' }}
                />
              </div>
            </div>
          </div>
          <div className="modal-footer border-top px-4 py-3">
            <button type="button" className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal">
              Cancelar
            </button>
            <button type="button" className="btn btn-primary rounded-pill px-4 d-flex align-items-center gap-2">
              <i className="bi bi-check-circle"></i>
              Actualizar Evento
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarEventoModal
