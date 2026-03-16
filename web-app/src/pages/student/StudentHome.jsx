import { useState, useEffect } from 'react'
import { eventService } from '../../services/eventService'
import EventCard from '../../components/EventCard'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

const fallbackImages = [eventAi, eventMarketing, eventUiux]

function StudentHome() {
  const [eventos, setEventos] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchEventos = async () => {
      try {
        const data = await eventService.getEventos()
        const mapped = data.slice(0, 3).map((e, index) => ({
          id: e.idEvento,
          image: e.banner || fallbackImages[index % fallbackImages.length],
          title: e.nombre,
          date: e.fechaInicio ? new Date(e.fechaInicio).toLocaleDateString('es-MX', { day: '2-digit', month: 'short', year: 'numeric' }) + ' | ' + new Date(e.fechaInicio).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }) : '',
          location: e.ubicacion,
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
      <h2 className="fw-bold mb-1">Inicio</h2>
      <p className="text-secondary small mb-4">
        Explora los eventos que tenemos preparados para ti
      </p>

      <div className="d-flex align-items-center gap-2 mb-4">
        <div className="input-group">
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-search text-secondary"></i>
          </span>
          <input
            type="text"
            className="form-control border-start-0"
            placeholder="Buscar eventos..."
          />
        </div>
      </div>

      <h5 className="fw-bold mb-3">
        <i className="bi bi-calendar-event me-2 text-primary"></i>
        Próximos Eventos
      </h5>
      {loading ? (
        <div className="text-center py-3">
          <div className="spinner-border text-primary spinner-border-sm" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
        </div>
      ) : eventos.length > 0 ? (
        <div className="row g-3 mb-5">
          {eventos.map(event => (
            <div className="col-12 col-md-6 col-lg-4" key={event.id}>
              <EventCard image={event.image} title={event.title} date={event.date} location={event.location} />
            </div>
          ))}
        </div>
      ) : (
        <div className="card border-0 shadow-sm rounded-3 mb-5">
          <div className="card-body text-center py-5">
            <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
              <i className="bi bi-calendar-x text-primary fs-3"></i>
            </div>
            <h6 className="fw-bold mb-1">No hay eventos próximos</h6>
            <p className="text-secondary small mb-0">
              Cuando se publiquen nuevos eventos, aparecerán aquí.
            </p>
          </div>
        </div>
      )}

      <h5 className="fw-bold mb-3">
        <i className="bi bi-award me-2 text-primary"></i>
        Diploma
      </h5>
      <div className="card border-0 shadow-sm rounded-3">
        <div className="card-body text-center py-5">
          <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
            <i className="bi bi-award text-primary fs-3"></i>
          </div>
          <h6 className="fw-bold mb-1">Aún no tienes diplomas</h6>
          <p className="text-secondary small mb-0">
            Asiste a eventos para obtener tus diplomas y certificaciones.
          </p>
        </div>
      </div>
    </div>
  )
}

export default StudentHome
