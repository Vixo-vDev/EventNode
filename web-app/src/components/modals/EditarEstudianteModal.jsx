function EditarEstudianteModal() {
  return (
    <div className="modal fade" id="editarEstudianteModal" tabIndex="-1" aria-labelledby="editarEstudianteModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 rounded-4 shadow bg-white">
          <div className="modal-header border-0 px-4 pt-4 pb-0">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="editarEstudianteModalLabel">Editar estudiante</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Actualizar el perfil y la información académica de este expediente estudiantil.
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          
          <div className="modal-body p-4 pt-4">
            <div className="row g-3">
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Nombres</label>
                <input type="text" className="form-control text-dark small" defaultValue="Julien" style={{ fontSize: '13px' }} />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Apellidos</label>
                <input type="text" className="form-control text-dark small" defaultValue="Martinez" style={{ fontSize: '13px' }} />
              </div>

              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Matricula</label>
                <input type="text" className="form-control text-secondary bg-light small border-light-subtle" defaultValue="20243ds01" readOnly style={{ fontSize: '13px' }} />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Email Institucional</label>
                <div className="input-group">
                  <span className="input-group-text bg-white border-end-0 text-secondary pe-2">
                    <i className="bi bi-envelope"></i>
                  </span>
                  <input type="email" className="form-control border-start-0 ps-0 text-dark small" defaultValue="j.martinez@eventnode.edu" style={{ fontSize: '13px' }} />
                </div>
              </div>

              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Edad</label>
                <input type="number" className="form-control text-dark small" defaultValue="21" style={{ fontSize: '13px' }} />
              </div>
              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Sexo</label>
                <select className="form-select text-dark small" style={{ fontSize: '13px' }}>
                  <option selected>Hombre</option>
                  <option>Mujer</option>
                  <option>Otro</option>
                </select>
              </div>
              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Cuatrimestre</label>
                <input type="number" className="form-control text-dark small" defaultValue="5" style={{ fontSize: '13px' }} />
              </div>
            </div>
          </div>
          
          <div className="modal-footer border-top-0 px-4 py-3 bg-light bg-opacity-50 rounded-bottom-4 d-flex justify-content-end gap-2 mt-2">
            <button type="button" className="btn btn-white border px-4 fw-semibold text-dark rounded-3" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              Cancelar
            </button>
            <button type="button" className="btn btn-primary px-4 fw-semibold rounded-3" style={{ fontSize: '13px' }}>
              Guardar cambios
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarEstudianteModal
