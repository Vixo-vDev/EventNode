import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'
import eventConcert from '../../assets/events/event_concert.png'
import eventTechSummit from '../../assets/events/event_tech_summit.png'
import eventGala from '../../assets/events/event_gala.png'
import eventFestival from '../../assets/events/event_festival.png'
import eventWorkshop from '../../assets/events/event_workshop.png'
import CrearEventoModal from '../../components/modals/CrearEventoModal'

function AdminEventCard({ image, title, location, date, status, capacityCurrent, capacityMax, isFull, isFinished }) {
  const isActive = status === 'ACTIVO'
  const isCancelled = status === 'CANCELADO'
  const isTerminado = status === 'TERMINADO'
  const percent = capacityMax > 0 ? Math.round((capacityCurrent / capacityMax) * 100) : 0

  return (
    <div
      className="card border-0 shadow-sm rounded-3 h-100 overflow-hidden"
      style={isFull ? { border: '2px solid #dc3545' } : {}}
    >
      <div className="position-relative">
        <img
          src={image}
          className="card-img-top"
          alt={title}
          style={{
            height: '160px',
            objectFit: 'cover',
            filter: isTerminado || isCancelled ? 'grayscale(100%)' : 'none'
          }}
        />
        <span className={`badge position-absolute top-0 start-0 m-2 rounded-pill px-3 ${
          isActive ? 'bg-primary text-white' :
          isTerminado ? 'bg-secondary text-white' :
          'bg-danger text-white'
        }`}>
          {status}
        </span>
      </div>
      <div className="card-body p-3">
        <h6 className="fw-bold mb-2">{title}</h6>
        <div className="d-flex align-items-center gap-1 text-secondary small mb-3">
          <i className="bi bi-geo-alt"></i>
          <span>{location}</span>
          <span className="mx-1">•</span>
          <span>{date}</span>
        </div>

        <div className="d-flex justify-content-between align-items-center mb-2">
          <span className={`text-uppercase small fw-bold ${isFull ? 'text-danger' : isTerminado ? 'text-secondary' : 'text-dark'}`}>
            {isTerminado ? 'Finalizado' : 'Capacidad'}
          </span>
          <span className={`small fw-semibold ${isFull ? 'text-danger' : ''}`}>
            {capacityCurrent} / {capacityMax}
          </span>
        </div>
        <div className="progress" style={{ height: '4px' }}>
          <div
            className={`progress-bar ${isFull ? 'bg-danger' : isTerminado ? 'bg-secondary' : 'bg-primary'}`}
            style={{ width: `${percent}%` }}
          ></div>
        </div>
      </div>
      <div className="card-footer bg-white border-top d-flex justify-content-start gap-3 px-3 py-2">
        {isActive && (
          <>
            <button className="btn btn-link text-secondary p-0" title="Editar">
              <i className="bi bi-pencil"></i>
            </button>
            <button className="btn btn-link text-secondary p-0" title="Duplicar">
              <i className="bi bi-files"></i>
            </button>
            <button className="btn btn-link text-secondary p-0" title="Vista previa">
              <i className="bi bi-eye"></i>
            </button>
            <button className="btn btn-link text-secondary p-0" title="QR">
              <i className="bi bi-qr-code"></i>
            </button>
          </>
        )}
        <button className="btn btn-link text-secondary p-0" title="Eliminar">
          <i className="bi bi-trash"></i>
        </button>
      </div>
    </div>
  )
}

function GestionEventos() {
  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Eventos</h2>
          <p className="text-secondary small mb-0">
            Supervise y administre todos sus eventos activos en tiempo real
          </p>
        </div>
        <button
          className="btn btn-primary rounded-pill d-flex align-items-center gap-2 flex-shrink-0"
          data-bs-toggle="modal"
          data-bs-target="#crearEventoModal"
        >
          <i className="bi bi-plus-circle"></i>
          Nuevo Evento
        </button>
      </div>

      <div className="input-group mb-3" style={{ maxWidth: '400px' }}>
        <span className="input-group-text bg-white border-end-0">
          <i className="bi bi-search text-secondary"></i>
        </span>
        <input
          type="text"
          className="form-control border-start-0"
          placeholder="Buscar eventos por nombre..."
        />
      </div>

      <div className="d-flex gap-2 mb-4 flex-wrap">
        <button className="btn btn-primary btn-sm rounded-pill px-3">Todos</button>
        <button className="btn btn-outline-secondary btn-sm rounded-pill px-3">Activo</button>
        <button className="btn btn-outline-secondary btn-sm rounded-pill px-3">Terminado</button>
        <button className="btn btn-outline-secondary btn-sm rounded-pill px-3">Cancelado</button>
      </div>

      <div className="row g-3">
        <div className="col-12 col-md-6 col-lg-4">
          <AdminEventCard
            image={eventConcert}
            title="Concierto Rock en Vivo"
            location="Estadio Nacional, Lima"
            date="25 Oct"
            status="ACTIVO"
            capacityCurrent={65}
            capacityMax={100}
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <AdminEventCard
            image={eventTechSummit}
            title="Tech Summit 2023"
            location="Centro de Convenciones"
            date="12 Nov"
            status="ACTIVO"
            capacityCurrent={500}
            capacityMax={500}
            isFull
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <AdminEventCard
            image={eventGala}
            title="Gala de Aniversario"
            location="Hotel Marriott"
            date="Mañana"
            status="ACTIVO"
            capacityCurrent={120}
            capacityMax={150}
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <AdminEventCard
            image={eventFestival}
            title="Festival Gastronómico"
            location="Parque de la Exposición"
            date="15 Sep"
            status="TERMINADO"
            capacityCurrent={2000}
            capacityMax={2000}
            isFinished
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <AdminEventCard
            image={eventWorkshop}
            title="Taller de Innovación"
            location="Auditorio Central"
            date="30 Oct"
            status="CANCELADO"
            capacityCurrent={0}
            capacityMax={50}
          />
        </div>
        <div className="col-12 col-md-6 col-lg-4">
          <div
            className="card h-100 rounded-3 d-flex align-items-center justify-content-center text-center p-4"
            style={{ border: '2px dashed #dee2e6', cursor: 'pointer' }}
            data-bs-toggle="modal"
            data-bs-target="#crearEventoModal"
            role="button"
          >
            <div className="rounded-circle bg-light d-flex align-items-center justify-content-center mb-2"
              style={{ width: '48px', height: '48px' }}>
              <i className="bi bi-plus-lg text-secondary fs-4"></i>
            </div>
            <h6 className="fw-bold mb-1 fst-italic">Crear Nuevo Evento</h6>
            <p className="text-secondary small mb-0 fst-italic">
              Configure fechas, ubicación y entradas
            </p>
          </div>
        </div>
      </div>

      <CrearEventoModal />
    </DashboardLayout>
  )
}

export default GestionEventos
