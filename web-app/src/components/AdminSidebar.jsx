import { NavLink, useLocation } from 'react-router-dom'

function AdminSidebar() {
  const location = useLocation()

  return (
    <nav className="d-flex flex-column h-100 bg-white border-end py-3">
      <div className="d-flex align-items-center gap-2 px-3 mb-4">
        <div className="rounded-circle bg-light d-flex align-items-center justify-content-center flex-shrink-0"
          style={{ width: '40px', height: '40px' }}>
          <i className="bi bi-person-fill text-secondary"></i>
        </div>
        <div className="text-truncate">
          <div className="fw-semibold small text-truncate text-dark">Sophia</div>
          <div className="text-secondary small text-truncate">Administrador</div>
        </div>
      </div>

      <NavLink
        to="/admin"
        end
        className={({ isActive }) =>
          `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${isActive ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
        }
      >
        <i className="bi bi-house"></i>
        Inicio
      </NavLink>

      <NavLink
        to="/admin/eventos"
        className={({ isActive }) =>
          `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${isActive ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
        }
      >
        <i className="bi bi-calendar-event"></i>
        Eventos
      </NavLink>

      <NavLink
        to="/admin/diplomas"
        className={({ isActive }) =>
          `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${isActive ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
        }
      >
        <i className="bi bi-award"></i>
        Diplomas
      </NavLink>

      <NavLink
        to="/admin/estudiantes"
        className={({ isActive }) =>
          `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${isActive ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
        }
      >
        <i className="bi bi-people"></i>
        Estudiantes
      </NavLink>

      <NavLink
        to="/admin/perfil"
        className={({ isActive }) =>
          `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${isActive ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
        }
      >
        <i className="bi bi-person"></i>
        Perfil
      </NavLink>

      <div className="mt-auto px-3 py-2">
        <button
          type="button"
          className="d-flex align-items-center gap-2 text-decoration-none text-secondary small btn btn-link p-0 border-0"
          data-bs-toggle="modal"
          data-bs-target="#logoutModal"
        >
          <i className="bi bi-box-arrow-left"></i>
          Cerrar sesión
        </button>
      </div>
    </nav>
  )
}

export default AdminSidebar
