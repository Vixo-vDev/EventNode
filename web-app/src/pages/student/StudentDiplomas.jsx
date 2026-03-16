function StudentDiplomas() {
  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Diplomas</h2>
          <p className="text-secondary small mb-0">
            Aquí puedes encontrar y descargar todos los diplomas de los cursos y
            talleres a los que has asistido.
          </p>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-3">
        <div className="card-body text-center py-5">
          <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '64px', height: '64px' }}>
            <i className="bi bi-award text-primary fs-3"></i>
          </div>
          <h6 className="fw-bold mb-1">Aún no tienes diplomas</h6>
          <p className="text-secondary small mb-0">
            Asiste a eventos y completa tu participación para recibir diplomas y certificaciones.
          </p>
        </div>
      </div>
    </div>
  )
}

export default StudentDiplomas
