function EditarDiplomaModal() {
  return (
    <div className="modal fade" id="editarDiplomaModal" tabIndex="-1" aria-labelledby="editarDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="editarDiplomaModalLabel">Editar Diploma</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Configure details and design for event certificates
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>
          
          <div className="modal-body p-4 bg-light">
            {/* Event Information Section */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-info-circle text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Información del evento</h6>
              </div>
              <div className="row g-3">
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Nombre del evento</label>
                  <select className="form-select text-dark small" style={{ fontSize: '13px' }}>
                    <option selected>Global Tech Summit 2024</option>
                    <option>Tech Summit 2023</option>
                    <option>Masterclass en diseño de IU</option>
                  </select>
                </div>
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Fecha del evento</label>
                  <input type="text" className="form-control text-dark small" defaultValue="10/24/2024" style={{ fontSize: '13px' }} />
                </div>
              </div>
            </div>

            {/* Firma Electronica Section */}
            <div className="bg-white rounded-3 p-4 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-magic text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Firma de asignación</h6>
              </div>
              <div className="border border-primary border-opacity-25 rounded-3 d-flex flex-column py-3 px-3 text-center" style={{ borderStyle: 'dashed !important', backgroundColor: '#f8faff' }}>
                
                {/* Uploaded File Item */}
                <div className="d-flex align-items-center justify-content-between bg-light rounded-3 p-2 mb-3 border w-100 mx-auto" style={{ maxWidth: '600px' }}>
                  <div className="d-flex align-items-center gap-3">
                    <div className="bg-primary bg-opacity-10 text-primary rounded p-2 d-flex align-items-center justify-content-center">
                      <i className="bi bi-file-earmark-image"></i>
                    </div>
                    <div className="text-start lh-sm">
                      <div className="fw-semibold text-dark mb-1" style={{ fontSize: '12px' }}>Firma_Oficial.png</div>
                      <div className="text-secondary" style={{ fontSize: '10px' }}>Subido el 12, 2024</div>
                    </div>
                  </div>
                  <button className="btn btn-link text-danger p-0 text-decoration-none fw-semibold" style={{ fontSize: '11px' }}>
                    Quitar
                  </button>
                </div>

                <div className="text-secondary" style={{ fontSize: '11px', lineHeight: '1.5' }}>
                  Haga clic o arrastre para reemplazar la firma (PNG,<br/>JPG, max. 2 MB)
                </div>
              </div>
            </div>

          </div>
          
          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-outline-secondary px-4 fw-semibold border-light-subtle text-dark" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              Cancel
            </button>
            <button type="button" className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2" style={{ fontSize: '13px' }}>
              <i className="bi bi-check2 border border-white rounded-circle px-1" style={{ fontSize: '10px' }}></i>
              Save Changes
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarDiplomaModal
