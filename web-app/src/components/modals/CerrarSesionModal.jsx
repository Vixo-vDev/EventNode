function CerrarSesionModal() {
  return (
    <div className="modal fade" id="logoutModal" tabIndex="-1" aria-labelledby="logoutModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-sm">
        <div className="modal-content border-0 rounded-4 shadow">
          <div className="modal-body p-4 text-center">
            <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3"
              style={{ width: '48px', height: '48px' }}>
              <i className="bi bi-box-arrow-right text-primary fs-4"></i>
            </div>
            <h5 className="fw-bold mb-2">¿Cerrar sesión?</h5>
            <p className="text-secondary small mb-4">
              ¿Estás seguro de que deseas salir de la plataforma?
            </p>

            <div className="d-flex gap-2">
              <button
                type="button"
                className="btn btn-outline-secondary rounded-pill w-50"
                data-bs-dismiss="modal"
              >
                Cancelar
              </button>
              <button
                type="button"
                className="btn btn-primary rounded-pill w-50"
                data-bs-dismiss="modal"
              >
                Cerrar sesión
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CerrarSesionModal
