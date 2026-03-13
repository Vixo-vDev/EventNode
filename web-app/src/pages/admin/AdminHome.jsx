import { Link } from 'react-router-dom'

function AdminHome() {
  const mockEvents = [
    { id: 1, name: 'Design Thinking Workshop', date: 'FEB 24, 09:00 AM', status: 'ACTIVO', capacityText: '75%', capacityPercent: 75, statusClass: 'bg-success text-success' },
    { id: 2, name: 'Blockchain Governance', date: 'FEB 25, 11:30 AM', status: 'ACTIVO', capacityText: '45%', capacityPercent: 45, statusClass: 'bg-success text-success' },
    { id: 3, name: 'Marketing Masterclass', date: 'FEB 26, 02:00 PM', status: 'CANCELADO', capacityText: '0%', capacityPercent: 0, statusClass: 'bg-danger text-danger', isCancelled: true }
  ];

  return (
    <div>
      <h2 className="fw-bold mb-4">Panel</h2>

      <div className="row g-3 mb-4">
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex justify-content-between align-items-start mb-2">
                <div className="rounded-2 bg-primary bg-opacity-10 d-flex align-items-center justify-content-center"
                  style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-people-fill text-primary"></i>
                </div>
                <span className="badge bg-success bg-opacity-10 text-success small">+5%</span>
              </div>
              <div className="text-secondary small mb-1">Ocurrencias</div>
              <div className="fw-bold fs-2">82%</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex justify-content-between align-items-start mb-2">
                <div className="rounded-2 bg-primary bg-opacity-10 d-flex align-items-center justify-content-center"
                  style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-calendar-check text-primary"></i>
                </div>
                <span className="badge bg-danger bg-opacity-10 text-danger small fw-semibold">LIVE</span>
              </div>
              <div className="text-secondary small mb-1">Total Active Events</div>
              <div className="fw-bold fs-2">24</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100 border-top border-3 border-primary">
            <div className="card-body p-3">
              <div className="text-uppercase text-secondary small fw-bold mb-2">Diplomas</div>
              <div className="d-flex justify-content-between align-items-end">
                <div>
                  <div className="fw-bold fs-3 mb-0">1,240</div>
                  <div className="text-secondary small">Issued</div>
                </div>
                <div className="text-end">
                  <div className="fw-bold fs-3 mb-0">42</div>
                  <div className="text-secondary small">Pending</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-3">
        <div className="card-body p-0">
          <div className="d-flex justify-content-between align-items-center p-3 pb-2">
            <h5 className="fw-bold mb-0">Detalles de Eventos</h5>
            <Link to="/admin/eventos" className="text-primary text-decoration-none small fw-semibold">
              Ver Todos
            </Link>
          </div>

          <div className="table-responsive">
            <table className="table table-hover mb-0 align-middle">
              <thead className="border-top">
                <tr>
                  <th className="text-uppercase text-secondary small fw-semibold ps-3 py-3" style={{ fontSize: '11px' }}>Nombre del Evento</th>
                  <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Fecha/Tiempo</th>
                  <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Estado</th>
                  <th className="text-uppercase text-secondary small fw-semibold pe-3 py-3" style={{ fontSize: '11px' }}>Capacidad</th>
                </tr>
              </thead>
              <tbody>
                {mockEvents.map(event => (
                  <tr key={event.id}>
                    <td className={`ps-3 py-3 fw-semibold small ${event.isCancelled ? 'text-primary' : ''}`}>{event.name}</td>
                    <td className="py-3 text-secondary small">{event.date}</td>
                    <td className="py-3">
                      <span className={`badge bg-opacity-10 rounded-pill px-3 ${event.statusClass}`}>{event.status}</span>
                    </td>
                    <td className="pe-3 py-3">
                      {event.isCancelled ? (
                         <span className="text-secondary small">{event.capacityText}</span>
                      ) : (
                        <div className="d-flex align-items-center gap-2">
                          <div className="progress flex-grow-1" style={{ height: '6px' }}>
                            <div className="progress-bar bg-primary" style={{ width: `${event.capacityPercent}%` }}></div>
                          </div>
                          <span className="text-secondary small">{event.capacityText}</span>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminHome
