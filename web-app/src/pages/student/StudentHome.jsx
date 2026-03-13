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
        setEventos([
          { id: 1, image: eventAi, title: "Congreso Internacional de Inteligencia Artificial", date: "15 Ene 2025 | 9:00 AM", location: "Auditorio Principal, UTEZ" },
          { id: 2, image: eventMarketing, title: "Workshop: Marketing Digitalización y Neuronas", date: "22 Ene 2025 | 11:00 AM", location: "Sala de Conferencias B" },
          { id: 3, image: eventUiux, title: "Semana del Diseño UI/UX 2025", date: "29 Ene 2025 | 10:00 AM", location: "Laboratorio de Diseño" }
        ])
      } finally {
        setLoading(false)
      }
    }
    fetchEventos()
  }, [])

  const mockDiplomas = [
    { id: 4, image: eventMarketing, title: "Web Development Summit '25", date: "05 Feb 2025 | 8:00 AM", location: "Centro de Convenciones" },
    { id: 5, image: eventAi, title: "Seminario Avanzado Big Data", date: "12 Feb 2025 | 2:00 PM", location: "Auditorio B, UTEZ" },
    { id: 6, image: eventUiux, title: "Seminario Avanzado Big Data", date: "19 Feb 2025 | 3:00 PM", location: "Laboratorio de Cómputo" }
  ]

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
      <div className="row g-3 mb-5">
        {loading ? (
          <div className="col-12 text-center py-3">
            <div className="spinner-border text-primary spinner-border-sm" role="status">
              <span className="visually-hidden">Cargando...</span>
            </div>
          </div>
        ) : eventos.map(event => (
          <div className="col-12 col-md-6 col-lg-4" key={event.id}>
            <EventCard image={event.image} title={event.title} date={event.date} location={event.location} />
          </div>
        ))}
      </div>

      <h5 className="fw-bold mb-3">
        <i className="bi bi-award me-2 text-primary"></i>
        Diploma
      </h5>
      <div className="row g-3">
        {mockDiplomas.map(diploma => (
          <div className="col-12 col-md-6 col-lg-4" key={diploma.id}>
             <EventCard image={diploma.image} title={diploma.title} date={diploma.date} location={diploma.location} />
          </div>
        ))}
      </div>
    </div>
  )
}

export default StudentHome
