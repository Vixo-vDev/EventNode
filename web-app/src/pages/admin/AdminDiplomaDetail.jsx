import { Link } from 'react-router-dom'
import diplomaMock from '../../assets/diploma_mock.png'

function AdminDiplomaDetail() {
  return (
    <div>
      <div className="d-flex align-items-center gap-3 mb-4">
        <Link to="/admin/diplomas" className="btn btn-light rounded-circle d-flex align-items-center justify-content-center p-0" style={{ width: '40px', height: '40px' }}>
          <i className="bi bi-arrow-left text-secondary fs-5"></i>
        </Link>
        <span className="fw-semibold small">Tech Summit 2023</span>
      </div>

      <div className="mb-4">
        <h2 className="fw-bold mb-1">Diploma</h2>
        <p className="text-secondary small mb-0">
          Gestionar, verificar y volver a emitir credenciales para los participantes
        </p>
      </div>

      {/* Visor del Diploma */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
          <span className="text-uppercase text-secondary fw-bold" style={{ fontSize: '11px', letterSpacing: '1px' }}>
            VISTA PREVIA
          </span>
          <div className="d-flex gap-2">
            <button className="btn btn-light btn-sm rounded-3 d-flex align-items-center justify-content-center text-secondary" style={{ width: '32px', height: '32px' }}>
              <i className="bi bi-zoom-in"></i>
            </button>
            <button className="btn btn-light btn-sm rounded-3 d-flex align-items-center justify-content-center text-secondary" style={{ width: '32px', height: '32px' }}>
              <i className="bi bi-arrows-fullscreen"></i>
            </button>
          </div>
        </div>
        <div className="card-body p-4 p-md-5 bg-light bg-opacity-50 d-flex justify-content-center">
          {/* El contenedor simula el papel o el cuadro del diploma con su sombra */}
          <div className="bg-white p-3 p-md-4 shadow-sm" style={{ maxWidth: '800px', width: '100%' }}>
            <img 
              src={diplomaMock} 
              alt="Diploma Johnathan D. Maxwell" 
              className="w-100 h-auto"
              style={{ objectFit: 'contain' }}
            />
          </div>
        </div>
      </div>

      {/* Acción Inferior */}
      <div className="d-flex justify-content-center">
        <button className="btn btn-primary rounded-3 px-5 py-2 fw-semibold d-flex align-items-center gap-2">
          <i className="bi bi-download"></i>
          Descargar PDF
        </button>
      </div>
    </div>
  )
}

export default AdminDiplomaDetail
