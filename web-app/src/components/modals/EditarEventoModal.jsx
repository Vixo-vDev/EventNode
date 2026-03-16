import { useState, useRef } from 'react'
import eventTechSummit from '../../assets/events/event_tech_summit.png'

function EditarEventoModal() {
  const fileInputRef = useRef(null);
  const formRef = useRef(null);
  const [bannerPreview, setBannerPreview] = useState(eventTechSummit);
  const [showSuccess, setShowSuccess] = useState(false);
  const [showError, setShowError] = useState(false);

  const handleBannerClick = () => fileInputRef.current?.click();
  const handleBannerChange = (e) => {
    const file = e.target.files[0];
    if (file) setBannerPreview(URL.createObjectURL(file));
  };
  const handleSave = () => {
    if (formRef.current && formRef.current.checkValidity()) {
      setShowSuccess(true);
      setShowError(false);
    } else {
      setShowError(true);
      setShowSuccess(false);
    }
  };

  return (
    <div className="modal fade" id="editarEventoModal" tabIndex="-1" aria-labelledby="editarEventoModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg modal-dialog-scrollable">
        <form ref={formRef} noValidate className="modal-content border-0 rounded-4 shadow">
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
              <input type="file" ref={fileInputRef} accept="image/*" className="d-none" onChange={handleBannerChange} />
              <div className="rounded-3 overflow-hidden position-relative" style={{ cursor: 'pointer' }} onClick={handleBannerClick}>
                <img
                  src={bannerPreview}
                  alt="Banner del evento"
                  className="w-100"
                  style={{ height: '180px', objectFit: 'cover' }}
                />
                <div className="position-absolute top-50 start-50 translate-middle bg-dark bg-opacity-50 text-white px-3 py-2 rounded-pill d-flex align-items-center gap-2" style={{ pointerEvents: 'none' }}>
                  <i className="bi bi-camera"></i> Cambiar Banner
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <label className="form-label text-secondary small">Nombre del Evento*</label>
                <input type="text" className="form-control" defaultValue="Tech Summit 2023" required />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-secondary small">Ubicación*</label>
                <div className="input-group">
                  <span className="input-group-text bg-white">
                    <i className="bi bi-geo-alt text-secondary"></i>
                  </span>
                  <input type="text" className="form-control" defaultValue="Centro de Convenciones, Ciudad de México" required />
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12">
                <label className="form-label text-secondary small">Descripción*</label>
                <textarea className="form-control" rows="3" defaultValue="El evento tecnológico más importante del año, donde expertos de todo el mundo se reúnen para discutir las últimas tendencias en IA, desarrollo web y ciberseguridad." required></textarea>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-6">
                <div className="mb-3">
                  <label className="form-label text-secondary small">Inicio*</label>
                  <input type="text" className="form-control" defaultValue="11/11/2023" required />
                </div>
                <div>
                  <label className="form-label text-secondary small">Fin*</label>
                  <input type="text" className="form-control" defaultValue="11/17/2023" required />
                </div>
              </div>
              <div className="col-12 col-md-6">
                <div className="mb-3">
                  <label className="form-label text-secondary small">Hora Inicio*</label>
                  <input type="text" className="form-control" defaultValue="09:00 AM" required />
                </div>
                <div>
                  <label className="form-label text-secondary small">Hora Fin*</label>
                  <input type="text" className="form-control" defaultValue="06:00 PM" required />
                </div>
              </div>
            </div>

            <div className="row g-3 mb-3">
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Categoría*</label>
                <select className="form-select" required>
                  <option value="1">Tecnología</option>
                  <option value="2">Ciencia</option>
                  <option value="3">Arte</option>
                  <option value="4">Marketing</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Diseño de Diploma</label>
                <select className="form-select">
                  <option value="1">Jasper Classic</option>
                  <option value="2">Modern Blue</option>
                  <option value="3">Elegant Gold</option>
                </select>
              </div>
              <div className="col-12 col-md-4">
                <label className="form-label text-secondary small">Capacidad Máxima*</label>
                <div className="input-group">
                  <span className="input-group-text bg-white">
                    <i className="bi bi-people text-secondary"></i>
                  </span>
                  <input type="number" className="form-control" defaultValue="500" required min="1" />
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
            <button type="button" className="btn btn-primary rounded-pill px-4 d-flex align-items-center gap-2" onClick={handleSave}>
              <i className="bi bi-check-circle"></i>
              Actualizar Evento
            </button>
          </div>
        </form>
      </div>

      {showSuccess && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content border-0 rounded-4 shadow text-center p-4">
              <div className="mb-3">
                <i className="bi bi-check-circle-fill text-success" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>¡Evento Actualizado!</h5>
              <p className="text-secondary small">Los cambios se han guardado exitosamente.</p>
              <button className="btn btn-primary rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowSuccess(false)} data-bs-dismiss="modal">Aceptar</button>
            </div>
          </div>
        </div>
      )}

      {showError && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}>
          <div className="modal-dialog modal-dialog-centered modal-sm">
            <div className="modal-content border-0 rounded-4 shadow text-center p-4">
              <div className="mb-3">
                <i className="bi bi-x-circle-fill text-danger" style={{ fontSize: '3rem' }}></i>
              </div>
              <h5>Revisa los datos</h5>
              <p className="text-secondary small">Por favor, asegúrate de completar todos los campos obligatorios (*) antes de continuar.</p>
              <button className="btn btn-danger rounded-pill px-4 mt-2 mx-auto" onClick={() => setShowError(false)}>Entendido</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default EditarEventoModal
