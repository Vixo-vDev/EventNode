import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'
import EventDetailInfo from '../../components/EventDetailInfo'
import OrganizerCard from '../../components/OrganizerCard'
import eventDetailImg from '../../assets/events/event_detail.png'

function StudentEventEnrolled() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
      <div className="d-flex align-items-center gap-2 mb-4">
        <Link
          to="/estudiante/eventos"
          className="text-secondary text-decoration-none small d-flex align-items-center gap-1"
        >
          <i className="bi bi-arrow-left"></i>
          Regresar
        </Link>
        <span className="text-secondary small">|</span>
        <h5 className="fw-bold mb-0">Detalles del Evento</h5>
      </div>

      <div className="rounded-4 overflow-hidden mb-4 position-relative"
        style={{ maxHeight: '300px' }}>
        <img
          src={eventDetailImg}
          alt="Hackaton 2025"
          className="w-100"
          style={{ objectFit: 'cover', height: '300px' }}
        />
        <div className="position-absolute bottom-0 start-0 p-4 w-100"
          style={{ background: 'linear-gradient(transparent, rgba(0,0,0,0.7))' }}>
          <div className="d-flex align-items-center gap-2 mb-2">
            <span className="badge bg-primary rounded-pill">DESARROLLO</span>
            <span className="badge bg-success rounded-pill">
              <i className="bi bi-check-circle me-1"></i>
              YA ESTÁS INSCRITO
            </span>
          </div>
          <h2 className="fw-bold text-white mb-0">Hackaton 2025</h2>
        </div>
      </div>

      <div className="row g-4">
        <div className="col-12 col-lg-8">
          <div className="card border-0 shadow-sm rounded-3 mb-4">
            <div className="card-body p-4">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-info-circle text-primary me-2"></i>
                Acerca de este taller
              </h6>
              <p className="text-secondary small mb-3">
                Lleva tus aplicaciones al siguiente nivel en este taller intensivo de Estelar. Nos
                enfocaremos en dominar Web 5 y técnicas de optimización que separan a
                los desarrolladores junior de los senior.
              </p>
              <h6 className="fw-semibold small mb-2">¿Qué aprenderás?</h6>
              <ul className="text-secondary small mb-0">
                <li>Dominio total de Stellar</li>
              </ul>
            </div>
          </div>

          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-4">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-people text-primary me-2"></i>
                Organizadores
              </h6>
              <div className="d-flex flex-wrap gap-4">
                <OrganizerCard
                  name="Abraham Vega"
                  role="Embajador de Estelar"
                />
                <OrganizerCard
                  name="Diego Vega"
                  role="Estudiante de Utez"
                />
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-4">
          <EventDetailInfo enrolled />
        </div>
      </div>
    </DashboardLayout>
  )
}

export default StudentEventEnrolled
