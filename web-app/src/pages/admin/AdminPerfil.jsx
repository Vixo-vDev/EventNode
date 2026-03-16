import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import { userService } from '../../services/userService'

function AdminPerfil({ user }) {
  const [perfil, setPerfil] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!user?.id) return

    const fetchPerfil = async () => {
      try {
        const data = await userService.getPerfil(user.id)
        setPerfil(data)
      } catch (err) {
        toast.error(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchPerfil()
  }, [user?.id])

  const userName = perfil
    ? `${perfil.nombre} ${perfil.apellidoPaterno}`
    : user?.name || 'Administrador'

  const userInitials = userName.split(' ').filter(Boolean).map(n => n[0]).join('').toUpperCase().slice(0, 2)

  const rolLabel = perfil?.rol === 'SUPERADMIN'
    ? 'Super Administrador'
    : 'Administrador'

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      </div>
    )
  }

  return (
    <div>
      <h2 className="fw-bold mb-4">Perfil</h2>

      <div className="card border-0 shadow-sm rounded-3 mb-4">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row align-items-center gap-3 mb-4">
            <div className="rounded-circle border border-2 border-primary bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0 fw-bold text-primary"
              style={{ width: '90px', height: '90px', fontSize: '32px' }}>
              {userInitials}
            </div>
            <div className="text-center text-md-start">
              <h5 className="fw-bold mb-0">{userName}</h5>
              <span className={`badge ${perfil?.estado === 'ACTIVO' ? 'bg-success bg-opacity-10 text-success' : 'bg-danger bg-opacity-10 text-danger'} small`}>
                {perfil?.estado || 'Activo'}
              </span>
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
            <div className="col-12 col-md-4">
              <label className="form-label text-secondary small">Nombre(s)</label>
              <input type="text" className="form-control bg-light" value={perfil?.nombre || ''} readOnly />
            </div>
            <div className="col-12 col-md-4">
              <label className="form-label text-secondary small">Apellido Paterno</label>
              <input type="text" className="form-control bg-light" value={perfil?.apellidoPaterno || ''} readOnly />
            </div>
            <div className="col-12 col-md-4">
              <label className="form-label text-secondary small">Apellido Materno</label>
              <input type="text" className="form-control bg-light" value={perfil?.apellidoMaterno || ''} readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Correo</label>
              <input type="email" className="form-control bg-light" value={perfil?.correo || ''} readOnly />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label text-secondary small">Rol</label>
              <input type="text" className="form-control bg-light" value={rolLabel} readOnly />
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
                <input type="password" className="form-control bg-light border-start-0" defaultValue="••••••••" readOnly />
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
    </div>
  )
}

export default AdminPerfil
