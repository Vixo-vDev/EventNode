import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import AdminSidebar from '../../components/AdminSidebar'

function AdminHome() {
  return (
    <DashboardLayout sidebar={<AdminSidebar />}>
      <h2 className="fw-bold mb-4">Panel</h2>

      <div className="row g-3 mb-4">
        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex justify-content-between align-items-start mb-2">
                <div className="rounded-2 bg-primary bg-opacity-10 d-flex align-items-center justify-content-center"
                  style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-people-fill text-primary"></i>
                </div>
                <span className="badge bg-success bg-opacity-10 text-success small">+5%</span>
              </div>
              <div className="text-secondary small mb-1">Ocurrencias</div>
              <div className="fw-bold fs-2">82%</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100">
            <div className="card-body p-3">
              <div className="d-flex justify-content-between align-items-start mb-2">
                <div className="rounded-2 bg-primary bg-opacity-10 d-flex align-items-center justify-content-center"
                  style={{ width: '36px', height: '36px' }}>
                  <i className="bi bi-calendar-check text-primary"></i>
                </div>
                <span className="badge bg-danger bg-opacity-10 text-danger small fw-semibold">LIVE</span>
              </div>
              <div className="text-secondary small mb-1">Total Active Events</div>
              <div className="fw-bold fs-2">24</div>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-4">
          <div className="card border-0 shadow-sm rounded-3 h-100 border-top border-3 border-primary">
            <div className="card-body p-3">
              <div className="text-uppercase text-secondary small fw-bold mb-2">Diplomas</div>
              <div className="d-flex justify-content-between align-items-end">
                <div>
                  <div className="fw-bold fs-3 mb-0">1,240</div>
                  <div className="text-secondary small">Issued</div>
                </div>
                <div className="text-end">
                  <div className="fw-bold fs-3 mb-0">42</div>
                  <div className="text-secondary small">Pending</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-3">
        <div className="card-body p-0">
          <div className="d-flex justify-content-between align-items-center p-3 pb-2">
            <h5 className="fw-bold mb-0">Detalles de Eventos</h5>
            <Link to="/admin/eventos" className="text-primary text-decoration-none small fw-semibold">
              Ver Todos
            </Link>
          </div>

          <div className="table-responsive">
            <table className="table table-hover mb-0 align-middle">
              <thead className="border-top">
                <tr>
                  <th className="text-uppercase text-secondary small fw-semibold ps-3 py-3" style={{ fontSize: '11px' }}>Nombre del Evento</th>
                  <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Fecha/Tiempo</th>
                  <th className="text-uppercase text-secondary small fw-semibold py-3" style={{ fontSize: '11px' }}>Estado</th>
                  <th className="text-uppercase text-secondary small fw-semibold pe-3 py-3" style={{ fontSize: '11px' }}>Capacidad</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small">Design Thinking Workshop</td>
                  <td className="py-3 text-secondary small">FEB 24, 09:00 AM</td>
                  <td className="py-3">
                    <span className="badge bg-success bg-opacity-10 text-success rounded-pill px-3">ACTIVO</span>
                  </td>
                  <td className="pe-3 py-3">
                    <div className="d-flex align-items-center gap-2">
                      <div className="progress flex-grow-1" style={{ height: '6px' }}>
                        <div className="progress-bar bg-primary" style={{ width: '75%' }}></div>
                      </div>
                      <span className="text-secondary small">75%</span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small">Blockchain Governance</td>
                  <td className="py-3 text-secondary small">FEB 25, 11:30 AM</td>
                  <td className="py-3">
                    <span className="badge bg-success bg-opacity-10 text-success rounded-pill px-3">ACTIVO</span>
                  </td>
                  <td className="pe-3 py-3">
                    <div className="d-flex align-items-center gap-2">
                      <div className="progress flex-grow-1" style={{ height: '6px' }}>
                        <div className="progress-bar bg-primary" style={{ width: '45%' }}></div>
                      </div>
                      <span className="text-secondary small">45%</span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
                <tr>
                  <td className="ps-3 py-3 fw-semibold small text-primary">Marketing Masterclass</td>
                  <td className="py-3 text-secondary small">FEB 26, 02:00 PM</td>
                  <td className="py-3">
                    <span className="badge bg-danger bg-opacity-10 text-danger rounded-pill px-3">CANCELLED</span>
                  </td>
                  <td className="pe-3 py-3 text-secondary small">0%</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default AdminHome
