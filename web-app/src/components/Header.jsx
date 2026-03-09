function Header() {
  return (
    <header className="d-md-none bg-white border-bottom px-3 py-2 d-flex align-items-center">
      <button
        className="btn btn-outline-secondary btn-sm me-3"
        type="button"
        data-bs-toggle="offcanvas"
        data-bs-target="#mobileSidebar"
        aria-controls="mobileSidebar"
      >
        <i className="bi bi-list"></i>
      </button>
      <span className="fw-bold text-dark font-monospace small">
        {`{ EN }`} EventNode
      </span>
    </header>
  )
}

export default Header
