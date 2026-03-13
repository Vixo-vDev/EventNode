<<<<<<< HEAD
function Header() {
  return (
    <header className="d-md-none bg-white border-bottom px-3 py-2 d-flex align-items-center">
      <button
        className="btn btn-outline-secondary btn-sm me-3"
=======
import { Link } from 'react-router-dom'

function Header() {
  return (
    <header className="bg-white border-bottom shadow-sm d-flex justify-content-between align-items-center px-3 py-2 d-md-none">
      <button
        className="btn btn-outline-dark border-0"
>>>>>>> feature/frontend-web/Home_Estudiante
        type="button"
        data-bs-toggle="offcanvas"
        data-bs-target="#mobileSidebar"
        aria-controls="mobileSidebar"
      >
<<<<<<< HEAD
        <i className="bi bi-list"></i>
      </button>
      <span className="fw-bold text-dark font-monospace small">
        {`{ EN }`} EventNode
      </span>
=======
        <i className="bi bi-list fs-4"></i>
      </button>
      <Link to="/estudiante" className="text-decoration-none text-dark d-flex align-items-center gap-2">
        <span className="fw-bold font-monospace">{`{ EN }`}</span>
        <span className="fw-semibold">EventNode</span>
      </Link>
      <div className="invisible">
        <i className="bi bi-list fs-4"></i>
      </div>
>>>>>>> feature/frontend-web/Home_Estudiante
    </header>
  )
}

export default Header
