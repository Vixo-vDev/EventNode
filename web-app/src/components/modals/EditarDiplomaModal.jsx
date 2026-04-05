import { useState, useEffect, useRef } from 'react'
import { toast } from 'react-toastify'
import { useTranslation } from '../../i18n/I18nContext'
import { diplomaService } from '../../services/diplomaService'

function DiplomaPreview({ eventName, signerName, firmaPreview, tieneFirma }) {
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
        {eventName || <span style={{ color: '#bbb', fontStyle: 'italic' }}>[ Sin evento ]</span>}
      </div>

      <div style={{ marginTop: '16px', width: '140px' }}>
        {firmaPreview ? (
          <img
            src={firmaPreview}
            alt="firma"
            style={{ maxHeight: '55px', maxWidth: '140px', objectFit: 'contain' }}
          />
        ) : tieneFirma ? (
          <div
            style={{
              height: '40px',
              borderBottom: '1px solid #ccc',
              display: 'flex',
              alignItems: 'flex-end',
              justifyContent: 'center',
              paddingBottom: '4px',
            }}
          >
            <span style={{ fontSize: '9px', color: '#999', fontStyle: 'italic' }}>[ firma actual guardada ]</span>
          </div>
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
            <span style={{ fontSize: '9px', color: '#ccc', fontStyle: 'italic' }}>[ sin firma ]</span>
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

function EditarDiplomaModal({ diploma, onSubmit, isLoading }) {
  const { t } = useTranslation()
  const [formData, setFormData] = useState({ firma: '', diseno: 'Personalizado', plantillaPdf: '', firmaImagen: '' })
  const [pdfFileName, setPdfFileName] = useState('')
  const [firmaFileName, setFirmaFileName] = useState('')
  const [firmaPreviewActual, setFirmaPreviewActual] = useState(null)
  const [newFirmaPreview, setNewFirmaPreview] = useState(null)
  const [loadingDetail, setLoadingDetail] = useState(false)
  const [errors, setErrors] = useState({})
  const pdfInputRef = useRef(null)
  const firmaInputRef = useRef(null)
  const closeBtnRef = useRef(null)

  useEffect(() => {
    if (!diploma) return
    setFormData({ firma: diploma.firma || '', diseno: diploma.diseno || 'Personalizado', plantillaPdf: '', firmaImagen: '' })
    setPdfFileName('')
    setFirmaFileName('')
    setNewFirmaPreview(null)
    setErrors({})
    setFirmaPreviewActual(null)

    // Cargar detalle completo para obtener la firma actual
    setLoadingDetail(true)
    diplomaService.obtenerDiploma(diploma.idDiploma)
      .then(detail => {
        if (detail?.firmaImagen) {
          const src = detail.firmaImagen.startsWith('data:')
            ? detail.firmaImagen
            : `data:image/png;base64,${detail.firmaImagen}`
          setFirmaPreviewActual(src)
        }
      })
      .catch(() => {})
      .finally(() => setLoadingDetail(false))
  }, [diploma])

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

    if (field === 'plantillaPdf' && !file.name.endsWith('.jrxml')) {
      toast.warning('Solo se permiten plantillas Jasper (.jrxml)')
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
      if (field === 'firmaImagen') {
        setFirmaFileName(file.name)
        setNewFirmaPreview(reader.result)
      }
    }
    reader.readAsDataURL(file)
  }

  const handleSubmit = async () => {
    const datos = {}
    if (formData.firma !== (diploma?.firma || '')) datos.firma = formData.firma
    if (formData.diseno && formData.diseno !== (diploma?.diseno || 'Personalizado')) datos.diseno = formData.diseno
    if (formData.plantillaPdf) datos.plantillaPdf = formData.plantillaPdf
    if (formData.firmaImagen) datos.firmaImagen = formData.firmaImagen

    if (Object.keys(datos).length === 0) {
      setErrors({ general: 'No hay cambios para guardar' })
      return
    }
    setErrors({})
    try {
      await onSubmit(diploma.idDiploma, datos)
      closeBtnRef.current?.click()
    } catch {
      // El error ya fue mostrado por el padre
    }
  }

  const firmaPreviewMostrada = newFirmaPreview || firmaPreviewActual

  return (
    <div className="modal fade" id="editarDiplomaModal" tabIndex="-1" aria-labelledby="editarDiplomaModalLabel" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered modal-xl">
        <div className="modal-content border-0 rounded-4 shadow bg-light">
          <div className="modal-header border-0 bg-white px-4 pt-4 pb-3 rounded-top-4">
            <div>
              <h5 className="fw-bold mb-1 text-dark" id="editarDiplomaModalLabel">{t('editDiploma.title')}</h5>
              <p className="text-secondary small mb-0" style={{ fontSize: '13px' }}>{t('editDiploma.subtitle')}</p>
            </div>
            <button type="button" className="btn-close align-self-start mt-1" data-bs-dismiss="modal" aria-label={t('common.close')} ref={closeBtnRef}></button>
          </div>

          <div className="modal-body p-0 bg-light">
            <div className="row g-0" style={{ minHeight: '480px' }}>
              {/* ── Formulario ── */}
              <div className="col-12 col-lg-7 p-4" style={{ maxHeight: '65vh', overflowY: 'auto' }}>
                {/* Info evento */}
                <div className="bg-white rounded-3 p-4 mb-3 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-info-circle text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('editDiploma.eventInfo')}</h6>
                  </div>
                  <input
                    type="text"
                    className="form-control bg-light text-dark small"
                    value={diploma?.nombreEvento || ''}
                    disabled
                    style={{ fontSize: '13px' }}
                  />
                </div>

                {/* Nombre firmante */}
                <div className="bg-white rounded-3 p-4 mb-3 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-pen text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('editDiploma.signerName')}</h6>
                  </div>
                  <input
                    type="text"
                    className="form-control text-dark small"
                    placeholder={t('editDiploma.signerPlaceholder')}
                    value={formData.firma}
                    onChange={(e) => setFormData(prev => ({ ...prev, firma: e.target.value }))}
                    style={{ fontSize: '13px' }}
                  />
                </div>

                {/* Plantilla JRXML */}
                <div className="bg-white rounded-3 p-4 mb-3 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-file-earmark-code text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('editDiploma.pdfTemplate')}</h6>
                  </div>
                  <div
                    className="border border-primary border-opacity-25 rounded-3 d-flex flex-column py-3 px-3 text-center"
                    style={{ borderStyle: 'dashed', backgroundColor: '#f8faff', cursor: 'pointer' }}
                    onClick={() => pdfInputRef.current?.click()}
                  >
                    {pdfFileName ? (
                      <div className="d-flex align-items-center justify-content-between bg-light rounded-3 p-2 border w-100 mx-auto" style={{ maxWidth: '600px' }}>
                        <div className="d-flex align-items-center gap-3">
                          <div className="bg-primary bg-opacity-10 text-primary rounded p-2">
                            <i className="bi bi-file-earmark-code"></i>
                          </div>
                          <div className="text-start lh-sm">
                            <div className="fw-semibold text-dark mb-1" style={{ fontSize: '12px' }}>{pdfFileName}</div>
                            <div className="text-secondary" style={{ fontSize: '10px' }}>{t('editDiploma.newTemplate')}</div>
                          </div>
                        </div>
                        <button
                          className="btn btn-link text-danger p-0 text-decoration-none fw-semibold"
                          style={{ fontSize: '11px' }}
                          onClick={(e) => { e.stopPropagation(); setFormData(prev => ({ ...prev, plantillaPdf: '' })); setPdfFileName('') }}
                        >
                          {t('editDiploma.remove')}
                        </button>
                      </div>
                    ) : (
                      <>
                        <i className="bi bi-cloud-arrow-up text-primary fs-3 mb-2"></i>
                        <div className="text-secondary" style={{ fontSize: '11px', lineHeight: '1.5' }}>
                          {diploma?.tienePlantilla ? t('editDiploma.replacePdf') : t('editDiploma.uploadPdf')}
                          <br />{t('editDiploma.pdfLimit')}
                        </div>
                      </>
                    )}
                    <input ref={pdfInputRef} type="file" className="d-none" accept=".jrxml" onChange={(e) => handleFileChange(e, 'plantillaPdf')} />
                  </div>
                </div>

                {/* Firma imagen */}
                <div className="bg-white rounded-3 p-4 shadow-sm">
                  <div className="d-flex align-items-center gap-2 mb-3">
                    <i className="bi bi-image text-primary"></i>
                    <h6 className="fw-bold mb-0 text-dark fs-6">{t('editDiploma.signatureImage')}</h6>
                  </div>
                  <div
                    className="border border-primary border-opacity-25 rounded-3 d-flex flex-column py-3 px-3 text-center"
                    style={{ borderStyle: 'dashed', backgroundColor: '#f8faff', cursor: 'pointer' }}
                    onClick={() => firmaInputRef.current?.click()}
                  >
                    {firmaFileName ? (
                      <div className="d-flex align-items-center justify-content-between bg-light rounded-3 p-2 border w-100 mx-auto" style={{ maxWidth: '600px' }}>
                        <div className="d-flex align-items-center gap-3">
                          <div className="bg-primary bg-opacity-10 text-primary rounded p-2">
                            <i className="bi bi-file-earmark-image"></i>
                          </div>
                          <div className="text-start lh-sm">
                            <div className="fw-semibold text-dark mb-1" style={{ fontSize: '12px' }}>{firmaFileName}</div>
                            <div className="text-secondary" style={{ fontSize: '10px' }}>{t('editDiploma.newSignature')}</div>
                          </div>
                        </div>
                        <button
                          className="btn btn-link text-danger p-0 text-decoration-none fw-semibold"
                          style={{ fontSize: '11px' }}
                          onClick={(e) => { e.stopPropagation(); setFormData(prev => ({ ...prev, firmaImagen: '' })); setFirmaFileName(''); setNewFirmaPreview(null) }}
                        >
                          {t('editDiploma.remove')}
                        </button>
                      </div>
                    ) : (
                      <>
                        <i className="bi bi-cloud-arrow-up text-primary fs-3 mb-2"></i>
                        <div className="text-secondary" style={{ fontSize: '11px', lineHeight: '1.5' }}>
                          {diploma?.tieneFirma ? t('editDiploma.replaceSignature') : t('editDiploma.uploadSignature')}
                          <br />{t('editDiploma.signatureLimit')}
                        </div>
                      </>
                    )}
                    <input ref={firmaInputRef} type="file" className="d-none" accept="image/*" onChange={(e) => handleFileChange(e, 'firmaImagen')} />
                  </div>
                </div>

                {diploma && (diploma.totalEmitidos > 0) && (
                  <div className="alert alert-warning mt-3 mb-0 d-flex align-items-center gap-2" style={{ fontSize: '13px' }}>
                    <i className="bi bi-exclamation-triangle"></i>
                    <span>{t('editDiploma.emissionWarning', { count: diploma.totalEmitidos })}</span>
                  </div>
                )}

                {errors.general && (
                  <div className="alert alert-danger mt-3 mb-0 d-flex align-items-center gap-2" style={{ fontSize: '13px' }}>
                    <i className="bi bi-x-circle"></i>
                    <span>{errors.general}</span>
                  </div>
                )}
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

                {loadingDetail ? (
                  <div className="d-flex align-items-center justify-content-center flex-grow-1">
                    <div className="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    <span className="text-secondary small">Cargando previsualización...</span>
                  </div>
                ) : (
                  <DiplomaPreview
                    eventName={diploma?.nombreEvento}
                    signerName={formData.firma}
                    firmaPreview={firmaPreviewMostrada}
                    tieneFirma={diploma?.tieneFirma}
                  />
                )}

                <p className="text-secondary text-center mt-3 mb-0" style={{ fontSize: '11px' }}>
                  {newFirmaPreview
                    ? 'Mostrando nueva firma seleccionada.'
                    : firmaPreviewActual
                    ? 'Mostrando firma actual del diploma.'
                    : 'La previsualización se actualiza al subir una firma.'}
                </p>
              </div>
            </div>
          </div>

          <div className="modal-footer border-0 px-4 py-3 bg-white rounded-bottom-4 d-flex justify-content-end gap-2">
            <button
              type="button"
              className="btn btn-outline-secondary px-4 fw-semibold border-light-subtle text-dark"
              data-bs-dismiss="modal"
              style={{ fontSize: '13px' }}
            >
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
                <><span className="spinner-border spinner-border-sm me-1" role="status"></span>{t('editDiploma.saving')}</>
              ) : (
                <><i className="bi bi-check2 border border-white rounded-circle px-1" style={{ fontSize: '10px' }}></i>{t('common.save')}</>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarDiplomaModal
