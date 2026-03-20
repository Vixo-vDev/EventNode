import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { userService } from '../../services/userService'
import EditarEstudianteModal from '../../components/modals/EditarEstudianteModal'
import CrearAdministradorModal from '../../components/modals/CrearAdministradorModal'

const INITIAL_ADMIN_FORM = {
  nombre: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  correo: '',
  password: '',
}

function AdminEstudiantes({ user }) {
  const [students, setStudents] = useState([])
  const [admins, setAdmins] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')

  // Estado para crear administrador
  const [adminForm, setAdminForm] = useState(INITIAL_ADMIN_FORM)
  const [adminError, setAdminError] = useState('')
  const [adminLoading, setAdminLoading] = useState(false)

  // Estado para editar estudiante
  const [selectedStudent, setSelectedStudent] = useState(null)

  const isSuperAdmin = user?.originalRole === 'SUPERADMIN'

  const fetchUsers = async () => {
    try {
      const data = await userService.getUsuarios()
      const alumnos = data
        .filter(u => u.rol === 'ALUMNO')
        .map(u => ({
          id: u.idUsuario,
          initials: (u.nombre?.[0] || '') + (u.apellidoPaterno?.[0] || ''),
          name: `${u.nombre} ${u.apellidoPaterno}`,
          nombre: u.nombre,
          apellidoPaterno: u.apellidoPaterno,
          apellidoMaterno: u.apellidoMaterno,
          matricula: u.matricula || '—',
          email: u.correo,
          edad: u.edad,
          sexo: u.sexo,
          cuatrimestre: u.cuatrimestre,
          role: 'STUDENT',
          active: u.estado === 'ACTIVO',
        }))
      const adminList = data
        .filter(u => u.rol === 'ADMINISTRADOR' || u.rol === 'SUPERADMIN')
        .map(u => ({
          id: u.idUsuario,
          name: `${u.nombre} ${u.apellidoPaterno}`,
          role: u.rol === 'SUPERADMIN' ? 'Super Admin' : 'Administrador',
          bg: u.rol === 'SUPERADMIN' ? 'bg-warning bg-opacity-25 text-warning' : 'bg-primary bg-opacity-10 text-primary',
        }))
      setStudents(alumnos)
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
      await userService.crearAdmin({
        ...adminForm,
        idSolicitante: user?.id,
      })
      toast.success('Administrador creado exitosamente')
      setAdminForm(INITIAL_ADMIN_FORM)
      // Cerrar modal programáticamente usando Bootstrap JS
      const modalEl = document.getElementById('crearAdminModal')
      if (modalEl && window.bootstrap) {
        const bsModal = window.bootstrap.Modal.getInstance(modalEl)
        if (bsModal) bsModal.hide()
      }
      // Refrescar lista
      setLoading(true)
      fetchUsers()
    } catch (err) {
      setAdminError(err.message)
      toast.error(err.message)
    } finally {
      setAdminLoading(false)
    }
  }

  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-3">
        <div>
          <h2 className="fw-bold mb-1">Estudiantes</h2>
          <p className="text-secondary small mb-0">
            Administrar y supervisar las cuentas, roles y estados de los estudiantes.
          </p>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-header bg-white border-bottom-0 p-4">
          <div className="d-flex flex-column flex-md-row gap-3">
            <div className="input-group bg-light rounded-3 overflow-hidden flex-grow-1" style={{ border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder="Buscar por nombre, matrícula o correo..."
                style={{ fontSize: '13px' }}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="card-body p-0">
          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Cargando...</span>
              </div>
            </div>
          ) : students.length > 0 ? (
            <div className="table-responsive">
              <table className="table align-middle mb-0">
                <thead>
                  <tr>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom ps-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>Nombre Completo</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Matrícula</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Email</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Estado</th>
                    <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom text-end pe-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>Acciones</th>
                  </tr>
                </thead>
                <tbody className="border-top-0">
                  {students
                    .filter(student =>
                      student.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                      student.matricula.toLowerCase().includes(searchTerm.toLowerCase()) ||
                      student.email.toLowerCase().includes(searchTerm.toLowerCase())
                    )
                    .map(student => (
                    <tr key={student.id}>
                      <td className="py-3 border-light ps-4">
                        <div className="d-flex align-items-center gap-3">
                          <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold" style={{ width: '32px', height: '32px', fontSize: '12px' }}>
                            {student.initials}
                          </div>
                          <span className="fw-bold text-dark small">{student.name}</span>
                        </div>
                      </td>
                      <td className="small py-3 border-light text-secondary">{student.matricula}</td>
                      <td className="small py-3 border-light text-secondary">{student.email}</td>
                      <td className="py-3 border-light">
                        <div className={`d-flex align-items-center gap-1 fw-bold ${student.active ? 'text-success' : 'text-secondary'}`} style={{ fontSize: '11px' }}>
                          <span style={{ fontSize: '14px', lineHeight: '1' }}>{student.active ? '•' : '○'}</span>
                          {student.active ? 'Activo' : 'Inactivo'}
                        </div>
                      </td>
                      <td className="py-3 border-light text-end pe-4">
                        <div className="d-flex justify-content-end gap-2 text-secondary">
                          <button
                            className="btn btn-link text-secondary p-0 m-0"
                            title="Editar"
                            data-bs-toggle="modal"
                            data-bs-target="#editarEstudianteModal"
                            onClick={() => setSelectedStudent(student)}
                          >
                            <i className="bi bi-pencil" style={{ fontSize: '13px' }}></i>
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
              <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '56px', height: '56px' }}>
                <i className="bi bi-people text-primary fs-4"></i>
              </div>
              <h6 className="fw-bold mb-1">No hay estudiantes registrados</h6>
              <p className="text-secondary small mb-0">
                Los estudiantes aparecerán aquí cuando se registren en la plataforma.
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Sección Administradores */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-header bg-white border-bottom-0 p-4">
          <div className="d-flex align-items-center justify-content-between">
            <h5 className="fw-bold mb-0 text-dark">Administradores</h5>
            {isSuperAdmin && (
              <button
                className="btn btn-primary btn-sm d-flex align-items-center gap-2 rounded-3 px-3"
                data-bs-toggle="modal"
                data-bs-target="#crearAdminModal"
                style={{ fontSize: '13px' }}
              >
                <i className="bi bi-plus-lg"></i>
                Nuevo Admin
              </button>
            )}
          </div>
        </div>
        <div className="card-body px-4 pb-4 pt-0">
          {admins.length > 0 ? (
            <div className="row g-3">
              {admins.map((admin, index) => (
                <div key={index} className="col-12 col-md-4">
                  <div className="border border-light-subtle rounded-4 p-3 d-flex align-items-center justify-content-between card-hover" style={{ transition: 'all 0.2s ease' }}>
                    <div className="d-flex align-items-center gap-3">
                      <div className={`rounded-circle d-flex align-items-center justify-content-center fw-bold ${admin.bg}`} style={{ width: '40px', height: '40px' }}>
                        <i className="bi bi-person-fill"></i>
                      </div>
                      <div className="lh-sm">
                        <div className="fw-bold text-dark" style={{ fontSize: '13px' }}>{admin.name}</div>
                        <div className="text-secondary" style={{ fontSize: '11px' }}>{admin.role}</div>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-secondary small mb-0">No hay administradores adicionales.</p>
          )}
        </div>
      </div>

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
    </div>
  )
}

export default AdminEstudiantes
