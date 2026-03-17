function CrearDiplomaModal({ eventos = [], formData = {}, onChange, onSubmit, isLoading }) {
  return (
    <div className="modal fade" id="crearDiplomaModal" tabIndex="-1" aria-labelledby="crearDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="crearDiplomaModalLabel">Crear Nuevo Diploma</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Configura los detalles y diseño del certificado para el evento.
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>

          <div className="modal-body p-4 bg-light">
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-info-circle text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Información del Evento</h6>
              </div>
              <div className="row g-3">
                <div className="col-12">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Evento *</label>
                  <select className="form-select text-secondary small" style={{ fontSize: '13px' }} name="idEvento" value={formData.idEvento || ''} onChange={onChange} required>
                    <option value="">Seleccionar evento...</option>
                    {eventos.map(e => (
                      <option key={e.idEvento} value={e.idEvento}>{e.nombre}</option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-palette text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Diseño de Diploma</h6>
              </div>
              <select className="form-select small" name="diseno" value={formData.diseno || 'Jasper Classic'} onChange={onChange}>
                <option value="Jasper Classic">Jasper Classic</option>
                <option value="Modern Blue">Modern Blue</option>
                <option value="Elegant Gold">Elegant Gold</option>
              </select>
            </div>

            <div className="bg-white rounded-3 p-4 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-pen text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Firma Electrónica</h6>
              </div>
              <input
                type="text"
                className="form-control small"
                placeholder="Nombre de quien firma..."
                name="firma"
                value={formData.firma || ''}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-outline-secondary px-4 fw-semibold" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              Cancelar
            </button>
            <button type="button" className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2" style={{ fontSize: '13px' }} onClick={onSubmit} disabled={isLoading}>
              {isLoading ? (
                <><span className="spinner-border spinner-border-sm me-1" role="status"></span>Creando...</>
              ) : (
                <><i className="bi bi-check2"></i>Guardar Diploma</>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CrearDiplomaModal
