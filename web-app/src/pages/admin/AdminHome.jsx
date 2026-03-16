import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { eventService } from '../../services/eventService'

function AdminHome() {
  const [eventos, setEventos] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchEventos = async () => {
      try {
        const data = await eventService.getEventos()
        const mapped = data.slice(0, 5).map(e => ({
          id: e.idEvento,
          name: e.nombre,
          date: e.fechaInicio ? new Date(e.fechaInicio).toLocaleDateString('es-MX', { month: 'short', day: '2-digit' }).toUpperCase() + ', ' + new Date(e.fechaInicio).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' }) : '',
          status: e.estado,
          capacityPercent: 0,
          capacityText: '0%',
          statusClass: e.estado === 'ACTIVO' ? 'bg-success text-success' : e.estado === 'CANCELADO' ? 'bg-danger text-danger' : 'bg-secondary text-secondary',
          isCancelled: e.estado === 'CANCELADO',
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

  const totalEventos = eventos.length
  const eventosActivos = eventos.filter(e => e.status === 'ACTIVO').length

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
              </div>
              <div className="text-secondary small mb-1">Asistencia</div>
              <div className="fw-bold fs-2">{totalEventos > 0 ? '—' : '0%'}</div>
              <div className="text-secondary small">Sin datos de asistencia aún</div>
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
                {eventosActivos > 0 && (
                  <span className="badge bg-danger bg-opacity-10 text-danger small fw-semibold">LIVE</span>
                )}
              </div>
              <div className="text-secondary small mb-1">Eventos Activos</div>
              <div className="fw-bold fs-2">{eventosActivos}</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100 border-top border-3 border-primary">
            <div className="card-body p-3">
              <div className="text-uppercase text-secondary small fw-bold mb-2">Diplomas</div>
              <div className="d-flex justify-content-between align-items-end">
                <div>
                  <div className="fw-bold fs-3 mb-0">0</div>
                  <div className="text-secondary small">Emitidos</div>
                </div>
                <div className="text-end">
                  <div className="fw-bold fs-3 mb-0">0</div>
                  <div className="text-secondary small">Pendientes</div>
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

          {loading ? (
            <div className="text-center py-4">
              <div className="spinner-border text-primary spinner-border-sm" role="status">
                <span className="visually-hidden">Cargando...</span>
              </div>
            </div>
          ) : eventos.length > 0 ? (
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
                  {eventos.map(event => (
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
          ) : (
            <div className="text-center py-5 px-3">
              <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '56px', height: '56px' }}>
                <i className="bi bi-calendar-plus text-primary fs-4"></i>
              </div>
              <h6 className="fw-bold mb-1">No hay eventos registrados</h6>
              <p className="text-secondary small mb-2">
                Crea tu primer evento para comenzar a gestionar la plataforma.
              </p>
              <Link to="/admin/eventos" className="btn btn-primary btn-sm rounded-pill px-4">
                Crear Evento
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default AdminHome
