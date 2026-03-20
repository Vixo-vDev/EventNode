import { useState, useEffect } from 'react'
import { eventService } from '../../services/eventService'
import { asistenciaService } from '../../services/asistenciaService'
import { precheckinService } from '../../services/precheckinService'
import { diplomaService } from '../../services/diplomaService'
import { userService } from '../../services/userService'

function AdminAnalytics() {
  const [loading, setLoading] = useState(true)
  const [eventos, setEventos] = useState([])
  const [diplomas, setDiplomas] = useState([])
  const [usuarios, setUsuarios] = useState([])
  const [attendanceData, setAttendanceData] = useState([])
  const [categoryData, setCategoryData] = useState([])

  useEffect(() => {
    fetchAllData()
  }, [])

  const fetchAllData = async () => {
    try {
      const [eventosRes, diplomasRes, usuariosRes] = await Promise.all([
        eventService.getEventos().catch(() => []),
        diplomaService.listarDiplomas().catch(() => []),
        userService.getUsuarios().catch(() => [])
      ])

      setEventos(eventosRes)
      setDiplomas(diplomasRes)
      setUsuarios(usuariosRes)

      // Fetch attendance per event
      const attData = await Promise.all(
        eventosRes.map(async (ev) => {
          const [asistencias, inscritos] = await Promise.all([
            asistenciaService.contarAsistencias(ev.idEvento).catch(() => 0),
            precheckinService.contarInscritos(ev.idEvento).catch(() => 0)
          ])
          return {
            id: ev.idEvento,
            nombre: ev.nombre,
            estado: ev.estado,
            categoria: ev.nombreCategoria || 'Sin categoría',
            asistencias,
            inscritos,
            capacidad: ev.capacidadMaxima || 0,
            rate: inscritos > 0 ? Math.round((asistencias / inscritos) * 100) : 0
          }
        })
      )
      setAttendanceData(attData)

      // Group by category
      const catMap = {}
      eventosRes.forEach(ev => {
        const cat = ev.nombreCategoria || 'Sin categoría'
        if (!catMap[cat]) catMap[cat] = { total: 0, activos: 0 }
        catMap[cat].total++
        if (ev.estado === 'ACTIVO') catMap[cat].activos++
      })
      setCategoryData(Object.entries(catMap).map(([name, data]) => ({ name, ...data })))

    } catch (err) {
      console.error('Error fetching analytics:', err)
    } finally {
      setLoading(false)
    }
  }

  // Summary stats
  const totalEventos = eventos.length
  const eventosActivos = eventos.filter(e => e.estado === 'ACTIVO').length
  const eventosCancelados = eventos.filter(e => e.estado === 'CANCELADO').length
  const eventosTerminados = eventos.filter(e => e.estado === 'TERMINADO').length

  const totalDiplomas = diplomas.length
  const diplomasEmitidos = diplomas.reduce((sum, d) => sum + (d.totalEmitidos || 0), 0)
  const diplomasPendientes = diplomas.reduce((sum, d) => sum + (d.totalPendientes || 0), 0)

  const totalUsuarios = usuarios.length
  const estudiantes = usuarios.filter(u => u.rol === 'ESTUDIANTE' || u.rol === 'STUDENT')
  const admins = usuarios.filter(u => u.rol === 'ADMINISTRADOR' || u.rol === 'ADMIN' || u.rol === 'SUPERADMIN')
  const masculino = usuarios.filter(u => u.sexo === 'M').length
  const femenino = usuarios.filter(u => u.sexo === 'F').length

  const totalAsistencias = attendanceData.reduce((s, e) => s + e.asistencias, 0)
  const totalInscritos = attendanceData.reduce((s, e) => s + e.inscritos, 0)
  const overallRate = totalInscritos > 0 ? Math.round((totalAsistencias / totalInscritos) * 100) : 0

  // Top events by attendance
  const topEvents = [...attendanceData].sort((a, b) => b.asistencias - a.asistencias).slice(0, 8)
  const maxAttendance = Math.max(...topEvents.map(e => e.asistencias), 1)

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <div className="text-center">
          <div className="spinner-border text-primary mb-3" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
          <p className="text-secondary">Cargando analíticas...</p>
        </div>
      </div>
    )
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold mb-1">Analíticas</h2>
          <p className="text-secondary mb-0">Resumen general de la plataforma</p>
        </div>
        <button className="btn btn-outline-primary btn-sm rounded-pill px-3" onClick={fetchAllData}>
          <i className="bi bi-arrow-clockwise me-1"></i>Actualizar
        </button>
      </div>

      {/* Summary Stat Cards */}
      <div className="row g-3 mb-4">
        <div className="col-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <div className="rounded-3 bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                <i className="bi bi-calendar-event text-primary"></i>
              </div>
              <div className="text-secondary small mb-1">Total Eventos</div>
              <div className="fw-bold fs-2">{totalEventos}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-success"><i className="bi bi-circle-fill me-1" style={{ fontSize: '6px' }}></i>{eventosActivos} activos</span>
                <span className="text-secondary"><i className="bi bi-circle-fill me-1" style={{ fontSize: '6px' }}></i>{eventosTerminados} terminados</span>
              </div>
            </div>
          </div>
        </div>

        <div className="col-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <div className="rounded-3 bg-success bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                <i className="bi bi-people-fill text-success"></i>
              </div>
              <div className="text-secondary small mb-1">Tasa de Asistencia</div>
              <div className="fw-bold fs-2">{overallRate}%</div>
              <div style={{ fontSize: '12px' }} className="text-secondary">{totalAsistencias} asistencias / {totalInscritos} inscritos</div>
            </div>
          </div>
        </div>

        <div className="col-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <div className="rounded-3 bg-warning bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                <i className="bi bi-award-fill text-warning"></i>
              </div>
              <div className="text-secondary small mb-1">Diplomas</div>
              <div className="fw-bold fs-2">{totalDiplomas}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-success">{diplomasEmitidos} emitidos</span>
                <span className="text-warning">{diplomasPendientes} pendientes</span>
              </div>
            </div>
          </div>
        </div>

        <div className="col-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <div className="rounded-3 bg-info bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                <i className="bi bi-person-fill text-info"></i>
              </div>
              <div className="text-secondary small mb-1">Usuarios</div>
              <div className="fw-bold fs-2">{totalUsuarios}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-primary">{estudiantes.length} estudiantes</span>
                <span className="text-secondary">{admins.length} admins</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="row g-3 mb-4">
        {/* Attendance Chart */}
        <div className="col-12 col-lg-8">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-bar-chart-fill text-primary me-2"></i>
                Asistencia por Evento
              </h6>
              {topEvents.length > 0 ? (
                <div>
                  {topEvents.map((ev, i) => (
                    <div key={ev.id} className="mb-3">
                      <div className="d-flex justify-content-between align-items-center mb-1">
                        <span className="small text-truncate me-2" style={{ maxWidth: '60%' }} title={ev.nombre}>{ev.nombre}</span>
                        <div className="d-flex align-items-center gap-2">
                          <span className="small fw-semibold">{ev.asistencias}</span>
                          <span className="text-secondary" style={{ fontSize: '11px' }}>/ {ev.inscritos} inscritos</span>
                        </div>
                      </div>
                      <div className="progress" style={{ height: '8px' }}>
                        <div
                          className={`progress-bar ${i === 0 ? 'bg-primary' : i === 1 ? 'bg-success' : i === 2 ? 'bg-info' : 'bg-secondary'}`}
                          style={{ width: `${(ev.asistencias / maxAttendance) * 100}%`, transition: 'width 0.6s ease' }}
                        ></div>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-bar-chart text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">No hay datos de asistencia disponibles</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Event Status Breakdown */}
        <div className="col-12 col-lg-4">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-pie-chart-fill text-primary me-2"></i>
                Estado de Eventos
              </h6>
              {totalEventos > 0 ? (
                <div>
                  {/* Visual ring */}
                  <div className="d-flex justify-content-center mb-3">
                    <div className="position-relative" style={{ width: '140px', height: '140px' }}>
                      <svg viewBox="0 0 36 36" style={{ width: '100%', height: '100%', transform: 'rotate(-90deg)' }}>
                        <circle cx="18" cy="18" r="15.915" fill="none" stroke="#e9ecef" strokeWidth="3" />
                        {eventosActivos > 0 && (
                          <circle cx="18" cy="18" r="15.915" fill="none" stroke="#198754" strokeWidth="3"
                            strokeDasharray={`${(eventosActivos / totalEventos) * 100} ${100 - (eventosActivos / totalEventos) * 100}`}
                            strokeDashoffset="0" />
                        )}
                        {eventosTerminados > 0 && (
                          <circle cx="18" cy="18" r="15.915" fill="none" stroke="#6c757d" strokeWidth="3"
                            strokeDasharray={`${(eventosTerminados / totalEventos) * 100} ${100 - (eventosTerminados / totalEventos) * 100}`}
                            strokeDashoffset={`${-(eventosActivos / totalEventos) * 100}`} />
                        )}
                        {eventosCancelados > 0 && (
                          <circle cx="18" cy="18" r="15.915" fill="none" stroke="#dc3545" strokeWidth="3"
                            strokeDasharray={`${(eventosCancelados / totalEventos) * 100} ${100 - (eventosCancelados / totalEventos) * 100}`}
                            strokeDashoffset={`${-((eventosActivos + eventosTerminados) / totalEventos) * 100}`} />
                        )}
                      </svg>
                      <div className="position-absolute top-50 start-50 translate-middle text-center">
                        <div className="fw-bold fs-4">{totalEventos}</div>
                        <div className="text-secondary" style={{ fontSize: '10px' }}>TOTAL</div>
                      </div>
                    </div>
                  </div>
                  {/* Legend */}
                  <div className="d-flex flex-column gap-2">
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-success" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">Activos</span>
                      </div>
                      <span className="fw-semibold small">{eventosActivos}</span>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-secondary" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">Terminados</span>
                      </div>
                      <span className="fw-semibold small">{eventosTerminados}</span>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-danger" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">Cancelados</span>
                      </div>
                      <span className="fw-semibold small">{eventosCancelados}</span>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-pie-chart text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">Sin eventos registrados</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="row g-3 mb-4">
        {/* Categories */}
        <div className="col-12 col-lg-6">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-tags-fill text-primary me-2"></i>
                Eventos por Categoría
              </h6>
              {categoryData.length > 0 ? (
                <div>
                  {categoryData.map((cat, i) => (
                    <div key={i} className="mb-3">
                      <div className="d-flex justify-content-between align-items-center mb-1">
                        <span className="small fw-medium">{cat.name}</span>
                        <span className="badge bg-primary bg-opacity-10 text-primary small">{cat.total} eventos</span>
                      </div>
                      <div className="progress" style={{ height: '6px' }}>
                        <div className="progress-bar bg-primary" style={{ width: `${totalEventos > 0 ? (cat.total / totalEventos) * 100 : 0}%` }}></div>
                      </div>
                      {cat.activos > 0 && (
                        <div className="text-success mt-1" style={{ fontSize: '11px' }}>{cat.activos} activo{cat.activos > 1 ? 's' : ''}</div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-tags text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">Sin categorías</p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Demographics */}
        <div className="col-12 col-lg-6">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <h6 className="fw-bold mb-3">
                <i className="bi bi-people-fill text-primary me-2"></i>
                Demografía de Usuarios
              </h6>
              {totalUsuarios > 0 ? (
                <div>
                  {/* Role distribution */}
                  <div>
                    <div className="small text-secondary mb-2 fw-semibold">Distribución por Rol</div>
                    <div className="row g-2">
                      <div className="col-6">
                        <div className="border rounded-3 p-2 text-center">
                          <div className="fw-bold fs-4 text-primary">{estudiantes.length}</div>
                          <div className="text-secondary" style={{ fontSize: '11px' }}>Estudiantes</div>
                        </div>
                      </div>
                      <div className="col-6">
                        <div className="border rounded-3 p-2 text-center">
                          <div className="fw-bold fs-4 text-warning">{admins.length}</div>
                          <div className="text-secondary" style={{ fontSize: '11px' }}>Administradores</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-person text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">Sin usuarios registrados</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Full event table */}
      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-0">
          <div className="p-3 pb-2">
            <h6 className="fw-bold mb-0">
              <i className="bi bi-table text-primary me-2"></i>
              Detalle de Eventos
            </h6>
          </div>
          {attendanceData.length > 0 ? (
            <div className="table-responsive">
              <table className="table table-hover mb-0 align-middle">
                <thead className="border-top">
                  <tr>
                    <th className="text-uppercase text-secondary small fw-semibold ps-3 py-3" style={{ fontSize: '11px' }}>Evento</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Categoría</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Estado</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Inscritos</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Asistencia</th>
                    <th className="text-uppercase text-secondary small fw-semibold pe-3 py-3" style={{ fontSize: '11px' }}>Tasa</th>
                  </tr>
                </thead>
                <tbody>
                  {attendanceData.map(ev => (
                    <tr key={ev.id}>
                      <td className="ps-3 py-3 fw-semibold small">{ev.nombre}</td>
                      <td className="py-3">
                        <span className="badge bg-primary bg-opacity-10 text-primary rounded-pill px-2">{ev.categoria}</span>
                      </td>
                      <td className="py-3">
                        <span className={`badge bg-opacity-10 rounded-pill px-3 ${
                          ev.estado === 'ACTIVO' ? 'bg-success text-success' :
                          ev.estado === 'CANCELADO' ? 'bg-danger text-danger' : 'bg-secondary text-secondary'
                        }`}>{ev.estado}</span>
                      </td>
                      <td className="py-3 small">{ev.inscritos}</td>
                      <td className="py-3 small">{ev.asistencias}</td>
                      <td className="pe-3 py-3">
                        <div className="d-flex align-items-center gap-2">
                          <div className="progress flex-grow-1" style={{ height: '6px', minWidth: '50px' }}>
                            <div className={`progress-bar ${ev.rate >= 70 ? 'bg-success' : ev.rate >= 40 ? 'bg-warning' : 'bg-danger'}`}
                              style={{ width: `${ev.rate}%` }}></div>
                          </div>
                          <span className="small fw-semibold" style={{ minWidth: '35px' }}>{ev.rate}%</span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="text-center py-5">
              <i className="bi bi-inbox text-secondary fs-1 d-block mb-2"></i>
              <p className="text-secondary small mb-0">No hay eventos para mostrar</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default AdminAnalytics
