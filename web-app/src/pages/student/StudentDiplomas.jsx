import { useState, useEffect } from 'react'
import { diplomaService } from '../../services/diplomaService'

function StudentDiplomas({ user }) {
  const [diplomas, setDiplomas] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!user?.id) return
    const fetchDiplomas = async () => {
      try {
        const data = await diplomaService.listarDiplomasEstudiante(user.id)
        setDiplomas(data)
      } catch {
        setDiplomas([])
      } finally {
        setLoading(false)
      }
    }
    fetchDiplomas()
  }, [user])

  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4 gap-2">
        <div>
          <h2 className="fw-bold mb-1">Diplomas</h2>
          <p className="text-secondary small mb-0">
            Aquí puedes encontrar todos los diplomas de los cursos y talleres a los que has asistido.
          </p>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
        </div>
      ) : diplomas.length > 0 ? (
        <div className="row g-3">
          {diplomas.map(d => (
            <div className="col-12 col-md-6 col-lg-4" key={d.idEmitido}>
              <div className="card border-0 shadow-sm rounded-3 h-100">
                <div className="card-body p-4 text-center">
                  <div className="rounded-circle bg-primary bg-opacity-10 d-inline-flex align-items-center justify-content-center mb-3" style={{ width: '56px', height: '56px' }}>
                    <i className="bi bi-award text-primary fs-4"></i>
                  </div>
                  <h6 className="fw-bold mb-1">{d.nombreEvento}</h6>
                  <p className="text-secondary small mb-2">
                    {d.fechaEnvio ? new Date(d.fechaEnvio).toLocaleDateString('es-MX', { day: 'numeric', month: 'long', year: 'numeric' }) : ''}
                  </p>
                  <span className={`badge rounded-pill px-3 py-1 ${d.estadoEnvio === 'ENVIADO' ? 'bg-success bg-opacity-10 text-success' : 'bg-danger bg-opacity-10 text-danger'}`}>
                    {d.estadoEnvio === 'ENVIADO' ? 'Recibido' : 'Error'}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
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
      )}
    </div>
  )
}

export default StudentDiplomas
