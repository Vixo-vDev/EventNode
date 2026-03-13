import { Link } from 'react-router-dom'

function AdminCheckIn() {
  const students = [
    { id: '1', nombre: 'Adrián González Ruiz', cuatrimestre: '4°', correo: 'adrian.grz@tec.mx', estado: 'Presente' },
    { id: '2', nombre: 'Beatriz Martínez Flores', cuatrimestre: '6°', correo: 'b.martinez@tec.mx', estado: 'Presente' },
    { id: '3', nombre: 'Carlos Eduardo López', cuatrimestre: '1°', correo: 'c.eduardo@tec.mx', estado: 'Pendiente' },
    { id: '4', nombre: 'Diana Patricia Sosa', cuatrimestre: '9°', correo: 'd.patricia@tec.mx', estado: 'Presente' },
    { id: '5', nombre: 'Esteban Méndez Lima', cuatrimestre: '3°', correo: 'e.mendez@tec.mx', estado: 'Pendiente' },
    { id: '6', nombre: 'Fernando Villa Gómez', cuatrimestre: '5°', correo: 'f.villa@tec.mx', estado: 'Presente' },
    { id: '7', nombre: 'Fernando Villa Gómez', cuatrimestre: '5°', correo: 'f.villa@tec.mx', estado: 'Presente' },
    { id: '8', nombre: 'Fernando Villa Gómez', cuatrimestre: '5°', correo: 'f.villa@tec.mx', estado: 'Presente' },
  ]

  return (
    <div>
      <div className="d-flex align-items-center gap-3 mb-4">
        <Link to="/admin/evento/1" className="btn btn-light rounded-circle d-flex align-items-center justify-content-center p-0" style={{ width: '40px', height: '40px' }}>
          <i className="bi bi-arrow-left text-secondary fs-5"></i>
        </Link>
        <span className="fw-semibold small">Tech Summit 2023</span>
      </div>

      <div className="mb-4">
        <h2 className="fw-bold mb-1">Lista de Asistencia (Check-in)</h2>
        <div className="text-secondary small">
          Gestiona y verifica la asistencia de los alumnos registrados para este evento.
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
                placeholder="Buscar por nombre o correo institucional..."
                style={{ fontSize: '13px' }}
              />
            </div>
            <button className="btn btn-outline-secondary rounded-3 d-flex align-items-center gap-2 small px-4 flex-shrink-0 border-light-subtle">
              <i className="bi bi-funnel"></i>
              Filtros
            </button>
          </div>

          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="border-bottom">
                <tr>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 ps-3" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Nombre Completo</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Cuatrimestre</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Correo Institucional</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 text-end pe-4" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Estado</th>
                </tr>
              </thead>
              <tbody className="border-top-0">
                {students.map((student, index) => (
                  <tr key={index}>
                    <td className="fw-bold small py-3 border-light ps-3">{student.nombre}</td>
                    <td className="small py-3 border-light text-secondary">{student.cuatrimestre}</td>
                    <td className="small py-3 border-light text-secondary">{student.correo}</td>
                    <td className="py-3 border-light text-end pe-4">
                      {student.estado === 'Presente' ? (
                        <span className="badge bg-success bg-opacity-10 text-success rounded-pill px-3 py-2 fw-semibold" style={{ fontSize: '11px' }}>
                          Presente
                        </span>
                      ) : (
                        <span className="badge bg-secondary bg-opacity-10 text-secondary rounded-pill px-3 py-2 fw-semibold" style={{ fontSize: '11px' }}>
                          Pendiente
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="card-footer bg-transparent border-top p-3 d-flex flex-column flex-md-row justify-content-between align-items-center gap-3">
          <span className="text-secondary small" style={{ fontSize: '12px' }}>
            Mostrando 1-8 de 142 alumnos registrados
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
              <li className="page-item disabled">
                <a className="page-link border-0 text-secondary bg-transparent h-100 d-flex align-items-center rounded" href="#" tabIndex="-1" aria-disabled="true">
                  <span className="small">...</span>
                </a>
              </li>
              <li className="page-item">
                <a className="page-link border-0 text-dark bg-transparent rounded d-flex align-items-center justify-content-center" href="#" style={{ width: '28px', height: '28px' }}>24</a>
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
    </div>
  )
}

export default AdminCheckIn
