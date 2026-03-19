import { useState, useEffect, useRef } from 'react'
import { toast } from 'react-toastify'

function EditarDiplomaModal({ diploma, onSubmit, isLoading }) {
  const [formData, setFormData] = useState({
    firma: '',
    diseno: 'Personalizado',
    plantillaPdf: '',
    firmaImagen: ''
  })
  const pdfInputRef = useRef(null)
  const firmaInputRef = useRef(null)
  const [pdfFileName, setPdfFileName] = useState('')
  const [firmaFileName, setFirmaFileName] = useState('')

  useEffect(() => {
    if (diploma) {
      setFormData({
        firma: diploma.firma || '',
        diseno: diploma.diseno || 'Personalizado',
        plantillaPdf: '',
        firmaImagen: ''
      })
      setPdfFileName('')
      setFirmaFileName('')
    }
  }, [diploma])

  // Fix aria-hidden focus issue: blur active element when modal hides
  useEffect(() => {
    const modalEl = document.getElementById('editarDiplomaModal')
    if (!modalEl) return
    const handleHide = () => {
      if (document.activeElement && modalEl.contains(document.activeElement)) {
        document.activeElement.blur()
      }
    }
    modalEl.addEventListener('hide.bs.modal', handleHide)
    return () => modalEl.removeEventListener('hide.bs.modal', handleHide)
  }, [])

  const handleFileChange = (e, field) => {
    const file = e.target.files[0]
    if (!file) return

    if (field === 'plantillaPdf' && file.type !== 'application/pdf') {
      toast.warning('Solo se permiten archivos PDF')
      return
    }

    if (field === 'firmaImagen' && !file.type.startsWith('image/')) {
      toast.warning('Solo se permiten imágenes')
      return
    }

    if (file.size > 5 * 1024 * 1024) {
      toast.warning('El archivo no debe exceder 5 MB')
      return
    }

    const reader = new FileReader()
    reader.onload = () => {
      setFormData(prev => ({ ...prev, [field]: reader.result }))
      if (field === 'plantillaPdf') setPdfFileName(file.name)
      if (field === 'firmaImagen') setFirmaFileName(file.name)
    }
    reader.readAsDataURL(file)
  }

  const handleSubmit = () => {
    const datos = {}
    if (formData.firma) datos.firma = formData.firma
    if (formData.diseno) datos.diseno = formData.diseno
    if (formData.plantillaPdf) datos.plantillaPdf = formData.plantillaPdf
    if (formData.firmaImagen) datos.firmaImagen = formData.firmaImagen

    if (Object.keys(datos).length === 0) {
      toast.warning('No hay cambios para guardar')
      return
    }

    onSubmit(diploma.idDiploma, datos)
  }

  return (
    <div className="modal fade" id="editarDiplomaModal" tabIndex="-1" aria-labelledby="editarDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="editarDiplomaModalLabel">Editar Diploma</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Modifica los datos del diploma. Los destinatarios existentes recibirán la versión actualizada por correo.
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>

          <div className="modal-body p-4 bg-light">
            {/* Event info (read-only) */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-info-circle text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Información del evento</h6>
              </div>
              <div className="row g-3">
                <div className="col-12">
                  <label className="form-label text-dark fw-semibold small mb-2" style={{ fontSize: '12px' }}>Evento</label>
                  <input
                    type="text"
                    className="form-control bg-light text-dark small"
                    value={diploma?.nombreEvento || ''}
                    disabled
                    style={{ fontSize: '13px' }}
                  />
                </div>
              </div>
            </div>

            {/* Firma name */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-pen text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Nombre del firmante</h6>
              </div>
              <input
                type="text"
                className="form-control text-dark small"
                placeholder="Nombre de quien firma el diploma"
                value={formData.firma}
                onChange={(e) => setFormData(prev => ({ ...prev, firma: e.target.value }))}
                style={{ fontSize: '13px' }}
              />
            </div>

            {/* Plantilla PDF */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-file-pdf text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Plantilla PDF</h6>
              </div>
              <div
                className="border border-primary border-opacity-25 rounded-3 d-flex flex-column py-3 px-3 text-center"
                style={{ borderStyle: 'dashed', backgroundColor: '#f8faff', cursor: 'pointer' }}
                onClick={() => pdfInputRef.current?.click()}
              >
                {pdfFileName ? (
                  <div className="d-flex align-items-center justify-content-between bg-light rounded-3 p-2 border w-100 mx-auto" style={{ maxWidth: '600px' }}>
                    <div className="d-flex align-items-center gap-3">
                      <div className="bg-primary bg-opacity-10 text-primary rounded p-2 d-flex align-items-center justify-content-center">
                        <i className="bi bi-file-earmark-pdf"></i>
                      </div>
                      <div className="text-start lh-sm">
                        <div className="fw-semibold text-dark mb-1" style={{ fontSize: '12px' }}>{pdfFileName}</div>
                        <div className="text-secondary" style={{ fontSize: '10px' }}>Nueva plantilla seleccionada</div>
                      </div>
                    </div>
                    <button
                      className="btn btn-link text-danger p-0 text-decoration-none fw-semibold"
                      style={{ fontSize: '11px' }}
                      onClick={(e) => { e.stopPropagation(); setFormData(prev => ({ ...prev, plantillaPdf: '' })); setPdfFileName('') }}
                    >
                      Quitar
                    </button>
                  </div>
                ) : (
                  <>
                    <i className="bi bi-cloud-arrow-up text-primary fs-3 mb-2"></i>
                    <div className="text-secondary" style={{ fontSize: '11px', lineHeight: '1.5' }}>
                      {diploma?.tienePlantilla
                        ? 'Haga clic para reemplazar la plantilla PDF actual'
                        : 'Haga clic para subir una plantilla PDF'}
                      <br />(PDF, max. 5 MB)
                    </div>
                  </>
                )}
                <input
                  ref={pdfInputRef}
                  type="file"
                  className="d-none"
                  accept="application/pdf"
                  onChange={(e) => handleFileChange(e, 'plantillaPdf')}
                />
              </div>
            </div>

            {/* Firma imagen */}
            <div className="bg-white rounded-3 p-4 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-image text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Imagen de firma</h6>
              </div>
              <div
                className="border border-primary border-opacity-25 rounded-3 d-flex flex-column py-3 px-3 text-center"
                style={{ borderStyle: 'dashed', backgroundColor: '#f8faff', cursor: 'pointer' }}
                onClick={() => firmaInputRef.current?.click()}
              >
                {firmaFileName ? (
                  <div className="d-flex align-items-center justify-content-between bg-light rounded-3 p-2 border w-100 mx-auto" style={{ maxWidth: '600px' }}>
                    <div className="d-flex align-items-center gap-3">
                      <div className="bg-primary bg-opacity-10 text-primary rounded p-2 d-flex align-items-center justify-content-center">
                        <i className="bi bi-file-earmark-image"></i>
                      </div>
                      <div className="text-start lh-sm">
                        <div className="fw-semibold text-dark mb-1" style={{ fontSize: '12px' }}>{firmaFileName}</div>
                        <div className="text-secondary" style={{ fontSize: '10px' }}>Nueva firma seleccionada</div>
                      </div>
                    </div>
                    <button
                      className="btn btn-link text-danger p-0 text-decoration-none fw-semibold"
                      style={{ fontSize: '11px' }}
                      onClick={(e) => { e.stopPropagation(); setFormData(prev => ({ ...prev, firmaImagen: '' })); setFirmaFileName('') }}
                    >
                      Quitar
                    </button>
                  </div>
                ) : (
                  <>
                    <i className="bi bi-cloud-arrow-up text-primary fs-3 mb-2"></i>
                    <div className="text-secondary" style={{ fontSize: '11px', lineHeight: '1.5' }}>
                      {diploma?.tieneFirma
                        ? 'Haga clic para reemplazar la imagen de firma actual'
                        : 'Haga clic para subir una imagen de firma'}
                      <br />(PNG, JPG, max. 5 MB)
                    </div>
                  </>
                )}
                <input
                  ref={firmaInputRef}
                  type="file"
                  className="d-none"
                  accept="image/*"
                  onChange={(e) => handleFileChange(e, 'firmaImagen')}
                />
              </div>
            </div>

            {diploma && (diploma.totalEmitidos > 0) && (
              <div className="alert alert-warning mt-3 mb-0 d-flex align-items-center gap-2" style={{ fontSize: '13px' }}>
                <i className="bi bi-exclamation-triangle"></i>
                <span>Este diploma ya fue emitido a <strong>{diploma.totalEmitidos}</strong> persona(s). Al guardar, se les enviará un correo con la versión actualizada.</span>
              </div>
            )}
          </div>

          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button
              type="button"
              className="btn btn-outline-secondary px-4 fw-semibold border-light-subtle text-dark"
              data-bs-dismiss="modal"
              style={{ fontSize: '13px' }}
            >
              Cancelar
            </button>
            <button
              type="button"
              className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2"
              style={{ fontSize: '13px' }}
              onClick={handleSubmit}
              disabled={isLoading}
            >
              {isLoading ? (
                <><span className="spinner-border spinner-border-sm me-1" role="status"></span>Guardando...</>
              ) : (
                <><i className="bi bi-check2 border border-white rounded-circle px-1" style={{ fontSize: '10px' }}></i>Guardar Cambios</>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarDiplomaModal
