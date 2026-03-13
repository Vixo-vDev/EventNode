import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'
import EventBanner from '../../components/EventBanner'
import CategoryFilter from '../../components/CategoryFilter'
import EventCard from '../../components/EventCard'
import Pagination from '../../components/Pagination'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

function StudentEvents() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-3 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Explorar Eventos</h2>
          <p className="text-secondary small mb-0">
            Descubre y únete a los próximos eventos académicos y talleres.
          </p>
        </div>
        <div className="input-group" style={{ maxWidth: '280px' }}>
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-search text-secondary"></i>
          </span>
          <input
            type="text"
            className="form-control border-start-0"
            placeholder="Buscar por nombre o categoría..."
          />
        </div>
      </div>

      <ul className="nav nav-tabs mb-4">
        <li className="nav-item">
          <Link
            to="/estudiante/eventos"
            className="nav-link active fw-semibold small"
          >
            Explorar los Eventos
          </Link>
        </li>
        <li className="nav-item">
          <Link
            to="/estudiante/mis-eventos"
            className="nav-link text-secondary small"
          >
            Mis Eventos
          </Link>
        </li>
      </ul>

      <EventBanner />

      <CategoryFilter />

      <div className="row g-3 mb-4">
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventAi}
            title="Congreso Internacional de Inteligencia Artificial"
            date="10 Oct 2023 • 09:00 AM"
            location="Auditorio"
            category="DESARROLLO"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventMarketing}
            title="Workshop: Marketing Digital para Startups"
            date="22 Oct 2023 • 16:00 PM"
            location="Auditorio"
            category="MARKETING"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventUiux}
            title="Semana del Diseño UI/UX 2023"
            date="28 Oct 2023 • 10:00 AM"
            location="Auditorio"
            category="DESARROLLO"
          />
        </div>
      </div>

      <Pagination />
    </DashboardLayout>
  )
}

export default StudentEvents
