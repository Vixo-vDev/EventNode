import { Link } from 'react-router-dom'

function AdminPreCheckIn() {
  const students = [
    { id: '1', matricula: 'A01234567', nombre: 'Adrián González Ruiz', correo: 'adrian.grz@tec.mx', estado: 'Confirmado' },
    { id: '2', matricula: 'A01658392', nombre: 'Beatriz Martínez Flores', correo: 'b.martinez@tec.mx', estado: 'Confirmado' },
    { id: '3', matricula: 'A01029384', nombre: 'Carlos Eduardo Silva', correo: 'carlos.silva@tec.mx', estado: 'Confirmado' },
    { id: '4', matricula: 'A01748291', nombre: 'Daniela Herrera Vega', correo: 'daniela.hva@tec.mx', estado: 'Confirmado' },
    { id: '5', matricula: 'A01324152', nombre: 'Esteban Navarro', correo: 'enavarro@tec.mx', estado: 'Confirmado' },
    { id: '6', matricula: 'A01456721', nombre: 'Fernanda López Dávila', correo: 'f.lopezd@tec.mx', estado: 'Confirmado' },
    { id: '7', matricula: 'A01552233', nombre: 'Gerardo Ortiz Pineda', correo: 'gortiz@tec.mx', estado: 'Confirmado' },
    { id: '8', matricula: 'A01889900', nombre: 'Humberto Sánchez', correo: 'hsanchez@tec.mx', estado: 'Confirmado' }
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
        <h2 className="fw-bold mb-1">Lista de Pre-check-in</h2>
        <div className="text-secondary small d-flex align-items-center gap-2">
          <i className="bi bi-calendar-event"></i>
          Congreso de Innovación Tecnológica 2024
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row gap-3 mb-4">
            <div className="input-group bg-light rounded-3 overflow-hidden flex-grow-1" style={{ maxWidth: '400px', border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder="Buscar por nombre, matrícula o correo..."
                style={{ fontSize: '13px' }}
              />
            </div>
            <button className="btn btn-outline-secondary rounded-3 d-flex align-items-center gap-2 small px-4 ms-auto border-light-subtle">
              <i className="bi bi-funnel"></i>
              Filtrar
            </button>
          </div>

          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="border-bottom">
                <tr>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Matrícula</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Nombre Completo</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Correo Institucional</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 text-end pe-4" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Estado</th>
                </tr>
              </thead>
              <tbody className="border-top-0">
                {students.map((student, index) => (
                  <tr key={index}>
                    <td className="fw-bold small py-3 border-light">{student.matricula}</td>
                    <td className="small py-3 border-light text-secondary">{student.nombre}</td>
                    <td className="small py-3 border-light text-secondary">{student.correo}</td>
                    <td className="py-3 border-light text-end pe-4">
                      <span className="badge bg-success bg-opacity-10 text-success rounded-pill px-3 py-2 fw-semibold" style={{ fontSize: '11px' }}>
                        {student.estado}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="card-footer bg-transparent border-top p-3 d-flex flex-column flex-md-row justify-content-between align-items-center gap-3">
          <span className="text-secondary small">
            Mostrando 1 a 8 de 124 estudiantes
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
    </div>
  )
}

export default AdminPreCheckIn
