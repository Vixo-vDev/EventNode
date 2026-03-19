import { useState, useEffect } from 'react'
import { Link, useParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import { eventService } from '../../services/eventService'
import { asistenciaService } from '../../services/asistenciaService'

function AdminCheckIn() {
  const { id } = useParams()
  const [evento, setEvento] = useState(null)
  const [students, setStudents] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [matricula, setMatricula] = useState('')
  const [manualLoading, setManualLoading] = useState(false)
  const [updatingIds, setUpdatingIds] = useState(new Set())

  const fetchData = async () => {
    try {
      const [eventoData, asistenciasData] = await Promise.all([
        eventService.getEvento(id),
        asistenciaService.listarAsistencias(id),
      ])
      setEvento(eventoData)
      setStudents(asistenciasData)
    } catch {
      setStudents([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchData() }, [id])

  const handleManualCheckin = async () => {
    if (!matricula.trim()) return toast.error('Ingresa una matrícula')
    setManualLoading(true)
    try {
      await asistenciaService.registrarManual(matricula.trim(), parseInt(id))
      toast.success('Asistencia registrada exitosamente')
      setMatricula('')
      fetchData()
    } catch (err) {
      toast.error(err.message)
    } finally {
      setManualLoading(false)
    }
  }

  const handleToggleEstado = async (student) => {
    const newEstado = student.estado === 'ASISTIDO' ? 'PENDIENTE' : 'ASISTIDO'
    setUpdatingIds(prev => new Set([...prev, student.idAsistencia]))
    try {
      await asistenciaService.actualizarEstado(student.idAsistencia, newEstado)
      setStudents(prev =>
        prev.map(s =>
          s.idAsistencia === student.idAsistencia
            ? { ...s, estado: newEstado }
            : s
        )
      )
      toast.success(newEstado === 'ASISTIDO' ? 'Marcado como asistido' : 'Marcado como pendiente')
    } catch (err) {
      toast.error(err.message)
    } finally {
      setUpdatingIds(prev => {
        const next = new Set(prev)
        next.delete(student.idAsistencia)
        return next
      })
    }
  }

  const filtered = search
    ? students.filter(s =>
        (s.nombre || '').toLowerCase().includes(search.toLowerCase()) ||
        (s.correo || '').toLowerCase().includes(search.toLowerCase())
      )
    : students

  const asistidosCount = students.filter(s => s.estado === 'ASISTIDO').length
  const pendientesCount = students.filter(s => s.estado === 'PENDIENTE' || !s.estado).length

  return (
    <div>
      <div className="d-flex align-items-center gap-3 mb-4">
        <Link to={`/admin/evento/${id}`} className="btn btn-light rounded-circle shadow-sm d-flex align-items-center justify-content-center p-0" style={{ width: '40px', height: '40px' }}>
          <i className="bi bi-arrow-left text-secondary fs-5"></i>
        </Link>
        <span className="fw-semibold small">{evento?.nombre || 'Cargando...'}</span>
      </div>

      <div className="mb-4">
        <h2 className="fw-bold mb-1">Lista de Asistencia (Check-in)</h2>
        <div className="text-secondary small">
          Gestiona y verifica la asistencia de los alumnos registrados para este evento.
        </div>
      </div>

      {/* Stats */}
      <div className="row g-3 mb-4">
        <div className="col-6 col-md-3">
          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-3 text-center">
              <div className="text-secondary small text-uppercase fw-bold mb-1">Total</div>
              <div className="fw-bold fs-4">{students.length}</div>
            </div>
          </div>
        </div>
        <div className="col-6 col-md-3">
          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-3 text-center">
              <div className="text-success small text-uppercase fw-bold mb-1">Asistidos</div>
              <div className="fw-bold fs-4 text-success">{asistidosCount}</div>
            </div>
          </div>
        </div>
        <div className="col-6 col-md-3">
          <div className="card border-0 shadow-sm rounded-3">
            <div className="card-body p-3 text-center">
              <div className="text-warning small text-uppercase fw-bold mb-1">Pendientes</div>
              <div className="fw-bold fs-4 text-warning">{pendientesCount}</div>
            </div>
          </div>
        </div>
      </div>

      {/* Manual check-in */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-body p-4">
          <h6 className="fw-bold mb-2">Ingreso Manual</h6>
          <div className="d-flex gap-2">
            <input
              type="text"
              className="form-control"
              placeholder="Matrícula del estudiante..."
              value={matricula}
              onChange={(e) => setMatricula(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleManualCheckin()}
              style={{ maxWidth: '300px' }}
            />
            <button className="btn btn-primary rounded-pill px-4" onClick={handleManualCheckin} disabled={manualLoading}>
              {manualLoading ? 'Registrando...' : 'Registrar'}
            </button>
          </div>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row gap-3 mb-4">
            <div className="input-group bg-light rounded-3 overflow-hidden flex-grow-1" style={{ border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder="Buscar por nombre o correo..."
                style={{ fontSize: '13px' }}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>

          {loading ? (
            <div className="text-center py-4">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Cargando...</span>
              </div>
            </div>
          ) : filtered.length > 0 ? (
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="border-bottom">
                  <tr>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 ps-3" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Nombre Completo</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Cuatrimestre</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Correo</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Método</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 text-center" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Estado</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 text-center pe-3" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Asistido</th>
                  </tr>
                </thead>
                <tbody className="border-top-0">
                  {filtered.map((student) => (
                    <tr key={student.idAsistencia} className={student.estado === 'ASISTIDO' ? 'table-success' : ''}>
                      <td className="fw-bold small py-3 border-light ps-3">{student.nombre}</td>
                      <td className="small py-3 border-light text-secondary">{student.cuatrimestre ? `${student.cuatrimestre}°` : '—'}</td>
                      <td className="small py-3 border-light text-secondary">{student.correo}</td>
                      <td className="small py-3 border-light">
                        <span className={`badge rounded-pill px-2 py-1 ${student.metodo === 'QR' ? 'bg-primary bg-opacity-10 text-primary' : 'bg-warning bg-opacity-10 text-warning'}`} style={{ fontSize: '10px' }}>
                          {student.metodo}
                        </span>
                      </td>
                      <td className="py-3 border-light text-center">
                        <span className={`badge rounded-pill px-3 py-2 fw-semibold ${student.estado === 'ASISTIDO' ? 'bg-success bg-opacity-10 text-success' : 'bg-warning bg-opacity-10 text-warning'}`} style={{ fontSize: '11px' }}>
                          {student.estado === 'ASISTIDO' ? 'Asistido' : 'Pendiente'}
                        </span>
                      </td>
                      <td className="py-3 border-light text-center pe-3">
                        <div className="form-check d-flex justify-content-center m-0">
                          <input
                            type="checkbox"
                            className="form-check-input"
                            style={{ width: '20px', height: '20px', cursor: 'pointer' }}
                            checked={student.estado === 'ASISTIDO'}
                            disabled={updatingIds.has(student.idAsistencia)}
                            onChange={() => handleToggleEstado(student)}
                          />
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="text-center py-4">
              <p className="text-secondary small mb-0">No hay asistencias registradas para este evento.</p>
            </div>
          )}
        </div>
        <div className="card-footer bg-transparent border-top p-3 d-flex justify-content-between align-items-center">
          <span className="text-secondary small">
            Mostrando {filtered.length} de {students.length} asistencias
          </span>
          <span className="text-success small fw-semibold">
            {asistidosCount} asistido(s)
          </span>
        </div>
      </div>
    </div>
  )
}

export default AdminCheckIn
