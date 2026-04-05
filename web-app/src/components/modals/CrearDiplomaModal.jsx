import { useState, useRef } from 'react'
import { toast } from 'react-toastify'
import { useTranslation } from '../../i18n/I18nContext'

function DiplomaPreview({ eventName, signerName, firmaPreview }) {
  return (
    <div
      style={{
        background: 'white',
        border: '2px solid #e0e7ff',
        borderRadius: '12px',
        padding: '28px 24px',
        textAlign: 'center',
        fontFamily: 'Georgia, serif',
        minHeight: '320px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '10px',
        boxShadow: '0 2px 12px rgba(47,111,237,0.07)',
      }}
    >
      <div style={{ fontSize: '9px', color: '#aaa', letterSpacing: '3px', textTransform: 'uppercase' }}>
        Certificado de Participación
      </div>
      <div style={{ fontSize: '12px', color: '#777', marginTop: '4px' }}>Se certifica que</div>
      <div
        style={{
          fontSize: '20px',
          fontWeight: 'bold',
          color: '#2F6FED',
          borderBottom: '1px solid #2F6FED',
          paddingBottom: '4px',
          minWidth: '180px',
        }}
      >
        Nombre del Participante
      </div>
      <div style={{ fontSize: '11px', color: '#777' }}>ha participado satisfactoriamente en</div>
      <div style={{ fontSize: '14px', fontWeight: '600', color: '#333' }}>
        {eventName || <span style={{ color: '#bbb', fontStyle: 'italic' }}>[ Selecciona un evento ]</span>}
      </div>

      <div style={{ marginTop: '16px', width: '140px' }}>
        {firmaPreview ? (
          <img
            src={firmaPreview}
            alt="firma"
            style={{ maxHeight: '55px', maxWidth: '140px', objectFit: 'contain' }}
          />
        ) : (
          <div
            style={{
              height: '40px',
              borderBottom: '1px dashed #ccc',
              display: 'flex',
              alignItems: 'flex-end',
              justifyContent: 'center',
              paddingBottom: '4px',
            }}
          >
            <span style={{ fontSize: '9px', color: '#ccc', fontStyle: 'italic' }}>[ imagen de firma ]</span>
          </div>
        )}
        <div style={{ borderTop: firmaPreview ? '1px solid #ddd' : 'none', paddingTop: '4px', fontSize: '11px', color: '#555', marginTop: '4px' }}>
          {signerName || <span style={{ color: '#bbb', fontStyle: 'italic' }}>[ nombre del firmante ]</span>}
        </div>
      </div>

      <div style={{ fontSize: '9px', color: '#ccc', marginTop: '8px', fontStyle: 'italic' }}>
        * Diseño final basado en la plantilla JRXML
      </div>
    </div>
  )
}

