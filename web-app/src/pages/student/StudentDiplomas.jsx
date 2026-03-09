import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'
import DiplomaCard from '../../components/DiplomaCard'
import Pagination from '../../components/Pagination'
import eventAi from '../../assets/events/event_ai.png'
import eventMarketing from '../../assets/events/event_marketing.png'
import eventUiux from '../../assets/events/event_uiux.png'

function StudentDiplomas() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Diplomas</h2>
          <p className="text-secondary small mb-0">
            Aquí puedes encontrar y descargar todos los diplomas de los cursos y
            talleres que has asistido. Tus logros están respaldados David Valenzuela.
          </p>
        </div>
        <div className="input-group flex-shrink-0" style={{ maxWidth: '240px' }}>
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-search text-secondary"></i>
          </span>
          <input
            type="text"
            className="form-control border-start-0"
            placeholder="Buscar diploma..."
          />
        </div>
      </div>

      <div className="row g-3 mb-4">
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventAi}
            title="Congreso Internacional de Inteligencia Artificial"
            date="15 Oct 2023"
            category="DESARROLLO"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventMarketing}
            title="Workshop: Marketing Digital para Startups"
            date="22 Oct 2023"
            category="MARKETING"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventUiux}
            title="Semana del Diseño UI/UX 2023"
            date="28 Oct 2023"
            category="DESARROLLO"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventAi}
            title="Congreso Internacional de Inteligencia Artificial"
            date="15 Oct 2023"
            category="DESARROLLO"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventMarketing}
            title="Workshop: Marketing Digital para Startups"
            date="22 Oct 2023"
            category="MARKETING"
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <DiplomaCard
            image={eventUiux}
            title="Semana del Diseño UI/UX 2023"
            date="28 Oct 2023"
            category="DESARROLLO"
          />
        </div>
      </div>

      <Pagination />
    </DashboardLayout>
  )
}

export default StudentDiplomas
