function CrearAdministradorModal({ formData, error, isLoading, onChange, onSubmit }) {
  return (
    <div className="modal fade" id="crearAdminModal" tabIndex="-1" aria-labelledby="crearAdminModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 rounded-4 shadow bg-white">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="crearAdminModalLabel">Crear Administrador</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Registrar una nueva cuenta de administrador en la plataforma.
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>

          <form onSubmit={onSubmit}>
            <div className="modal-body p-4 pt-4">
              {error && (
                <div className="alert alert-danger py-2 small" role="alert">
                  {error}
                </div>
              )}
              <div className="row g-3">
                <div className="col-12">
                  <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Nombre</label>
                  <input
                    type="text"
                    name="nombre"
                    className="form-control text-dark small"
                    placeholder="Nombre(s)"
                    value={formData.nombre}
                    onChange={onChange}
                    required
                    style={{ fontSize: '13px' }}
                  />
                </div>
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Apellido Paterno</label>
                  <input
                    type="text"
                    name="apellidoPaterno"
                    className="form-control text-dark small"
                    placeholder="Apellido paterno"
                    value={formData.apellidoPaterno}
                    onChange={onChange}
                    required
                    style={{ fontSize: '13px' }}
                  />
                </div>
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Apellido Materno</label>
                  <input
                    type="text"
                    name="apellidoMaterno"
                    className="form-control text-dark small"
                    placeholder="Apellido materno (opcional)"
                    value={formData.apellidoMaterno}
                    onChange={onChange}
                    style={{ fontSize: '13px' }}
                  />
                </div>
                <div className="col-12">
                  <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Correo Electrónico</label>
                  <div className="input-group">
                    <span className="input-group-text bg-white border-end-0 text-secondary pe-2">
                      <i className="bi bi-envelope"></i>
                    </span>
                    <input
                      type="email"
                      name="correo"
                      className="form-control border-start-0 ps-0 text-dark small"
                      placeholder="correo@ejemplo.com"
                      value={formData.correo}
                      onChange={onChange}
                      required
                      style={{ fontSize: '13px' }}
                    />
                  </div>
                </div>
                <div className="col-12">
                  <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Contraseña</label>
                  <div className="input-group">
                    <span className="input-group-text bg-white border-end-0 text-secondary pe-2">
                      <i className="bi bi-lock"></i>
                    </span>
                    <input
                      type="password"
                      name="password"
                      className="form-control border-start-0 ps-0 text-dark small"
                      placeholder="Mínimo 8 caracteres, 1 mayúscula, 1 número, 1 especial"
                      value={formData.password}
                      onChange={onChange}
                      required
                      style={{ fontSize: '13px' }}
                    />
                  </div>
                </div>
              </div>
            </div>

            <div className="modal-footer border-top-0 px-4 py-3 bg-light bg-opacity-50 rounded-bottom-4 d-flex justify-content-end gap-2 mt-2">
              <button
                type="button"
                className="btn btn-white border px-4 fw-semibold text-dark rounded-3"
                data-bs-dismiss="modal"
                style={{ fontSize: '13px' }}
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="btn btn-primary px-4 fw-semibold rounded-3"
                disabled={isLoading}
                style={{ fontSize: '13px' }}
              >
                {isLoading ? (
                  <>
                    <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                    Creando...
                  </>
                ) : (
                  'Crear Administrador'
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  )
}

export default CrearAdministradorModal
