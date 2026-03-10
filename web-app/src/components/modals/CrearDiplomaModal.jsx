function CrearDiplomaModal() {
  return (
    <div className="modal fade" id="crearDiplomaModal" tabIndex="-1" aria-labelledby="crearDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="crearDiplomaModalLabel">Create New Diploma</h5>
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
                <h6 className="fw-bold mb-0 text-dark fs-6">Event Information</h6>
              </div>
              <div className="row g-3">
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Nombre del Evento</label>
                  <select className="form-select text-secondary small" style={{ fontSize: '13px' }}>
                    <option selected>Nombre del evento</option>
                    <option>Tech Summit 2023</option>
                    <option>Masterclass en diseño de IU</option>
                  </select>
                </div>
                <div className="col-12 col-md-6">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Fecha del Evento</label>
                  <input type="text" className="form-control text-secondary small placeholder-secondary" placeholder="mm/dd/yyyy" style={{ fontSize: '13px' }} />
                </div>
              </div>
            </div>

            {/* Plantilla de Diploma Section */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-magic text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Plantilla de Diploma</h6>
              </div>
              <div className="border border-primary border-opacity-25 rounded-3 d-flex flex-column align-items-center justify-content-center py-4 px-3 text-center" style={{ borderStyle: 'dashed !important', backgroundColor: '#f8faff' }}>
                <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                  <i className="bi bi-file-earmark-image"></i>
                </div>
                <div className="fs-6 fw-semibold text-dark mb-1 d-flex gap-1" style={{ fontSize: '13px' }}>
                  Drop your template here, or <span className="text-primary text-decoration-none" style={{ cursor: 'pointer' }}>browse</span>
                </div>
                <div className="text-secondary" style={{ fontSize: '11px' }}>Supports PNG, JPG (Max 2MB)</div>
              </div>
            </div>

            {/* Firma Electronica Section */}
            <div className="bg-white rounded-3 p-4 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-pen text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Firma Electronica</h6>
              </div>
              <div className="border border-primary border-opacity-25 rounded-3 d-flex flex-column align-items-center justify-content-center py-4 px-3 text-center" style={{ borderStyle: 'dashed !important', backgroundColor: '#f8faff' }}>
                <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center mb-2" style={{ width: '40px', height: '40px' }}>
                  <i className="bi bi-file-earmark-font"></i>
                </div>
                <div className="fs-6 fw-semibold text-dark mb-1 d-flex gap-1" style={{ fontSize: '13px' }}>
                  Drop your signature here, or <span className="text-primary text-decoration-none" style={{ cursor: 'pointer' }}>browse</span>
                </div>
                <div className="text-secondary" style={{ fontSize: '11px' }}>Supports PNG, JPG (Max 2MB)</div>
              </div>
            </div>

          </div>
          
          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-outline-secondary px-4 fw-semibold" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              Cancel
            </button>
            <button type="button" className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2" style={{ fontSize: '13px' }}>
              <i className="bi bi-check2"></i>
              Save and Generate
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CrearDiplomaModal
