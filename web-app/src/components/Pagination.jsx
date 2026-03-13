function Pagination() {
  return (
    <nav className="d-flex justify-content-center mt-4">
      <ul className="pagination pagination-sm mb-0">
        <li className="page-item">
          <button className="page-link rounded-start-pill px-3">
            <i className="bi bi-chevron-left me-1"></i>
            Anterior
          </button>
        </li>
        <li className="page-item active">
          <button className="page-link">1</button>
        </li>
        <li className="page-item">
          <button className="page-link">2</button>
        </li>
        <li className="page-item">
          <button className="page-link">3</button>
        </li>
        <li className="page-item disabled">
          <button className="page-link">...</button>
        </li>
        <li className="page-item">
          <button className="page-link">8</button>
        </li>
        <li className="page-item">
          <button className="page-link rounded-end-pill px-3">
            Siguiente
            <i className="bi bi-chevron-right ms-1"></i>
          </button>
        </li>
      </ul>
    </nav>
  )
}

export default Pagination
