import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'
import profileAvatar from '../../assets/profile_avatar.png'

function StudentProfile() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
      <h2 className="fw-bold mb-4">Perfil</h2>

      <div className="card border-0 shadow-sm rounded-3 mb-4">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row align-items-center gap-3 mb-4">
            <img
              src={profileAvatar}
              alt="Sophia Díaz"
              className="rounded-circle border border-3 border-primary"
              style={{ width: '90px', height: '90px', objectFit: 'cover' }}
            />
            <div className="text-center text-md-start">
              <h5 className="fw-bold mb-0">Sophia Díaz</h5>
              <span className="badge bg-success bg-opacity-10 text-success small">Activo</span>
            </div>
            <div className="ms-md-auto">
              <button className="btn btn-link text-primary text-decoration-none small fw-semibold">
                Cambiar foto
              </button>
            </div>
          </div>

          <h6 className="fw-bold mb-3">
            <i className="bi bi-person-badge text-primary me-2"></i>
            Información Personal
          </h6>

          <div className="row g-3 mb-4">
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Nombre(s)</label>
              <input type="text" className="form-control bg-light" defaultValue="Sophia" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Apellidos</label>
              <input type="text" className="form-control bg-light" defaultValue="Díaz" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Correo</label>
              <input type="email" className="form-control bg-light" defaultValue="20243ds01@utez.edu.mx" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Matrícula</label>
              <input type="text" className="form-control bg-light" defaultValue="20243ds01" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Edad</label>
              <input type="text" className="form-control bg-light" defaultValue="21" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Sexo</label>
              <input type="text" className="form-control bg-light" defaultValue="Femenino" readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Cuatrimestre</label>
              <input type="text" className="form-control bg-light" defaultValue="1" readOnly />
            </div>
          </div>

          <hr className="my-4" />

          <h6 className="fw-bold mb-3">
            <i className="bi bi-shield-lock text-primary me-2"></i>
            Seguridad
          </h6>

          <div className="mb-4">
            <label className="form-label text-secondary small">Contraseña</label>
            <div className="d-flex align-items-center gap-3">
              <div className="input-group" style={{ maxWidth: '250px' }}>
                <span className="input-group-text bg-light border-end-0">
                  <i className="bi bi-lock text-secondary"></i>
                </span>
                <input type="password" className="form-control bg-light border-start-0" defaultValue="12345678" readOnly />
              </div>
              <button className="btn btn-link text-primary text-decoration-none small fw-semibold p-0">
                <i className="bi bi-pencil me-1"></i>
                Cambiar contraseña
              </button>
            </div>
          </div>

          <div className="d-flex justify-content-end gap-2">
            <button className="btn btn-link text-secondary text-decoration-none">
              Cancelar
            </button>
            <button className="btn btn-primary rounded-pill px-4 d-flex align-items-center gap-2">
              <i className="bi bi-save"></i>
              Guardar cambios
            </button>
          </div>
        </div>
      </div>

      <div className="d-flex align-items-center gap-2 p-3 bg-white rounded-3 shadow-sm">
        <div className="rounded-circle bg-primary d-flex align-items-center justify-content-center flex-shrink-0"
          style={{ width: '32px', height: '32px' }}>
          <i className="bi bi-check-lg text-white small"></i>
        </div>
        <div>
          <div className="fw-semibold small">Verificación de Cuenta</div>
          <div className="text-secondary small">
            Tu cuenta está verificada por la matrícula
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default StudentProfile
