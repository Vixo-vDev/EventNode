import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'
import EventCard from '../../components/EventCard'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

function StudentHome() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
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
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventAi}
            title="Congreso Internacional de Inteligencia Artificial"
            date="15 Ene 2025 | 9:00 AM"
            location="Auditorio Principal, UTEZ"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventMarketing}
            title="Workshop: Marketing Digitalización y Neuronas"
            date="22 Ene 2025 | 11:00 AM"
            location="Sala de Conferencias B"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventUiux}
            title="Semana del Diseño UI/UX 2025"
            date="29 Ene 2025 | 10:00 AM"
            location="Laboratorio de Diseño"
          />
        </div>
      </div>

      <h5 className="fw-bold mb-3">
        <i className="bi bi-award me-2 text-primary"></i>
        Diploma
      </h5>
      <div className="row g-3">
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventMarketing}
            title="Web Development Summit '25"
            date="05 Feb 2025 | 8:00 AM"
            location="Centro de Convenciones"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventAi}
            title="Seminario Avanzado Big Data"
            date="12 Feb 2025 | 2:00 PM"
            location="Auditorio B, UTEZ"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <EventCard
            image={eventUiux}
            title="Seminario Avanzado Big Data"
            date="19 Feb 2025 | 3:00 PM"
            location="Laboratorio de Cómputo"
          />
        </div>
      </div>
    </DashboardLayout>
  )
}

export default StudentHome
