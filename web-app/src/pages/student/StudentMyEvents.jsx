import { Link } from 'react-router-dom'
import CategoryFilter from '../../components/CategoryFilter'
import EventCard from '../../components/EventCard'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

function StudentMyEvents() {
  const mockEvents = [
    { id: 1, image: eventAi, title: "Congreso Internacional de Inteligencia Artificial", date: "15 Oct 2023 • 09:00 AM", location: "Auditorio", category: "DESARROLLO" },
    { id: 2, image: eventMarketing, title: "Workshop: Marketing Digital para Startups", date: "22 Oct 2023 • 16:00 PM", location: "Auditorio", category: "MARKETING" },
    { id: 3, image: eventUiux, title: "Semana del Diseño UI/UX 2023", date: "28 Oct 2023 • 10:00 AM", location: "Auditorio", category: "DESARROLLO" }
  ];

  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-3 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Mis Eventos</h2>
          <p className="text-secondary small mb-0">
            Gestiona y revisa los eventos en los que estás inscrito.
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
            className="nav-link text-secondary small"
          >
            Explorar los Eventos
          </Link>
        </li>
        <li className="nav-item">
          <Link
            to="/estudiante/mis-eventos"
            className="nav-link active fw-semibold small"
          >
            Mis Eventos
          </Link>
        </li>
      </ul>

      <CategoryFilter />

      <div className="row g-3 mb-4">
        {mockEvents.map(event => (
          <div className="col-12 col-md-6 col-lg-4" key={event.id}>
            <EventCard
              image={event.image}
              title={event.title}
              date={event.date}
              location={event.location}
              category={event.category}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default StudentMyEvents
