import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'
import eventTechSummit from '../../assets/events/event_tech_summit.png'
import EditarEventoModal from '../../components/modals/EditarEventoModal'

function AdminEventDetail() {
  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="d-flex align-items-center gap-2">
          <Link to="/admin/eventos" className="text-dark text-decoration-none">
            <i className="bi bi-arrow-left fs-5"></i>
          </Link>
          <h5 className="fw-bold mb-0">Tech Summit 2023</h5>
        </div>
        <button
          className="btn btn-primary rounded-pill d-flex align-items-center gap-2 btn-sm px-3"
          data-bs-toggle="modal"
          data-bs-target="#editarEventoModal"
        >
          <i className="bi bi-pencil-square"></i>
          Edit Event
        </button>
      </div>

      <div className="rounded-4 overflow-hidden mb-4 position-relative" style={{ height: '220px' }}>
        <img
          src={eventTechSummit}
          alt="Tech Summit 2023"
          className="w-100 h-100"
          style={{ objectFit: 'cover', filter: 'brightness(0.4)' }}
        />
        <div className="position-absolute bottom-0 start-0 p-4">
          <div className="d-flex gap-2 mb-2">
            <span className="badge bg-success rounded-pill px-3 small">ACTIVE</span>
            <span className="badge bg-purple rounded-pill px-3 small"
              style={{ backgroundColor: '#7c3aed' }}>TECHNOLOGY</span>
          </div>
          <h2 className="text-white fw-bold mb-2">TECH SUMMIT 2023</h2>
          <p className="text-white text-opacity-75 small mb-0" style={{ maxWidth: '500px' }}>
            Conferencia tecnológica global para desarrolladores e innovadores centrada
            en el futuro de la IA y la nube. Únase a nosotros para vivir una experiencia
            transformadora.
          </p>
        </div>
      </div>

      <div className="row g-3 mb-4">
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex align-items-center gap-2 mb-2">
                <i className="bi bi-people-fill text-primary"></i>
                <span className="text-uppercase text-secondary small fw-bold">Capacidad</span>
              </div>
              <div className="d-flex align-items-center gap-3">
                <span className="fw-bold fs-2">82%</span>
                <div className="progress flex-grow-1" style={{ height: '6px' }}>
                  <div className="progress-bar bg-primary" style={{ width: '82%' }}></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex align-items-center gap-2 mb-2">
                <i className="bi bi-award-fill text-primary"></i>
                <span className="text-uppercase text-secondary small fw-bold">Diplomas</span>
              </div>
              <div className="fw-bold fs-2">350</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 rounded-3 h-100 bg-danger bg-opacity-10 border border-danger border-opacity-25">
            <div className="card-body p-3">
              <div className="text-uppercase text-danger small fw-bold mb-1">Cancelar Evento</div>
              <p className="text-secondary small mb-2" style={{ fontSize: '11px' }}>
                La cancelación del evento notificará a todos los estudiantes registrados y ya no se permitirá la inscripción.
              </p>
              <button className="btn btn-danger rounded-pill w-100 btn-sm">
                Cancelar Evento
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="row g-3">
        <div className="col-12 col-md-8">
          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-4">
              <h6 className="fw-bold mb-1">Lista de Estudiantes</h6>
              <p className="text-secondary small mb-3">
                Gestionar el estado de los estudiantes y realizar un seguimiento de la asistencia.
              </p>
              <div className="d-flex gap-3">
                <Link to="/admin/evento/1/pre-check-in" className="btn btn-outline-secondary btn-sm d-flex align-items-center gap-1 rounded-pill px-3 text-decoration-none">
                  <i className="bi bi-person-check"></i>
                  Pre Check - In
                </Link>
                <Link to="/admin/evento/1/check-in" className="btn btn-outline-secondary btn-sm d-flex align-items-center gap-1 rounded-pill px-3 text-decoration-none">
                  <i className="bi bi-person-check-fill"></i>
                  Check - In
                </Link>
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 rounded-3 h-100 text-white"
            style={{ background: 'linear-gradient(135deg, #2563eb 0%, #1e40af 100%)' }}>
            <div className="card-body p-3">
              <div className="text-uppercase small fw-bold mb-1">Enviar Diplomas</div>
              <p className="small opacity-75 mb-2" style={{ fontSize: '11px' }}>
                Se mandarán automáticamente los diplomas a los estudiantes con Check - In.
              </p>
              <button className="btn btn-light rounded-pill w-100 btn-sm fw-semibold text-primary">
                Enviar Diplomas
              </button>
            </div>
          </div>
        </div>
      </div>

      <EditarEventoModal />
    </DashboardLayout>
  )
}

export default AdminEventDetail
