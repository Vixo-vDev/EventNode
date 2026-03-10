import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'
import CrearDiplomaModal from '../../components/modals/CrearDiplomaModal'
import EditarDiplomaModal from '../../components/modals/EditarDiplomaModal'

function AdminDiplomas() {
  const diplomas = [
    { id: 1, nombre: 'Diploma', evento: 'Masterclass en diseño de IU', fecha: '12 Oct 2023', estado: '19/19' },
    { id: 2, nombre: 'Diploma 2', evento: 'Taller de CSS', fecha: '15 Oct 2023', estado: '30/35' },
    { id: 3, nombre: 'Diploma 3', evento: 'Seminario Web de Ciberseguridad', fecha: '20 Oct 2023', estado: '24/25' },
    { id: 4, nombre: 'Diploma 4', evento: 'Seminario Web de Ciberseguridad', fecha: '20 Oct 2023', estado: '24/25' },
    { id: 5, nombre: 'Diploma 5', evento: 'Seminario Web de Ciberseguridad', fecha: '20 Oct 2023', estado: '25/25' },
    { id: 6, nombre: 'Diploma 6', evento: 'Seminario Web de Ciberseguridad', fecha: '30 Oct 2023', estado: '11/25' }
  ]

  const getStatusColor = (estado) => {
    const [current, total] = estado.split('/').map(Number)
    if (current === total) return 'bg-success bg-opacity-10 text-success'
    if (current / total > 0.8) return 'bg-success bg-opacity-10 text-success'
    return 'bg-success bg-opacity-10 text-success' // In the screenshot all badges are green
  }

  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-3">
        <div>
          <h2 className="fw-bold mb-1">Gestión de Diplomas</h2>
        </div>
        <button
          className="btn btn-primary rounded-pill d-flex align-items-center gap-2 flex-shrink-0 px-4"
          data-bs-toggle="modal"
          data-bs-target="#crearDiplomaModal"
        >
          <i className="bi bi-plus-lg"></i>
          Crear Nuevo Certificado
        </button>
      </div>

      {/* Tarjetas de Estadísticas */}
      <div className="row g-3 mb-4">
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-4 h-100 p-3">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div className="bg-primary bg-opacity-10 text-primary rounded pt-1 px-2 pb-1 d-flex align-items-center justify-content-center">
                  <i className="bi bi-file-earmark-check"></i>
                </div>
                <span className="badge bg-success bg-opacity-10 text-success rounded-pill fw-semibold">+12%</span>
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Total de Certificaciones</div>
              <h3 className="fw-bold mb-0">1,250</h3>
            </div>
          </div>
        </div>
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-4 h-100 p-3">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div className="bg-success bg-opacity-10 text-success rounded pt-1 px-2 pb-1 d-flex align-items-center justify-content-center">
                  <i className="bi bi-check-circle"></i>
                </div>
                <span className="badge bg-success bg-opacity-10 text-success rounded-pill fw-semibold">+5%</span>
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Certificados Entregados</div>
              <h3 className="fw-bold mb-0">980</h3>
            </div>
          </div>
        </div>
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-4 h-100 p-3">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div className="bg-danger bg-opacity-10 text-danger rounded pt-1 px-2 pb-1 d-flex align-items-center justify-content-center">
                  <i className="bi bi-clock-history"></i>
                </div>
                <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill fw-semibold">-4%</span>
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Certificados Pendientes</div>
              <h3 className="fw-bold mb-0">270</h3>
            </div>
          </div>
        </div>
      </div>

      {/* Panel de Tabla */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row justify-content-between gap-3 mb-4">
            <div className="input-group bg-light rounded-3 overflow-hidden" style={{ maxWidth: '400px', border: 'none' }}>
              <span className="input-group-text bg-transparent border-0 pe-1">
                <i className="bi bi-search text-secondary"></i>
              </span>
              <input
                type="text"
                className="form-control bg-transparent border-0 shadow-none small"
                placeholder="Buscar por certificado..."
                style={{ fontSize: '13px' }}
              />
            </div>
            <div className="d-flex gap-2">
              <select className="form-select border-light-subtle rounded-3 small text-secondary" style={{ width: '130px', fontSize: '13px' }}>
                <option value="">Mes</option>
              </select>
              <select className="form-select border-light-subtle rounded-3 small text-secondary" style={{ width: '150px', fontSize: '13px' }}>
                <option value="">Cuatrimestre</option>
              </select>
            </div>
          </div>

          <div className="table-responsive">
            <table className="table align-middle mb-0">
              <thead>
                <tr>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '11px', letterSpacing: '0.5px', paddingLeft: '1.5rem' }}>Nombre del Certificado</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Evento / Origen</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Fecha</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Estado</th>
                  <th className="text-uppercase text-secondary small fw-bold pb-3 border-0 border-bottom text-end pe-4" style={{ fontSize: '11px', letterSpacing: '0.5px' }}>Acciones</th>
                </tr>
              </thead>
              <tbody className="border-top-0">
                {diplomas.map((diploma) => (
                  <tr key={diploma.id}>
                    <td className="fw-bold small py-3 border-light ps-4">{diploma.nombre}</td>
                    <td className="small py-3 border-light">
                      <a href="#" className="text-primary text-decoration-none">{diploma.evento}</a>
                    </td>
                    <td className="small py-3 border-light text-secondary" style={{ width: '90px' }}>
                      <div className="lh-sm">
                        {diploma.fecha.split(' ')[0]} {diploma.fecha.split(' ')[1]}<br/>
                        {diploma.fecha.split(' ')[2]}
                      </div>
                    </td>
                    <td className="py-3 border-light">
                      <span className={`badge ${getStatusColor(diploma.estado)} rounded-pill px-3 py-1 fw-bold`} style={{ fontSize: '11px' }}>
                        {diploma.estado}
                      </span>
                    </td>
                    <td className="py-3 border-light text-end pe-4">
                      <div className="d-flex justify-content-end gap-3">
                        <Link to={`/admin/diploma/${diploma.id}`} className="btn btn-link text-secondary p-0 m-0" title="Ver">
                          <i className="bi bi-eye"></i>
                        </Link>
                        <button
                          className="btn btn-link text-primary p-0 m-0"
                          title="Editar"
                          data-bs-toggle="modal"
                          data-bs-target="#editarDiplomaModal"
                        >
                          <i className="bi bi-pencil"></i>
                        </button>
                        <button className="btn btn-link text-danger p-0 m-0" title="Eliminar">
                          <i className="bi bi-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="card-footer bg-transparent border-top p-3 d-flex flex-column flex-md-row justify-content-between align-items-center gap-3">
          <span className="text-secondary small px-3" style={{ fontSize: '12px' }}>
            Mostrando 1 a 6 de 1,250 certificados
          </span>
          <nav aria-label="Page navigation">
            <ul className="pagination pagination-sm mb-0 gap-1 pe-3">
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

      <CrearDiplomaModal />
      <EditarDiplomaModal />
    </DashboardLayout>
  )
}

export default AdminDiplomas
