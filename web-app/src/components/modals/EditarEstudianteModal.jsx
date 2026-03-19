import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { userService } from '../../services/userService'

function EditarEstudianteModal({ student, onStudentUpdated }) {
  const [formData, setFormData] = useState({
    nombre: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    matricula: '',
    correo: '',
    edad: '',
    sexo: 'Hombre',
    cuatrimestre: ''
  })
  const [loading, setLoading] = useState(false)
  const [disableLoading, setDisableLoading] = useState(false)

  useEffect(() => {
    if (student) {
      setFormData({
        nombre: student.nombre || '',
        apellidoPaterno: student.apellidoPaterno || '',
        apellidoMaterno: student.apellidoMaterno || '',
        matricula: student.matricula || '',
        correo: student.email || '',
        edad: student.edad || '',
        sexo: student.sexo || 'Hombre',
        cuatrimestre: student.cuatrimestre || ''
      })
    }
  }, [student])

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async () => {
    try {
      setLoading(true)
      await userService.actualizarAlumno(student.id, {
        nombre: formData.nombre,
        apellidoPaterno: formData.apellidoPaterno,
        apellidoMaterno: formData.apellidoMaterno,
        correo: formData.correo,
        edad: parseInt(formData.edad, 10),
        sexo: formData.sexo,
        cuatrimestre: parseInt(formData.cuatrimestre, 10)
      })
      toast.success('Estudiante actualizado exitosamente')
      
      const modalEl = document.getElementById('editarEstudianteModal')
      if (modalEl && window.bootstrap) {
        const bsModal = window.bootstrap.Modal.getInstance(modalEl)
        if (bsModal) bsModal.hide()
      }
      
      if (onStudentUpdated) onStudentUpdated()
    } catch (err) {
      toast.error(err.message || 'Error al actualizar estudiante')
    } finally {
      setLoading(false)
    }
  }

  const handleToggleState = async () => {
    try {
      setDisableLoading(true)
      await userService.cambiarEstado(student.id)
      toast.success(student?.active ? 'Estudiante deshabilitado exitosamente' : 'Estudiante habilitado exitosamente')
      
      const modalEl = document.getElementById('editarEstudianteModal')
      if (modalEl && window.bootstrap) {
        const bsModal = window.bootstrap.Modal.getInstance(modalEl)
        if (bsModal) bsModal.hide()
      }
      
      if (onStudentUpdated) onStudentUpdated()
    } catch (err) {
      toast.error(err.message || 'Error al cambiar estado')
    } finally {
      setDisableLoading(false)
    }
  }

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
                <input type="text" name="nombre" value={formData.nombre} onChange={handleChange} className="form-control text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Apellido Paterno</label>
                <input type="text" name="apellidoPaterno" value={formData.apellidoPaterno} onChange={handleChange} className="form-control text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Apellido Materno</label>
                <input type="text" name="apellidoMaterno" value={formData.apellidoMaterno} onChange={handleChange} className="form-control text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
              </div>
              <div className="col-12 col-md-6">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Matricula</label>
                <input type="text" className="form-control text-secondary bg-light small border-light-subtle" value={formData.matricula} readOnly style={{ fontSize: '13px' }} />
              </div>
              <div className="col-12">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Email Institucional</label>
                <div className="input-group">
                  <span className="input-group-text bg-white border-end-0 text-secondary pe-2">
                    <i className="bi bi-envelope"></i>
                  </span>
                  <input type="email" name="correo" value={formData.correo} onChange={handleChange} className="form-control border-start-0 ps-0 text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
                </div>
              </div>

              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Edad</label>
                <input type="number" name="edad" value={formData.edad} onChange={handleChange} className="form-control text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
              </div>
              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Sexo</label>
                <select name="sexo" value={formData.sexo} onChange={handleChange} className="form-select text-dark small" style={{ fontSize: '13px' }} disabled={loading}>
                  <option value="Hombre">Hombre</option>
                  <option value="Mujer">Mujer</option>
                  <option value="Otro">Otro</option>
                </select>
              </div>
              <div className="col-4">
                <label className="form-label text-dark fw-bold small mb-2" style={{ fontSize: '12px' }}>Cuatrimestre</label>
                <input type="number" name="cuatrimestre" value={formData.cuatrimestre} onChange={handleChange} className="form-control text-dark small" style={{ fontSize: '13px' }} disabled={loading} />
              </div>
            </div>
          </div>
          
          <div className="modal-footer border-top-0 px-4 py-3 bg-light bg-opacity-50 rounded-bottom-4 d-flex justify-content-between algin-items-center mt-2">
            <div>
              {student && (
                <button 
                  type="button" 
                  className={`btn ${student.active ? 'btn-outline-danger' : 'btn-outline-success'} fw-semibold px-3 py-2 rounded-3`} 
                  style={{ fontSize: '13px' }}
                  onClick={handleToggleState}
                  disabled={disableLoading || loading}
                >
                  {disableLoading ? (
                    <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                  ) : (
                    <i className={`bi ${student.active ? 'bi-person-dash' : 'bi-person-check'} me-2`}></i>
                  )}
                  {student.active ? 'Deshabilitar' : 'Habilitar'}
                </button>
              )}
            </div>
            <div className="d-flex gap-2">
              <button type="button" className="btn btn-white border px-4 fw-semibold text-dark rounded-3" data-bs-dismiss="modal" style={{ fontSize: '13px' }} disabled={loading || disableLoading}>
                Cancelar
              </button>
              <button type="button" onClick={handleSubmit} className="btn btn-primary px-4 fw-semibold rounded-3" style={{ fontSize: '13px' }} disabled={loading || disableLoading}>
                {loading ? (
                  <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                ) : 'Guardar cambios'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default EditarEstudianteModal
