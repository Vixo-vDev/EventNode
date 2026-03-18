import { useState, useRef } from 'react'
import { toast } from 'react-toastify'

function CrearDiplomaModal({ eventos = [], formData = {}, onChange, onSubmit, isLoading }) {
  const [pdfFile, setPdfFile] = useState(null)
  const [pdfPreview, setPdfPreview] = useState(null)
  const [firmaFile, setFirmaFile] = useState(null)
  const [firmaPreview, setFirmaPreview] = useState(null)
  const pdfInputRef = useRef(null)
  const firmaInputRef = useRef(null)

  const handlePdfChange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    if (file.type !== 'application/pdf') {
      toast.warning('Solo se permiten archivos PDF')
      return
    }
    if (file.size > 15 * 1024 * 1024) {
      toast.warning('El archivo no debe superar 15MB')
      return
    }
    setPdfFile(file)
    setPdfPreview(file.name)

    const reader = new FileReader()
    reader.onload = () => {
      onChange({ target: { name: 'plantillaPdf', value: reader.result } })
    }
    reader.readAsDataURL(file)
  }

  const handleFirmaChange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    if (!file.type.startsWith('image/')) {
      toast.warning('Solo se permiten imágenes (PNG, JPG)')
      return
    }
    if (file.size > 5 * 1024 * 1024) {
      toast.warning('La imagen no debe superar 5MB')
      return
    }
    setFirmaFile(file)

    const reader = new FileReader()
    reader.onload = () => {
      setFirmaPreview(reader.result)
      onChange({ target: { name: 'firmaImagen', value: reader.result } })
    }
    reader.readAsDataURL(file)
  }

  const handlePdfDrop = (e) => {
    e.preventDefault()
    const file = e.dataTransfer.files[0]
    if (file) {
      const fakeEvent = { target: { files: [file] } }
      handlePdfChange(fakeEvent)
    }
  }

  const handleFirmaDrop = (e) => {
    e.preventDefault()
    const file = e.dataTransfer.files[0]
    if (file) {
      const fakeEvent = { target: { files: [file] } }
      handleFirmaChange(fakeEvent)
    }
  }

  const removePdf = () => {
    setPdfFile(null)
    setPdfPreview(null)
    onChange({ target: { name: 'plantillaPdf', value: '' } })
    if (pdfInputRef.current) pdfInputRef.current.value = ''
  }

  const removeFirma = () => {
    setFirmaFile(null)
    setFirmaPreview(null)
    onChange({ target: { name: 'firmaImagen', value: '' } })
    if (firmaInputRef.current) firmaInputRef.current.value = ''
  }

  return (
    <div className="modal fade" id="crearDiplomaModal" tabIndex="-1" aria-labelledby="crearDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="crearDiplomaModalLabel">Crear Nuevo Diploma</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>
                Sube la plantilla PDF, selecciona el evento y agrega la firma.
              </p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label="Cerrar"></button>
          </div>

          <div className="modal-body p-4 bg-light" style={{ maxHeight: '65vh', overflowY: 'auto' }}>
            {/* Evento Selection */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-calendar-event text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Evento</h6>
              </div>
              <select
                className="form-select text-secondary small"
                style={{ fontSize: '13px' }}
                name="idEvento"
                value={formData.idEvento || ''}
                onChange={onChange}
                required
              >
                <option value="">Seleccionar evento...</option>
                {eventos.map(e => (
                  <option key={e.idEvento} value={e.idEvento}>{e.nombre}</option>
                ))}
              </select>
            </div>

            {/* PDF Template Upload */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-file-earmark-pdf text-danger"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Plantilla PDF</h6>
              </div>
              <p className="text-secondary small mb-3" style={{ fontSize: '12px' }}>
                Sube el diseño base del diploma en PDF. El nombre del estudiante se colocará centrado automáticamente.
              </p>

              <input
                type="file"
                ref={pdfInputRef}
                accept="application/pdf"
                className="d-none"
                onChange={handlePdfChange}
              />

              {!pdfPreview ? (
                <div
                  className="border border-2 border-dashed rounded-3 p-4 text-center"
                  style={{ cursor: 'pointer', borderColor: '#dee2e6', backgroundColor: '#fafbfc' }}
                  onClick={() => pdfInputRef.current?.click()}
                  onDragOver={(e) => e.preventDefault()}
                  onDrop={handlePdfDrop}
                >
                  <i className="bi bi-cloud-arrow-up text-primary fs-2 d-block mb-2"></i>
                  <p className="mb-1 fw-semibold small">Arrastra tu archivo PDF aquí</p>
                  <p className="text-secondary mb-0" style={{ fontSize: '12px' }}>o haz clic para seleccionar (máx. 15MB)</p>
                </div>
              ) : (
                <div className="d-flex align-items-center gap-3 p-3 bg-light rounded-3 border">
                  <div className="bg-danger bg-opacity-10 text-danger rounded d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px', minWidth: '40px' }}>
                    <i className="bi bi-file-earmark-pdf fs-5"></i>
                  </div>
                  <div className="flex-grow-1 overflow-hidden">
                    <p className="mb-0 fw-semibold small text-truncate">{pdfPreview}</p>
                    <p className="mb-0 text-secondary" style={{ fontSize: '11px' }}>Plantilla cargada</p>
                  </div>
                  <button type="button" className="btn btn-sm btn-outline-danger rounded-circle" onClick={removePdf} style={{ width: '30px', height: '30px', padding: 0 }}>
                    <i className="bi bi-x"></i>
                  </button>
                </div>
              )}
            </div>

            {/* Signature Upload */}
            <div className="bg-white rounded-3 p-4 mb-3 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-pen text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Firma</h6>
              </div>
              <p className="text-secondary small mb-3" style={{ fontSize: '12px' }}>
                Sube una imagen de la firma que aparecerá en el diploma (PNG o JPG, fondo transparente recomendado).
              </p>

              <input
                type="file"
                ref={firmaInputRef}
                accept="image/png,image/jpeg,image/jpg"
                className="d-none"
                onChange={handleFirmaChange}
              />

              {!firmaPreview ? (
                <div
                  className="border border-2 border-dashed rounded-3 p-4 text-center"
                  style={{ cursor: 'pointer', borderColor: '#dee2e6', backgroundColor: '#fafbfc' }}
                  onClick={() => firmaInputRef.current?.click()}
                  onDragOver={(e) => e.preventDefault()}
                  onDrop={handleFirmaDrop}
                >
                  <i className="bi bi-image text-primary fs-2 d-block mb-2"></i>
                  <p className="mb-1 fw-semibold small">Arrastra la imagen de firma aquí</p>
                  <p className="text-secondary mb-0" style={{ fontSize: '12px' }}>PNG o JPG (máx. 5MB)</p>
                </div>
              ) : (
                <div className="text-center">
                  <div className="d-inline-block position-relative mb-2">
                    <img
                      src={firmaPreview}
                      alt="Firma"
                      className="border rounded-3"
                      style={{ maxHeight: '100px', maxWidth: '100%', objectFit: 'contain', backgroundColor: '#f8f9fa' }}
                    />
                    <button
                      type="button"
                      className="btn btn-sm btn-danger rounded-circle position-absolute"
                      style={{ top: '-8px', right: '-8px', width: '24px', height: '24px', padding: 0, fontSize: '12px' }}
                      onClick={removeFirma}
                    >
                      <i className="bi bi-x"></i>
                    </button>
                  </div>
                  <p className="text-secondary mb-0" style={{ fontSize: '11px' }}>Firma cargada</p>
                </div>
              )}
            </div>

            {/* Signer Name */}
            <div className="bg-white rounded-3 p-4 shadow-sm border-0">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i className="bi bi-person-badge text-primary"></i>
                <h6 className="fw-bold mb-0 text-dark fs-6">Nombre del firmante</h6>
              </div>
              <input
                type="text"
                className="form-control small"
                placeholder="Ej: Mtro. Juan Pérez García"
                name="firma"
                value={formData.firma || ''}
                onChange={onChange}
              />
              <p className="text-secondary mt-2 mb-0" style={{ fontSize: '11px' }}>
                Este nombre aparecerá debajo de la firma en el diploma.
              </p>
            </div>
          </div>

          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-outline-secondary px-4 fw-semibold" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              Cancelar
            </button>
            <button
              type="button"
              className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2"
              style={{ fontSize: '13px' }}
              onClick={onSubmit}
              disabled={isLoading || !formData.idEvento || !formData.plantillaPdf || !formData.firmaImagen}
            >
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
