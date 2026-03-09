import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'
import eventTechSummit from '../../assets/events/event_tech_summit.png'

function AdminEventDetail() {
  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div className="d-flex align-items-center gap-2">
          <Link to="/admin/eventos" className="text-dark text-decoration-none">
            <i className="bi bi-arrow-left fs-5"></i>
          </Link>
          <h5 className="fw-bold mb-0">Tech Summit 2023</h5>
        </div>
        <button className="btn btn-primary rounded-pill d-flex align-items-center gap-2">
          <i className="bi bi-pencil"></i>
          Edit Event
        </button>
      </div>

      <div className="card border-0 rounded-3 overflow-hidden mb-4">
        <div className="position-relative" style={{ height: '240px' }}>
          <img
            src={eventTechSummit}
            alt="Tech Summit 2023"
            className="w-100 h-100"
            style={{ objectFit: 'cover' }}
          />
          <div className="position-absolute top-0 start-0 w-100 h-100 d-flex flex-column justify-content-end p-4"
            style={{ background: 'linear-gradient(transparent 20%, rgba(0,0,0,0.8) 100%)' }}>
            <div className="d-flex gap-2 mb-2">
              <span className="badge bg-success rounded-pill px-3 small">ACTIVE</span>
              <span className="badge bg-dark bg-opacity-75 rounded-pill px-3 small">TECHNOLOGY</span>
            </div>
            <h2 className="text-white fw-bold mb-2">TECH SUMMIT 2023</h2>
            <p className="text-white text-opacity-75 small mb-0" style={{ maxWidth: '600px' }}>
              Conferencia tecnológica global para desarrolladores e innovadores centrada
              en el futuro de la IA y la nube. Únase a nosotros para vivir una experiencia
              transformadora.
            </p>
          </div>
        </div>
      </div>

      <div className="row g-3 mb-4">
        <div className="col-12 col-lg-8">
          <div className="row g-3 mb-3">
            <div className="col-12 col-md-6">
              <div className="card border-0 shadow-sm rounded-3">
                <div className="card-body p-3">
                  <div className="d-flex align-items-center gap-2 text-secondary small mb-2">
                    <i className="bi bi-people-fill text-primary"></i>
                    <span className="text-uppercase fw-bold" style={{ fontSize: '11px' }}>Capacidad</span>
                  </div>
                  <div className="d-flex align-items-center gap-3">
                    <span className="fw-bold fs-3">82%</span>
                    <div className="progress flex-grow-1" style={{ height: '6px' }}>
                      <div className="progress-bar bg-primary" style={{ width: '82%' }}></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="col-12 col-md-6">
              <div className="card border-0 shadow-sm rounded-3">
                <div className="card-body p-3">
                  <div className="d-flex align-items-center gap-2 text-secondary small mb-2">
                    <i className="bi bi-display text-primary"></i>
                    <span className="text-uppercase fw-bold" style={{ fontSize: '11px' }}>Diplomas</span>
                  </div>
                  <div className="fw-bold fs-3">350</div>
                </div>
              </div>
            </div>
          </div>

          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-3">
              <h6 className="fw-bold mb-1">Lista de Estudiantes</h6>
              <p className="text-secondary small mb-3">
                Gestionar el estado de los estudiantes y realizar un seguimiento de la asistencia.
              </p>
              <div className="d-flex gap-2">
                <button className="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                  <i className="bi bi-download"></i>
                  Pre Check - In
                </button>
                <button className="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                  <i className="bi bi-download"></i>
                  Check - In
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-4">
          <div className="card border-0 rounded-3 mb-3"
            style={{ backgroundColor: '#fff0f0', border: '1px solid #fecaca' }}>
            <div className="card-body p-3">
              <div className="text-danger text-uppercase small fw-bold mb-2" style={{ fontSize: '11px' }}>
                Cancelar Evento
              </div>
              <p className="text-secondary small mb-3">
                La cancelación del evento notificará a todos los estudiantes registrados y ya no se permitirá las inscripciones.
              </p>
              <button className="btn btn-danger rounded-pill w-100">
                Cancelar Evento
              </button>
            </div>
          </div>

          <div className="card border-0 rounded-3"
            style={{ backgroundColor: '#e8f4fd', border: '1px solid #bde0fe' }}>
            <div className="card-body p-3">
              <div className="text-primary text-uppercase small fw-bold mb-2" style={{ fontSize: '11px' }}>
                Enviar Diplomas
              </div>
              <p className="text-secondary small mb-3">
                Se mandara automáticamente los diplomas a los estudiantes con Check - In
              </p>
              <button className="btn btn-primary rounded-pill w-100">
                Enviar Diplomas
              </button>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default AdminEventDetail
