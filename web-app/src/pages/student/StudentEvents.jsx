import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { eventService } from '../../services/eventService'
import EventCard from '../../components/EventCard'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

const fallbackImages = [eventAi, eventMarketing, eventUiux]

function StudentEvents() {
  const [eventos, setEventos] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchEventos = async () => {
      try {
        const data = await eventService.getEventos()
        const mapped = data.map((e, index) => ({
          id: e.idEvento,
          image: e.banner || fallbackImages[index % fallbackImages.length],
          title: e.nombre,
          date: e.fechaInicio ? new Date(e.fechaInicio).toLocaleDateString('es-MX', { day: '2-digit', month: 'short', year: 'numeric' }) + ' • ' + new Date(e.fechaInicio).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }) : '',
          location: e.ubicacion,
          category: e.categoriaNombre || 'GENERAL',
        }))
        setEventos(mapped)
      } catch {
        setEventos([])
      } finally {
        setLoading(false)
      }
    }
    fetchEventos()
  }, [])

  return (
    <div>
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
          <Link to="/estudiante/eventos" className="nav-link active fw-semibold small">
            Explorar los Eventos
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/estudiante/mis-eventos" className="nav-link text-secondary small">
            Mis Eventos
          </Link>
        </li>
      </ul>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
        </div>
      ) : eventos.length > 0 ? (
        <div className="row g-3 mb-4">
          {eventos.map(event => (
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
      ) : (
        <div className="card border-0 shadow-sm rounded-3">
          <div className="card-body text-center py-5">
            <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
              <i className="bi bi-calendar-plus text-primary fs-3"></i>
            </div>
            <h6 className="fw-bold mb-1">No hay eventos disponibles</h6>
            <p className="text-secondary small mb-0">
              Aún no se han publicado eventos. Vuelve pronto para descubrir nuevas actividades.
            </p>
          </div>
        </div>
      )}
    </div>
  )
}

export default StudentEvents
