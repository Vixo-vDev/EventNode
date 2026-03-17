import { useState, useEffect } from 'react'
import { Link, useParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import { eventService } from '../../services/eventService'
import { precheckinService } from '../../services/precheckinService'
import eventDetailImg from '../../assets/events/event_detail.png'

function StudentEventDetail({ user }) {
  const { id } = useParams()
  const [evento, setEvento] = useState(null)
  const [loading, setLoading] = useState(true)
  const [enrolled, setEnrolled] = useState(false)
  const [enrolling, setEnrolling] = useState(false)
  const [inscritos, setInscritos] = useState(0)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await eventService.getEvento(id)
        setEvento(data)
        setInscritos(data.inscritos || 0)

        // Check if already enrolled
        if (user?.id) {
          try {
            const misEventos = await precheckinService.listarMisEventos(user.id)
            const isEnrolled = misEventos.some(e => e.idEvento === parseInt(id) && e.inscripcionEstado === 'ACTIVO')
            setEnrolled(isEnrolled)
          } catch { /* not enrolled */ }
        }
      } catch {
        setEvento(null)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [id, user])

  const handleInscribirse = async () => {
    if (!user?.id) return toast.error('Debes iniciar sesión')
    setEnrolling(true)
    try {
      await precheckinService.inscribirse(user.id, parseInt(id))
      toast.success('¡Te has inscrito exitosamente!')
      setEnrolled(true)
      setInscritos(prev => prev + 1)
    } catch (err) {
      toast.error(err.message)
    } finally {
      setEnrolling(false)
    }
  }

  const handleCancelar = async () => {
    if (!user?.id) return
    setEnrolling(true)
    try {
      await precheckinService.cancelarInscripcion(user.id, parseInt(id))
      toast.info('Inscripción cancelada')
      setEnrolled(false)
      setInscritos(prev => prev - 1)
    } catch (err) {
      toast.error(err.message)
    } finally {
      setEnrolling(false)
    }
  }

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      </div>
    )
  }

  if (!evento) {
    return (
      <div className="text-center py-5">
        <h5>Evento no encontrado</h5>
        <Link to="/estudiante/eventos" className="btn btn-primary btn-sm rounded-pill px-4 mt-2">
          Volver a Eventos
        </Link>
      </div>
    )
  }

  const bannerSrc = evento.banner && evento.banner.startsWith('data:image/') ? evento.banner : eventDetailImg
  const capacityPercent = evento.capacidadMaxima > 0 ? Math.round((inscritos / evento.capacidadMaxima) * 100) : 0

  return (
    <div>
      <div className="d-flex align-items-center gap-2 mb-4">
        <Link to="/estudiante/eventos" className="text-secondary text-decoration-none small d-flex align-items-center gap-1">
          <i className="bi bi-arrow-left"></i>
          Regresar
        </Link>
        <span className="text-secondary small">|</span>
        <h5 className="fw-bold mb-0">Detalles del Evento</h5>
      </div>

      <div className="rounded-4 overflow-hidden mb-4 position-relative shadow" style={{ maxHeight: '300px' }}>
        <img src={bannerSrc} alt={evento.nombre} className="w-100" style={{ objectFit: 'cover', height: '300px' }} />
        <div className="position-absolute bottom-0 start-0 p-4 w-100" style={{ background: 'linear-gradient(transparent, rgba(0,0,0,0.7))' }}>
          <span className="badge bg-primary rounded-pill mb-2">{evento.categoriaNombre || 'GENERAL'}</span>
          <h2 className="fw-bold text-white mb-0">{evento.nombre}</h2>
        </div>
      </div>

      <div className="row g-4">
        <div className="col-12 col-lg-8">
          <div className="card border-0 shadow-sm rounded-4 mb-4">
            <div className="card-body p-4">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-info-circle text-primary me-2"></i>
                Acerca de este evento
              </h6>
              <p className="text-secondary small mb-0">{evento.descripcion}</p>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-4">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4">
              <h6 className="text-uppercase text-secondary small fw-bold mb-3">Detalles del Horario</h6>

              <div className="d-flex align-items-start gap-3 mb-3">
                <div className="rounded-circle bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0" style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-calendar3 text-primary small"></i>
                </div>
                <div>
                  <div className="text-secondary small">Fecha</div>
                  <div className="fw-semibold small">
                    {new Date(evento.fechaInicio).toLocaleDateString('es-MX', { day: 'numeric', month: 'long', year: 'numeric' })}
                  </div>
                </div>
              </div>

              <div className="d-flex align-items-start gap-3 mb-3">
                <div className="rounded-circle bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0" style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-clock text-primary small"></i>
                </div>
                <div>
                  <div className="text-secondary small">Horario</div>
                  <div className="fw-semibold small">
                    {new Date(evento.fechaInicio).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' })} - {new Date(evento.fechaFin).toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit' })}
                  </div>
                </div>
              </div>

              <div className="d-flex align-items-start gap-3 mb-3">
                <div className="rounded-circle bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0" style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-geo-alt text-primary small"></i>
                </div>
                <div>
                  <div className="text-secondary small">Ubicación</div>
                  <div className="fw-semibold small">{evento.ubicacion}</div>
                </div>
              </div>

              <div className="d-flex align-items-start gap-3 mb-4">
                <div className="rounded-circle bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0" style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-people text-primary small"></i>
                </div>
                <div className="flex-grow-1">
                  <div className="text-secondary small">Capacidad</div>
                  <div className="fw-semibold small">{inscritos} / {evento.capacidadMaxima}</div>
                  <div className="progress mt-1" style={{ height: '4px' }}>
                    <div className={`progress-bar ${capacityPercent >= 100 ? 'bg-danger' : 'bg-primary'}`} style={{ width: `${Math.min(capacityPercent, 100)}%` }}></div>
                  </div>
                </div>
              </div>

              {enrolled ? (
                <button className="btn btn-outline-danger w-100 rounded-pill fw-semibold" onClick={handleCancelar} disabled={enrolling}>
                  {enrolling ? 'Cancelando...' : 'Cancelar Inscripción'}
                </button>
              ) : evento.estado === 'ACTIVO' ? (
                <button className="btn btn-primary w-100 rounded-pill fw-semibold" onClick={handleInscribirse} disabled={enrolling || capacityPercent >= 100}>
                  {enrolling ? 'Inscribiendo...' : capacityPercent >= 100 ? 'Evento Lleno' : 'Inscribirme'}
                </button>
              ) : (
                <button className="btn btn-secondary w-100 rounded-pill fw-semibold" disabled>
                  Evento {evento.estado}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default StudentEventDetail
