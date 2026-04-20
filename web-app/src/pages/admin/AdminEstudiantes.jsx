import { useEffect, useRef, useState } from 'react'
import { toast } from 'react-toastify'
import { useTranslation } from '../../i18n/I18nContext'
import { userService } from '../../services/userService'
import EditarEstudianteModal from '../../components/modals/EditarEstudianteModal'
import CrearAdministradorModal from '../../components/modals/CrearAdministradorModal'
import CrearEstudianteModal from '../../components/modals/CrearEstudianteModal'
import EditarAdministradorModal from '../../components/modals/EditarAdministradorModal'

const INITIAL_ADMIN_FORM = {
  nombre: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  correo: '',
  password: '',
}

function AdminEstudiantes({ user }) {
  const { t } = useTranslation()
  const [activeTab, setActiveTab] = useState('estudiantes')
  const [students, setStudents] = useState([])
  const [admins, setAdmins] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [adminForm, setAdminForm] = useState(INITIAL_ADMIN_FORM)
  const [adminError, setAdminError] = useState('')
  const [adminLoading, setAdminLoading] = useState(false)
  const [selectedStudent, setSelectedStudent] = useState(null)
  const [viewStudent, setViewStudent] = useState(null)
  const [selectedAdmin, setSelectedAdmin] = useState(null)
  const [toggleTarget, setToggleTarget] = useState(null)
  const [toggleLoading, setToggleLoading] = useState(false)
  const toggleCloseBtnRef = useRef(null)

  const isSuperAdmin = user?.originalRole === 'SUPERADMIN'

  const fetchUsers = async () => {
    try {
      const data = await userService.getUsuarios()
      const studentList = data
        .filter(u => u.rol === 'ALUMNO')
        .map(u => ({
          id: u.idUsuario,
          initials: (u.nombre?.[0] || '') + (u.apellidoPaterno?.[0] || ''),
          name: `${u.nombre} ${u.apellidoPaterno}`,
          nombre: u.nombre,
          apellidoPaterno: u.apellidoPaterno,
          apellidoMaterno: u.apellidoMaterno,
          matricula: u.matricula || '-',
          email: u.correo,
          edad: u.edad,
          sexo: u.sexo,
          cuatrimestre: u.cuatrimestre,
          active: u.estado === 'ACTIVO',
        }))

      const adminList = data
        .filter(u => (u.rol === 'ADMINISTRADOR' || u.rol === 'SUPERADMIN') && u.idUsuario !== user?.id)
        .map(u => ({
          id: u.idUsuario,
          name: `${u.nombre} ${u.apellidoPaterno}`,
          nombre: u.nombre,
          apellidoPaterno: u.apellidoPaterno,
          apellidoMaterno: u.apellidoMaterno,
          email: u.correo,
          active: u.estado === 'ACTIVO',
          esPrincipal: u.esPrincipal === true,
          role: u.rol === 'SUPERADMIN' ? 'Super Admin' : 'Administrador',
          bg: u.rol === 'SUPERADMIN' ? 'bg-info bg-opacity-25 text-danger' : 'bg-warning bg-opacity-10 text-dark',
        }))

      setStudents(studentList)
      setAdmins(adminList)
    } catch {
      setStudents([])
      setAdmins([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUsers()
  }, [])

  const handleAdminChange = (e) => {
    const { name, value } = e.target
    setAdminForm(prev => ({ ...prev, [name]: value }))
    setAdminError('')
  }

  const handleAdminSubmit = async (e) => {
    e.preventDefault()
    setAdminError('')
    setAdminLoading(true)
    try {
      await userService.crearAdmin({ ...adminForm, idSolicitante: user?.id })
      toast.success(t('students.adminCreatedSuccess'))
      setAdminForm(INITIAL_ADMIN_FORM)
      setLoading(true)
      fetchUsers()
    } catch (err) {
      setAdminError(err.message)
      toast.error(err.message)
    } finally {
      setAdminLoading(false)
    }
  }

  const handleToggleState = async () => {
    if (!toggleTarget) return
    setToggleLoading(true)
    try {
      const res = await userService.cambiarEstado(toggleTarget.id)
      const nuevoEstado = res?.estado === 'ACTIVO'

      if (toggleTarget.type === 'student') {
        setStudents(prev => prev.map(item => item.id === toggleTarget.id ? { ...item, active: nuevoEstado } : item))
      } else {
        setAdmins(prev => prev.map(item => item.id === toggleTarget.id ? { ...item, active: nuevoEstado } : item))
      }

      toast.success(res?.mensaje || 'Estado actualizado')
      setToggleTarget(null)
      toggleCloseBtnRef.current?.click()
    } catch (err) {
      toast.error(err.message || 'Error al cambiar estado')
    } finally {
      setToggleLoading(false)
    }
  }

  const estudiantesFiltrados = students.filter(student =>
    student.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.matricula.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.email.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const adminsFiltrados = admins.filter(admin =>
    admin.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    admin.role.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-3">
        <div>
          <h2 className="fw-bold mb-1">Usuarios</h2>
          <p className="text-secondary small mb-0">Gestiona los usuarios del sistema</p>
        </div>
        {activeTab === 'estudiantes' && (
          <button
            className="btn btn-dark rounded-pill d-flex align-items-center gap-2 flex-shrink-0"
            data-bs-toggle="modal"
            data-bs-target="#crearEstudianteModal"
          >
            <i className="bi bi-plus-circle"></i>
            Nuevo estudiante
          </button>
        )}
        {isSuperAdmin && activeTab === 'administradores' && (
          <button
            className="btn btn-dark rounded-pill d-flex align-items-center gap-2 flex-shrink-0"
            data-bs-toggle="modal"
            data-bs-target="#crearAdminModal"
          >
            <i className="bi bi-plus-circle"></i>
            {t('students.newAdmin')}
          </button>
        )}
      </div>

      <div className="d-flex gap-2 mb-4">
        <button
            className={`btn rounded-pill px-4 py-2 fw-semibold ${activeTab === 'estudiantes' ? 'btn-dark' : 'btn-outline-secondary'}`}
          style={{ fontSize: '13px' }}
          onClick={() => { setActiveTab('estudiantes'); setSearchTerm('') }}
        >
          <i className="bi bi-mortarboard me-2"></i>
          Estudiantes
        </button>
        {isSuperAdmin && (
          <button
            className={`btn rounded-pill px-4 py-2 fw-semibold ${activeTab === 'administradores' ? 'btn-dark' : 'btn-outline-secondary'}`}
            style={{ fontSize: '13px' }}
            onClick={() => { setActiveTab('administradores'); setSearchTerm('') }}
          >
            <i className="bi bi-shield-person me-2"></i>
            Administradores
          </button>
        )}
      </div>

      {activeTab === 'estudiantes' && (
        <div className="card border-0 shadow-sm rounded-4 mb-4">
          <div className="card-header bg-white border-bottom-0 p-4">
            <div className="input-group bg-light rounded-3 overflow-hidden" style={{ border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder={t('students.searchPlaceholder')}
                style={{ fontSize: '13px' }}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
          <div className="card-body p-0">
            {loading ? (
              <div className="text-center py-5">
                <div className="text-dark small">cargando...</div>
              </div>
            ) : estudiantesFiltrados.length > 0 ? (
              <div className="table-responsive">
                <table className="table align-middle mb-0">
                  <thead>
                    <tr>
                      <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom ps-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>{t('students.fullName')}</th>
                      <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>{t('students.matricula')}</th>
                      <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>{t('students.email')}</th>
                      <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>{t('students.status')}</th>
                      <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom text-end pe-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>{t('students.actions')}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {estudiantesFiltrados.map(student => (
                      <tr key={student.id}>
                        <td className="py-3 border-light ps-4">
                          <div className="d-flex align-items-center gap-3">
                            <div className="bg-warning bg-opacity-25 text-danger rounded-circle d-flex align-items-center justify-content-center fw-bold" style={{ width: '32px', height: '32px', fontSize: '12px' }}>
                              {student.initials}
                            </div>
                            <span className="fw-bold text-dark small">{student.name}</span>
                          </div>
                        </td>
                        <td className="small py-3 border-light text-secondary">{student.matricula}</td>
                        <td className="small py-3 border-light text-secondary">{student.email}</td>
                        <td className="py-3 border-light">
                          <span className={`badge rounded-pill ${student.active ? 'bg-success text-white' : 'bg-danger text-white'}`}>
                            {student.active ? 'ACTIVO' : 'INACTIVO'}
                          </span>
                        </td>
                        <td className="py-3 border-light text-end pe-4">
                          <div className="d-flex justify-content-end gap-2">
                            <button
                              className="btn btn-link text-primary p-0 m-0"
                              title="Ver detalle"
                              data-bs-toggle="modal"
                              data-bs-target="#verEstudianteModal"
                              onClick={() => setViewStudent(student)}
                            >
                              <i className="bi bi-eye" style={{ fontSize: '13px' }}></i>
                            </button>
                            <button
                              className="btn btn-link text-secondary p-0 m-0"
                              title="Editar"
                              data-bs-toggle="modal"
                              data-bs-target="#editarEstudianteModal"
                              onClick={() => setSelectedStudent(student)}
                            >
                              <i className="bi bi-pencil" style={{ fontSize: '13px' }}></i>
                            </button>
                            <button
                              className={`btn btn-link p-0 m-0 ${student.active ? 'text-danger' : 'text-warning'}`}
                              title={student.active ? 'Desactivar' : 'Activar'}
                              data-bs-toggle="modal"
                              data-bs-target="#toggleUserModal"
                              onClick={() => setToggleTarget({ id: student.id, type: 'student', name: student.name, active: student.active })}
                            >
                              <i className={`bi ${student.active ? 'bi-person-dash' : 'bi-person-check'}`} style={{ fontSize: '13px' }}></i>
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="text-center py-5">
                <div className="rounded-circle bg-danger bg-opacity-25 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '56px', height: '56px' }}>
                  <i className="bi bi-people text-warning fs-4"></i>
                </div>
                <h6 className="fw-bold mb-1">{t('students.noStudents')}</h6>
                <p className="text-secondary small mb-0">{t('students.studentsComingSoon')}</p>
              </div>
            )}
          </div>
        </div>
      )}

      {isSuperAdmin && activeTab === 'administradores' && (
        <div className="card border-0 shadow-sm rounded-4 mb-4">
          <div className="card-header bg-white border-bottom-0 p-4">
            <div className="input-group bg-light rounded-3 overflow-hidden" style={{ maxWidth: '400px', border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder="Buscar administrador..."
                style={{ fontSize: '13px' }}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
          <div className="card-body px-4 pb-4 pt-0">
            {loading ? (
              <div className="text-center py-5">
                <div className="text-dark small">cargando...</div>
              </div>
            ) : adminsFiltrados.length > 0 ? (
              <div className="row g-3">
                {adminsFiltrados.map(admin => (
                  <div key={admin.id} className="col-12 col-md-4">
                    <div className="border border-dark rounded-4 p-3 d-flex align-items-center justify-content-between">
                      <div className="d-flex align-items-center gap-3">
                        <div className={`rounded-circle d-flex align-items-center justify-content-center fw-bold ${admin.bg}`} style={{ width: '40px', height: '40px' }}>
                          <i className="bi bi-person-fill"></i>
                        </div>
                        <div className="lh-sm">
                          <div className="fw-bold text-dark" style={{ fontSize: '13px' }}>{admin.name}</div>
                          <div className="text-secondary" style={{ fontSize: '11px' }}>{admin.role}</div>
                        </div>
                      </div>
                      <div className="d-flex gap-2">
                        <button
                          className="btn btn-link text-primary p-0"
                          title="Ver detalle"
                          data-bs-toggle="modal"
                          data-bs-target="#verAdminModal"
                          onClick={() => setSelectedAdmin(admin)}
                        >
                          <i className="bi bi-eye" style={{ fontSize: '14px' }}></i>
                        </button>
                        {isSuperAdmin && !admin.esPrincipal && (
                          <button
                            className="btn btn-link text-secondary p-0"
                            title="Editar"
                            data-bs-toggle="modal"
                            data-bs-target="#editarAdminModal"
                            onClick={() => setSelectedAdmin(admin)}
                          >
                            <i className="bi bi-pencil" style={{ fontSize: '14px' }}></i>
                          </button>
                        )}
                        {isSuperAdmin && !admin.esPrincipal && (
                          <button
                            className={`btn btn-link p-0 ${admin.active ? 'text-warning' : 'text-success'}`}
                            title={admin.active ? 'Desactivar' : 'Activar'}
                            data-bs-toggle="modal"
                            data-bs-target="#toggleUserModal"
                            onClick={() => setToggleTarget({ id: admin.id, type: 'admin', name: admin.name, active: admin.active })}
                          >
                            <i className={`bi ${admin.active ? 'bi-person-dash' : 'bi-person-check'}`} style={{ fontSize: '14px' }}></i>
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-5">
                <div className="rounded-circle bg-warning bg-opacity-25 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '56px', height: '56px' }}>
                  <i className="bi bi-shield-person text-danger fs-4"></i>
                </div>
                <h6 className="fw-bold mb-1">{t('students.noAdmins')}</h6>
              </div>
            )}
          </div>
        </div>
      )}

      <CrearEstudianteModal onStudentCreated={fetchUsers} />

      <EditarAdministradorModal
        admin={selectedAdmin}
        onAdminUpdated={fetchUsers}
      />

      <EditarEstudianteModal
        student={selectedStudent}
        onStudentUpdated={fetchUsers}
      />

      <CrearAdministradorModal
        formData={adminForm}
        error={adminError}
        isLoading={adminLoading}
        onChange={handleAdminChange}
        onSubmit={handleAdminSubmit}
      />

      <div className="modal" id="verEstudianteModal" tabIndex="-1" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content border-0 rounded-4 shadow">
            <div className="modal-header border-0 px-4 pt-4 pb-0">
              <h5 className="fw-bold">Detalle del estudiante</h5>
              <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div className="modal-body px-4 py-3">
              {viewStudent && (
                <div className="d-flex flex-column gap-3">
                  <div className="d-flex align-items-center gap-3 mb-2">
                    <div className="bg-warning bg-opacity-25 text-danger rounded-circle d-flex align-items-center justify-content-center fw-bold" style={{ width: '52px', height: '52px', fontSize: '18px' }}>
                      {viewStudent.initials}
                    </div>
                    <div>
                      <div className="fw-bold text-dark">{viewStudent.name}</div>
                      <span className={`badge rounded-pill ${viewStudent.active ? 'bg-success text-white' : 'bg-danger text-white'}`}>
                        {viewStudent.active ? 'ACTIVO' : 'INACTIVO'}
                      </span>
                    </div>
                  </div>
                  <div className="border-top pt-3 d-flex flex-column gap-2">
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Apellido Paterno</div>
                      <div className="fw-semibold small">{viewStudent.apellidoPaterno || '-'}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Apellido Materno</div>
                      <div className="fw-semibold small">{viewStudent.apellidoMaterno || '-'}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Matriula</div>
                      <div className="fw-semibold small">{viewStudent.matricula}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Correo electroncio</div>
                      <div className="fw-semibold small">{viewStudent.email || '-'}</div>
                    </div>
                    <div className="d-flex gap-4">
                      <div>
                        <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Edad</div>
                        <div className="fw-semibold small">{viewStudent.edad || '-'}</div>
                      </div>
                      <div>
                        <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Sexo</div>
                        <div className="fw-semibold small">{viewStudent.sexo || '-'}</div>
                      </div>
                      <div>
                        <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Cuatrimestre</div>
                        <div className="fw-semibold small">{viewStudent.cuatrimestre || '-'}</div>
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>
            <div className="modal-footer border-top px-4 py-3">
              <button type="button" className="btn btn-dark rounded-pill px-4" data-bs-dismiss="modal">Cerrar</button>
            </div>
          </div>
        </div>
      </div>

      <div className="modal" id="verAdminModal" tabIndex="-1" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content border-0 rounded-4 shadow">
            <div className="modal-header border-0 px-4 pt-4 pb-0">
              <h5 className="fw-bold">Detalle del administrador</h5>
              <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div className="modal-body px-4 py-3">
              {selectedAdmin && (
                <div className="d-flex flex-column gap-3">
                  <div className="d-flex align-items-center gap-3 mb-2">
                    <div className={`rounded-circle d-flex align-items-center justify-content-center fw-bold ${selectedAdmin.bg}`} style={{ width: '52px', height: '52px', fontSize: '18px' }}>
                      <i className="bi bi-person-fill"></i>
                    </div>
                    <div>
                      <div className="fw-bold text-dark">{selectedAdmin.name}</div>
                      <span className={`badge rounded-pill ${selectedAdmin.role === 'Super Admin' ? 'bg-info bg-opacity-25 text-danger' : 'bg-warning bg-opacity-10 text-dark'}`} style={{ fontSize: '11px' }}>
                        {selectedAdmin.role}
                      </span>
                    </div>
                  </div>
                  <div className="border-top pt-3 d-flex flex-column gap-2">
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Apellido Paterno</div>
                      <div className="fw-semibold small">{selectedAdmin.apellidoPaterno || '-'}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Apellido Materno</div>
                      <div className="fw-semibold small">{selectedAdmin.apellidoMaterno || '-'}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Correo electornico</div>
                      <div className="fw-semibold small">{selectedAdmin.email || '-'}</div>
                    </div>
                    <div>
                      <div className="text-uppercase text-secondary fw-bold" style={{ fontSize: '10px', letterSpacing: '1px' }}>Estado</div>
                      <span className={`badge rounded-pill ${selectedAdmin.active ? 'bg-success text-white' : 'bg-danger text-white'}`}>
                        {selectedAdmin.active ? 'ACTIVO' : 'INACTIVO'}
                      </span>
                    </div>
                  </div>
                </div>
              )}
            </div>
            <div className="modal-footer border-top px-4 py-3">
              <button type="button" className="btn btn-dark rounded-pill px-4" data-bs-dismiss="modal">Cerrar</button>
            </div>
          </div>
        </div>
      </div>

      <div className="modal" id="toggleUserModal" tabIndex="-1" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered modal-sm">
          <div className="modal-content border-0 rounded-4 shadow text-center p-4">
            <div className="mb-3">
              <i className={`bi ${toggleTarget?.active ? 'bi-person-dash-fill text-danger' : 'bi-person-check-fill text-warning'}`} style={{ fontSize: '3rem' }}></i>
            </div>
            <h6 className="fw-bold mb-2">
              {toggleTarget?.active ? 'Desactivar usuario' : 'Activar usuario'}
            </h6>
            <p className="text-secondary small mb-3">
              {toggleTarget?.active
                ? `Seguro que quieres desactivar a ${toggleTarget?.name}`
                : `Seguro que quieres activar a ${toggleTarget?.name}`}
            </p>
            <div className="d-flex justify-content-center gap-2">
              <button className="btn btn-link text-secondary text-decoration-none" data-bs-dismiss="modal" ref={toggleCloseBtnRef}>Cancelar</button>
              <button
                className={`btn rounded-pill px-4 ${toggleTarget?.active ? 'btn-danger' : 'btn-warning'}`}
                onClick={handleToggleState}
                disabled={toggleLoading}
              >
                {toggleLoading ? 'Guardando...' : toggleTarget?.active ? 'Desactivar' : 'Activar'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AdminEstudiantes
