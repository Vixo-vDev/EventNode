import CrearDiplomaModal from '../../components/modals/CrearDiplomaModal'
import EditarDiplomaModal from '../../components/modals/EditarDiplomaModal'

function AdminDiplomas() {
  return (
    <div>
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
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Total de Certificaciones</div>
              <h3 className="fw-bold mb-0">0</h3>
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
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Certificados Entregados</div>
              <h3 className="fw-bold mb-0">0</h3>
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
              </div>
              <div className="text-secondary small mb-1" style={{ fontSize: '13px' }}>Certificados Pendientes</div>
              <h3 className="fw-bold mb-0">0</h3>
            </div>
          </div>
        </div>
      </div>

      {/* Estado vacío */}
      <div className="card border-0 shadow-sm rounded-4 mb-4">
        <div className="card-body text-center py-5">
          <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
            <i className="bi bi-award text-primary fs-3"></i>
          </div>
          <h6 className="fw-bold mb-1">No hay diplomas registrados</h6>
          <p className="text-secondary small mb-2">
            Crea eventos y registra asistencias para poder emitir diplomas y certificaciones.
          </p>
          <button
            className="btn btn-primary btn-sm rounded-pill px-4"
            data-bs-toggle="modal"
            data-bs-target="#crearDiplomaModal"
          >
            Crear Primer Certificado
          </button>
        </div>
      </div>

      <CrearDiplomaModal />
      <EditarDiplomaModal />
    </div>
  )
}

export default AdminDiplomas
