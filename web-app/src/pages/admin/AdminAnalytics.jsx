import { useState, useEffect } from 'react'
import { eventService } from '../../services/eventService'
import { asistenciaService } from '../../services/asistenciaService'
import { precheckinService } from '../../services/precheckinService'
import { diplomaService } from '../../services/diplomaService'
import { userService } from '../../services/userService'
import { useTranslation } from '../../i18n/I18nContext'

function AdminAnalytics() {
  const { t } = useTranslation()
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
    } finally {
      setLoading(false)
    }
  }

  // Summary stats
  const totalEventos = eventos.length
  const eventosActivos = eventos.filter(e => e.estado === 'ACTIVO').length
  const eventosCancelados = eventos.filter(e => e.estado === 'CANCELADO').length
  const eventosTerminados = eventos.filter(e => e.estado === 'FINALIZADO').length

  const totalDiplomas = diplomas.length
  const diplomasEmitidos = diplomas.reduce((sum, d) => sum + (d.totalEmitidos || 0), 0)
  const diplomasPendientes = diplomas.reduce((sum, d) => sum + (d.totalPendientes || 0), 0)

  const totalUsuarios = usuarios.length
  const estudiantes = usuarios.filter(u => u.rol === 'ALUMNO' || u.rol === 'ESTUDIANTE' || u.rol === 'STUDENT')
  const admins = usuarios.filter(u => u.rol === 'ADMINISTRADOR' || u.rol === 'SUPERADMIN')

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
            <span className="visually-hidden">{t('common.loading')}</span>
          </div>
          <p className="text-secondary">{t('analytics.loadingAnalytics')}</p>
        </div>
      </div>
    )
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold mb-1">{t('analytics.title')}</h2>
          <p className="text-secondary mb-0">{t('analytics.subtitle')}</p>
        </div>
        <button className="btn btn-outline-primary btn-sm rounded-pill px-3" onClick={fetchAllData}>
          <i className="bi bi-arrow-clockwise me-1"></i>{t('analytics.refresh')}
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
              <div className="text-secondary small mb-1">{t('analytics.totalEvents')}</div>
              <div className="fw-bold fs-2">{totalEventos}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-success"><i className="bi bi-circle-fill me-1" style={{ fontSize: '6px' }}></i>{eventosActivos} {t('analytics.activeEvents')}</span>
                <span className="text-secondary"><i className="bi bi-circle-fill me-1" style={{ fontSize: '6px' }}></i>{eventosTerminados} {t('analytics.finishedEvents')}</span>
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
              <div className="text-secondary small mb-1">{t('analytics.attendanceRate')}</div>
              <div className="fw-bold fs-2">{overallRate}%</div>
              <div style={{ fontSize: '12px' }} className="text-secondary">{t('analytics.attendanceFraction', { total: totalInscritos }).replace('asistencias', totalAsistencias)}</div>
            </div>
          </div>
        </div>

        <div className="col-6 col-lg-3">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-3">
              <div className="rounded-3 bg-warning bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                <i className="bi bi-award-fill text-warning"></i>
              </div>
              <div className="text-secondary small mb-1">{t('analytics.diplomasStat')}</div>
              <div className="fw-bold fs-2">{totalDiplomas}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-success">{diplomasEmitidos} {t('analytics.emitted')}</span>
                <span className="text-warning">{diplomasPendientes} {t('analytics.pendingStat')}</span>
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
              <div className="text-secondary small mb-1">{t('analytics.users')}</div>
              <div className="fw-bold fs-2">{totalUsuarios}</div>
              <div className="d-flex gap-2 mt-1" style={{ fontSize: '12px' }}>
                <span className="text-primary">{estudiantes.length} {t('analytics.studentsStat')}</span>
                <span className="text-secondary">{admins.length} {t('analytics.adminsStat')}</span>
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
                {t('analytics.attendanceByEvent')}
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
                  <p className="text-secondary small mb-0">{t('analytics.noAttendanceData')}</p>
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
                {t('analytics.eventStatus')}
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
                        <div className="text-secondary" style={{ fontSize: '10px' }}>{t('analytics.total')}</div>
                      </div>
                    </div>
                  </div>
                  {/* Legend */}
                  <div className="d-flex flex-column gap-2">
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-success" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">{t('analytics.activeLabel')}</span>
                      </div>
                      <span className="fw-semibold small">{eventosActivos}</span>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-secondary" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">{t('analytics.finishedLabel')}</span>
                      </div>
                      <span className="fw-semibold small">{eventosTerminados}</span>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                      <div className="d-flex align-items-center gap-2">
                        <span className="rounded-circle bg-danger" style={{ width: '10px', height: '10px', display: 'inline-block' }}></span>
                        <span className="small">{t('analytics.cancelledLabel')}</span>
                      </div>
                      <span className="fw-semibold small">{eventosCancelados}</span>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-pie-chart text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">{t('analytics.noEventsRegistered')}</p>
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
                {t('analytics.eventsByCategory')}
              </h6>
              {categoryData.length > 0 ? (
                <div>
                  {categoryData.map((cat, i) => (
                    <div key={i} className="mb-3">
                      <div className="d-flex justify-content-between align-items-center mb-1">
                        <span className="small fw-medium">{cat.name}</span>
                        <span className="badge bg-primary bg-opacity-10 text-primary small">{cat.total} {t('analytics.events')}</span>
                      </div>
                      <div className="progress" style={{ height: '6px' }}>
                        <div className="progress-bar bg-primary" style={{ width: `${totalEventos > 0 ? (cat.total / totalEventos) * 100 : 0}%` }}></div>
                      </div>
                      {cat.activos > 0 && (
                        <div className="text-success mt-1" style={{ fontSize: '11px' }}>{cat.activos} {t('analytics.activeCount')}</div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-tags text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">{t('analytics.noEventsToShow')}</p>
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
                {t('analytics.userDemographics')}
              </h6>
              {totalUsuarios > 0 ? (
                <div>
                  {/* Role distribution */}
                  <div>
                    <div className="small text-secondary mb-2 fw-semibold">{t('analytics.roleDistribution')}</div>
                    <div className="row g-2">
                      <div className="col-6">
                        <div className="border rounded-3 p-2 text-center">
                          <div className="fw-bold fs-4 text-primary">{estudiantes.length}</div>
                          <div className="text-secondary" style={{ fontSize: '11px' }}>{t('analytics.studentsLabel')}</div>
                        </div>
                      </div>
                      <div className="col-6">
                        <div className="border rounded-3 p-2 text-center">
                          <div className="fw-bold fs-4 text-warning">{admins.length}</div>
                          <div className="text-secondary" style={{ fontSize: '11px' }}>{t('analytics.administratorsLabel')}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-center py-4">
                  <i className="bi bi-person text-secondary fs-1 d-block mb-2"></i>
                  <p className="text-secondary small mb-0">{t('analytics.noEventsToShow')}</p>
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
              {t('analytics.eventDetails')}
            </h6>
          </div>
          {attendanceData.length > 0 ? (
            <div className="table-responsive">
              <table className="table table-hover mb-0 align-middle">
                <thead className="border-top">
                  <tr>
                    <th className="text-uppercase text-secondary small fw-semibold ps-3 py-3" style={{ fontSize: '11px' }}>{t('analytics.eventCol')}</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>{t('analytics.categoryCol')}</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>{t('events.status')}</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>{t('analytics.enrolledCol')}</th>
                    <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>{t('analytics.attendanceCol')}</th>
                    <th className="text-uppercase text-secondary small fw-semibold pe-3 py-3" style={{ fontSize: '11px' }}>{t('analytics.rateCol')}</th>
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
              <p className="text-secondary small mb-0">{t('analytics.noEventsToShow')}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default AdminAnalytics
