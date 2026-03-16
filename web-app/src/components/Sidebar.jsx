import { NavLink, Link, useLocation } from 'react-router-dom'

function Sidebar({ menuItems, user }) {
  const location = useLocation()

  const userName = user?.name || 'Usuario'
  const userInitials = userName.split(' ').filter(Boolean).map(n => n[0]).join('').toUpperCase().slice(0, 2)
  const rolLabel = user?.role === 'ADMIN'
    ? (user?.originalRole === 'SUPERADMIN' ? 'Super Administrador' : 'Administrador')
    : 'Estudiante'

  return (
    <nav className="d-flex flex-column h-100 bg-white border-end py-3">
      <div className="d-flex align-items-center gap-2 px-3 mb-4">
        <div className="rounded-circle border border-2 border-primary bg-primary bg-opacity-10 d-flex align-items-center justify-content-center flex-shrink-0 fw-bold text-primary"
          style={{ width: '40px', height: '40px', fontSize: '14px' }}>
          {userInitials}
        </div>
        <div className="text-truncate">
          <div className="fw-semibold small text-truncate text-dark">{userName}</div>
          <div className="text-secondary small text-truncate">{rolLabel}</div>
        </div>
      </div>

      {menuItems.map((item, index) => {
        // Special logic to highlight "Eventos" path and subpaths for students/admins 
        // to keep the highlight active when viewing event details
        const isEventsLink = item.path.includes('/eventos')
        const isEventsActive = isEventsLink && (
          location.pathname.startsWith(`${item.path.replace('/eventos', '')}/evento`) || 
          location.pathname.startsWith(`${item.path.replace('/eventos', '')}/mis-eventos`)
        )

        return (
          <NavLink
            key={index}
            to={item.path}
            end={item.exact}
            className={({ isActive }) =>
              `d-flex align-items-center gap-2 px-3 py-2 text-decoration-none ${(isActive || isEventsActive) ? 'bg-primary bg-opacity-10 text-primary fw-semibold border-start border-3 border-primary' : 'text-secondary'}`
            }
          >
            <i className={item.icon}></i>
            {item.label}
          </NavLink>
        )
      })}

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

export default Sidebar
