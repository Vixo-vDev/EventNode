import { Link } from 'react-router-dom'

function EventCard({ image, title, date, location, category, detailUrl }) {
  return (
    <div className="card border-0 shadow-sm h-100 rounded-4 overflow-hidden card-hover">
      <div className="position-relative">
        <img
          src={image}
          className="card-img-top"
          alt={title}
          style={{ height: '150px', objectFit: 'cover' }}
        />
        {category && (
          <span className="badge bg-primary bg-opacity-75 position-absolute top-0 start-0 m-2 rounded-pill px-3 small"
            style={{ backdropFilter: 'blur(4px)' }}>
            {category}
          </span>
        )}
      </div>
      <div className="card-body p-3">
        <h6 className="card-title fw-bold mb-2">{title}</h6>
        <div className="d-flex align-items-center gap-2 text-secondary small mb-1">
          <i className="bi bi-calendar3 text-primary" style={{ fontSize: '12px' }}></i>
          <span>{date}</span>
        </div>
        <div className="d-flex align-items-center gap-2 text-secondary small">
          <i className="bi bi-geo-alt text-primary" style={{ fontSize: '12px' }}></i>
          <span>{location}</span>
        </div>
      </div>
      <div className="card-footer bg-white border-0 p-3 pt-0">
        <Link
          to={detailUrl || '/estudiante/evento/1'}
          className="btn btn-outline-primary btn-sm rounded-pill w-100 fw-semibold"
        >
          Ver Detalles
        </Link>
      </div>
    </div>
  )
}

export default EventCard

