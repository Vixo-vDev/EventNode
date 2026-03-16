import { Link } from 'react-router-dom'

function StudentMyEvents() {
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
          <Link to="/estudiante/eventos" className="nav-link text-secondary small">
            Explorar los Eventos
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/estudiante/mis-eventos" className="nav-link active fw-semibold small">
            Mis Eventos
          </Link>
        </li>
      </ul>

      <div className="card border-0 shadow-sm rounded-3">
        <div className="card-body text-center py-5">
          <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
            <i className="bi bi-bookmark-star text-primary fs-3"></i>
          </div>
          <h6 className="fw-bold mb-1">No estás inscrito en ningún evento</h6>
          <p className="text-secondary small mb-2">
            Explora los eventos disponibles e inscríbete para verlos aquí.
          </p>
          <Link to="/estudiante/eventos" className="btn btn-primary btn-sm rounded-pill px-4">
            Explorar Eventos
          </Link>
        </div>
      </div>
    </div>
  )
}

export default StudentMyEvents