function CrearDiplomaModal({ eventos = [], formData = {}, onChange, onSubmit, isLoading }) {
  const { t } = useTranslation()
  const [pdfFile, setPdfFile] = useState(null)
  const [pdfPreview, setPdfPreview] = useState(null)
  const [firmaFile, setFirmaFile] = useState(null)
  const [firmaPreview, setFirmaPreview] = useState(null)
  const [errors, setErrors] = useState({})
  const pdfInputRef = useRef(null)
  const firmaInputRef = useRef(null)
  const closeBtnRef = useRef(null)

  const selectedEvento = eventos.find(e => String(e.idEvento) === String(formData.idEvento))

  const clearError = (field) => {
    if (errors[field]) setErrors(prev => { const e = { ...prev }; delete e[field]; return e })
  }

  const handlePdfChange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    if (!file.name.endsWith('.jrxml')) {
      toast.warning('Solo se permiten plantillas Jasper (.jrxml)')
      return
    }
    if (file.size > 15 * 1024 * 1024) {
      toast.warning('El archivo no debe superar 15MB')
      return
    }
    setPdfFile(file)
    setPdfPreview(file.name)
    clearError('plantillaPdf')
    const reader = new FileReader()
    reader.onload = () => onChange({ target: { name: 'plantillaPdf', value: reader.result } })
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
    clearError('firmaImagen')
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
    if (file) handlePdfChange({ target: { files: [file] } })
  }

  const handleFirmaDrop = (e) => {
    e.preventDefault()
    const file = e.dataTransfer.files[0]
    if (file) handleFirmaChange({ target: { files: [file] } })
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

  const handleSubmit = async () => {
    const newErrors = {}
    if (!formData.idEvento) newErrors.idEvento = t('diplomas.selectEvent')
    if (!formData.plantillaPdf) newErrors.plantillaPdf = t('diplomas.uploadPdfTemplate')
    if (!formData.firmaImagen) newErrors.firmaImagen = t('diplomas.uploadSignatureImage')
    setErrors(newErrors)
    if (Object.keys(newErrors).length > 0) return
    try {
      await onSubmit()
      closeBtnRef.current?.click()
    } catch {
      // El error ya fue mostrado por el padre
    }
  }

  return (
    <div className="modal fade" id="crearDiplomaModal" tabIndex="-1" aria-labelledby="crearDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-xl">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="crearDiplomaModalLabel">{t('createDiploma.title')}</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>{t('createDiploma.subtitle')}</p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label={t('common.close')} ref={closeBtnRef}></button>
          </div>

          <div className="modal-body p-0 bg-light">
            <div className="row g-0" style={{ minHeight: '480px' }}>
              {/* ── Formulario ── */}
              <div className="col-12 col-lg-7 p-4" style={{ maxHeight: '65vh', overflowY: 'auto' }}>
                {/* Evento */}
                <div className="bg-white rounded-3 p-4 mb-3 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-calendar-event text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('createDiploma.event')}</h6>
                  </div>
                  <select
                    className={`form-select text-secondary small ${errors.idEvento ? 'is-invalid' : ''}`}
                    style={{ fontSize: '13px' }}
                    name="idEvento"
                    value={formData.idEvento || ''}
                    onChange={(e) => { clearError('idEvento'); onChange(e) }}
                    required
                  >
                    <option value="">{t('createDiploma.selectEvent')}</option>
                    {eventos.map(e => (
                      <option key={e.idEvento} value={e.idEvento}>{e.nombre}</option>
                    ))}
                  </select>
                  {errors.idEvento && <div className="invalid-feedback">{errors.idEvento}</div>}
                </div>

                {/* Plantilla JRXML */}
                <div className={`bg-white rounded-3 p-4 mb-3 shadow-sm ${errors.plantillaPdf ? 'border border-danger border-opacity-50' : ''}`}>
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-file-earmark-code text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('createDiploma.pdfTemplate')}</h6>
                  </div>
                  <p className="text-secondary small mb-3" style={{ fontSize: '12px' }}>{t('createDiploma.pdfInstruction')}</p>
                  <input type="file" ref={pdfInputRef} accept=".jrxml" className="d-none" onChange={handlePdfChange} />
                  {!pdfPreview ? (
                    <div
                      className="border border-2 border-dashed rounded-3 p-4 text-center"
                      style={{ cursor: 'pointer', borderColor: errors.plantillaPdf ? '#dc3545' : '#dee2e6', backgroundColor: '#fafbfc' }}
                      onClick={() => pdfInputRef.current?.click()}
                      onDragOver={(e) => e.preventDefault()}
                      onDrop={handlePdfDrop}
                    >
                      <i className="bi bi-cloud-arrow-up text-primary fs-2 d-block mb-2"></i>
                      <p className="mb-1 fw-semibold small">{t('createDiploma.dragPdf')}</p>
                      <p className="text-secondary mb-0" style={{ fontSize: '12px' }}>{t('createDiploma.pdfLimit')}</p>
                    </div>
                  ) : (
                    <div className="d-flex align-items-center gap-3 p-3 bg-light rounded-3 border">
                      <div className="bg-primary bg-opacity-10 text-primary rounded d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px', minWidth: '40px' }}>
                        <i className="bi bi-file-earmark-code fs-5"></i>
                      </div>
                      <div className="flex-grow-1 overflow-hidden">
                        <p className="mb-0 fw-semibold small text-truncate">{pdfPreview}</p>
                        <p className="mb-0 text-secondary" style={{ fontSize: '11px' }}>{t('createDiploma.templateLoaded')}</p>
                      </div>
                      <button type="button" className="btn btn-sm btn-outline-danger rounded-circle" onClick={removePdf} style={{ width: '30px', height: '30px', padding: 0 }}>
                        <i className="bi bi-x"></i>
                      </button>
                    </div>
                  )}
                  {errors.plantillaPdf && <div className="text-danger mt-2" style={{ fontSize: '12px' }}><i className="bi bi-exclamation-circle me-1"></i>{errors.plantillaPdf}</div>}
                </div>

                {/* Firma imagen */}
                <div className={`bg-white rounded-3 p-4 mb-3 shadow-sm ${errors.firmaImagen ? 'border border-danger border-opacity-50' : ''}`}>
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-pen text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('createDiploma.signatureLabel')}</h6>
                  </div>
                  <p className="text-secondary small mb-3" style={{ fontSize: '12px' }}>{t('createDiploma.signatureInstruction')}</p>
                  <input type="file" ref={firmaInputRef} accept="image/png,image/jpeg,image/jpg" className="d-none" onChange={handleFirmaChange} />
                  {!firmaPreview ? (
                    <div
                      className="border border-2 border-dashed rounded-3 p-4 text-center"
                      style={{ cursor: 'pointer', borderColor: errors.firmaImagen ? '#dc3545' : '#dee2e6', backgroundColor: '#fafbfc' }}
                      onClick={() => firmaInputRef.current?.click()}
                      onDragOver={(e) => e.preventDefault()}
                      onDrop={handleFirmaDrop}
                    >
                      <i className="bi bi-image text-primary fs-2 d-block mb-2"></i>
                      <p className="mb-1 fw-semibold small">{t('createDiploma.dragSignature')}</p>
                      <p className="text-secondary mb-0" style={{ fontSize: '12px' }}>{t('createDiploma.signatureLimit')}</p>
                    </div>
                  ) : (
                    <div className="text-center">
                      <div className="d-inline-block position-relative mb-2">
                        <img
                          src={firmaPreview}
                          alt={t('createDiploma.signatureLabel')}
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
                      <p className="text-secondary mb-0" style={{ fontSize: '11px' }}>{t('createDiploma.signatureLoaded')}</p>
                    </div>
                  )}
                  {errors.firmaImagen && <div className="text-danger mt-2" style={{ fontSize: '12px' }}><i className="bi bi-exclamation-circle me-1"></i>{errors.firmaImagen}</div>}
                </div>

                {/* Nombre firmante */}
                <div className="bg-white rounded-3 p-4 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-person-badge text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('createDiploma.signerName')}</h6>
                  </div>
                  <input
                    type="text"
                    className="form-control small"
                    placeholder={t('createDiploma.signerPlaceholder')}
                    name="firma"
                    value={formData.firma || ''}
                    onChange={onChange}
                  />
                  <p className="text-secondary mt-2 mb-0" style={{ fontSize: '11px' }}>{t('createDiploma.signerHelp')}</p>
                </div>
              </div>

              {/* ── Previsualización ── */}
              <div
                className="col-12 col-lg-5 p-4 d-flex flex-column"
                style={{ borderLeft: '1px solid #f0f0f0', background: '#f8f9fb' }}
              >
                <div className="d-flex align-items-center gap-2 mb-3">
                  <i className="bi bi-eye text-primary"></i>
                  <h6 className="fw-bold mb-0 text-dark fs-6">Previsualización</h6>
                </div>
                <DiplomaPreview
                  eventName={selectedEvento?.nombre}
                  signerName={formData.firma}
                  firmaPreview={firmaPreview}
                />
                <p className="text-secondary text-center mt-3 mb-0" style={{ fontSize: '11px' }}>
                  La vista previa se actualiza conforme llenas el formulario.
                </p>
              </div>
            </div>
          </div>

          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-outline-secondary px-4 fw-semibold" data-bs-dismiss="modal" style={{ fontSize: '13px' }}>
              {t('common.cancel')}
            </button>
            <button
              type="button"
              className="btn btn-primary px-4 fw-semibold d-flex align-items-center gap-2"
              style={{ fontSize: '13px' }}
              onClick={handleSubmit}
              disabled={isLoading}
            >
              {isLoading ? (
                <><span className="spinner-border spinner-border-sm me-1" role="status"></span>{t('createDiploma.creating')}</>
              ) : (
                <><i className="bi bi-check2"></i>{t('createDiploma.save')}</>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CrearDiplomaModal
