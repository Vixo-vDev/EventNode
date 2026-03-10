import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'
import EditarEstudianteModal from '../../components/modals/EditarEstudianteModal'

function AdminEstudiantes() {
  const students = [
    { id: 1, initials: 'JD', name: 'Jane Doe', matricula: '202300042', email: 'jane.doe@university.edu', role: 'STUDENT', active: true },
    { id: 2, initials: 'MS', name: 'Mark Smith', matricula: '202300055', email: 'mark.smith@university.edu', role: 'ADMIN', active: false },
    { id: 3, initials: 'JD', name: 'Jane Doe', matricula: '202300042', email: 'jane.doe@university.edu', role: 'STUDENT', active: true },
    { id: 4, initials: 'MS', name: 'Mark Smith', matricula: '202300055', email: 'mark.smith@university.edu', role: 'ADMIN', active: false },
    { id: 5, initials: 'JD', name: 'Jane Doe', matricula: '202300042', email: 'jane.doe@university.edu', role: 'STUDENT', active: true },
    { id: 6, initials: 'MS', name: 'Mark Smith', matricula: '202300055', email: 'mark.smith@university.edu', role: 'ADMIN', active: false },
  ]

  const admins = [
    { name: 'Jordan Smith', role: 'Master Level', bg: 'bg-warning bg-opacity-25 text-warning' },
    { name: 'Elena Vance', role: 'Analytics Head', bg: 'bg-primary bg-opacity-10 text-primary' },
    { name: 'Marcus Chen', role: 'Security Officer', bg: 'bg-danger bg-opacity-10 text-danger' }
  ]

  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-3">
        <div>
          <h2 className="fw-bold mb-1">Estudiantes</h2>
          <p className="text-secondary small mb-0">
            Administrar y supervisar las cuentas, roles y estados de los estudiantes.
          </p>
        </div>
        <button className="btn btn-primary rounded-3 d-flex align-items-center gap-2 flex-shrink-0 px-3 fw-semibold" style={{ fontSize: '13px' }}>
          <i className="bi bi-person-plus-fill"></i>
          Agregar estudiante
        </button>
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
                placeholder="Search by Name, Matricula, or Email"
                style={{ fontSize: '13px' }}
              />
            </div>
            <div className="d-flex align-items-center gap-2">
              <span className="text-secondary small fw-bold text-uppercase" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>ESTADO</span>
              <select className="form-select border-light-subtle rounded-3 small fw-semibold" style={{ width: '110px', fontSize: '13px' }}>
                <option selected>Todos</option>
                <option>Active</option>
                <option>Inactive</option>
              </select>
              <button className="btn btn-light rounded-3 ms-1 d-flex align-items-center justify-content-center text-secondary" style={{ width: '38px', height: '38px' }}>
                <i className="bi bi-funnel"></i>
              </button>
            </div>
          </div>
        </div>

        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table align-middle mb-0">
              <thead>
                <tr>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom ps-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>Nombre Completo</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Matricula</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Email</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Rol</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '10px', letterSpacing: '1px' }}>Estado</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom text-end pe-4" style={{ fontSize: '10px', letterSpacing: '1px' }}>Acciones</th>
                </tr>
              </thead>
              <tbody className="border-top-0">
                {students.map((student, index) => (
                  <tr key={index}>
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
                      <span className={`badge rounded-pill px-3 py-1 fw-bold ${student.role === 'ADMIN' ? 'bg-light text-secondary text-uppercase' : 'bg-primary bg-opacity-10 text-primary'}`} style={{ fontSize: '10px', letterSpacing: '0.5px' }}>
                        {student.role}
                      </span>
                    </td>
                    <td className="py-3 border-light">
                      <div className={`d-flex align-items-center gap-1 fw-bold ${student.active ? 'text-success' : 'text-secondary'}`} style={{ fontSize: '11px' }}>
                        <span style={{ fontSize: '14px', lineHeight: '1' }}>{student.active ? '•' : '○'}</span>
                        {student.active ? 'Active' : 'Inactive'}
                      </div>
                    </td>
                    <td className="py-3 border-light text-end pe-4">
                      <div className="d-flex justify-content-end gap-2 text-secondary">
                        <button
                          className="btn btn-link text-secondary p-0 m-0"
                          title="Editar"
                          data-bs-toggle="modal"
                          data-bs-target="#editarEstudianteModal"
                        >
                          <i className="bi bi-pencil" style={{ fontSize: '13px' }}></i>
                        </button>
                        <button className="btn btn-link text-secondary p-0 m-0" title="Bloquear">
                          <i className="bi bi-slash-circle" style={{ fontSize: '13px' }}></i>
                        </button>
                        <button className="btn btn-link text-secondary p-0 m-0" title="Eliminar">
                          <i className="bi bi-trash" style={{ fontSize: '13px' }}></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="card-footer bg-transparent border-top p-4 d-flex flex-column flex-md-row justify-content-between align-items-center gap-3">
          <span className="text-secondary small" style={{ fontSize: '12px' }}>
            Mostrando 1 - 6 de 42 resultados
          </span>
          <nav aria-label="Page navigation">
            <ul className="pagination pagination-sm mb-0 gap-1">
              <li className="page-item disabled">
                <a className="page-link border-0 text-secondary bg-transparent h-100 d-flex align-items-center rounded" href="#" tabIndex="-1" aria-disabled="true">
                  <i className="bi bi-chevron-left small"></i>
                </a>
              </li>
              <li className="page-item active" aria-current="page">
                <a className="page-link border-0 bg-primary text-white rounded d-flex align-items-center justify-content-center" href="#" style={{ width: '28px', height: '28px' }}>1</a>
              </li>
              <li className="page-item">
                <a className="page-link border-0 text-dark bg-transparent rounded d-flex align-items-center justify-content-center" href="#" style={{ width: '28px', height: '28px' }}>2</a>
              </li>
              <li className="page-item">
                <a className="page-link border-0 text-dark bg-transparent rounded d-flex align-items-center justify-content-center" href="#" style={{ width: '28px', height: '28px' }}>3</a>
              </li>
              <li className="page-item">
                <a className="page-link border-0 text-dark bg-transparent h-100 d-flex align-items-center rounded" href="#">
                  <i className="bi bi-chevron-right small"></i>
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </div>

      {/* Seccion Administradores */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-header bg-white border-bottom-0 p-4 d-flex justify-content-between align-items-center">
          <h5 className="fw-bold mb-0 text-dark">Administradores</h5>
          <button className="btn btn-link text-primary p-0 d-flex align-items-center gap-2 text-decoration-none">
            <i className="bi bi-person-plus-fill fs-5"></i>
          </button>
        </div>
        <div className="card-body px-4 pb-4 pt-0">
          <div className="row g-3">
            {admins.map((admin, index) => (
              <div key={index} className="col-12 col-md-4">
                <div className="border border-light-subtle rounded-4 p-3 d-flex align-items-center justify-content-between">
                  <div className="d-flex align-items-center gap-3">
                    <div className={`rounded-circle d-flex align-items-center justify-content-center fw-bold ${admin.bg}`} style={{ width: '40px', height: '40px' }}>
                      <i className="bi bi-person-fill"></i>
                    </div>
                    <div className="lh-sm">
                      <div className="fw-bold text-dark" style={{ fontSize: '13px' }}>{admin.name}</div>
                      <div className="text-secondary" style={{ fontSize: '11px' }}>{admin.role}</div>
                    </div>
                  </div>
                  <button className="btn btn-link text-secondary p-0">
                    <i className="bi bi-three-dots-vertical"></i>
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <EditarEstudianteModal />
    </DashboardLayout>
  )
}

export default AdminEstudiantes
