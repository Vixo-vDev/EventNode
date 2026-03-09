import { Link } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import StudentSidebar from '../../components/StudentSidebar'

function StudentDiplomaDetail() {
  return (
    <DashboardLayout sidebar={<StudentSidebar />}>
      <Link
        to="/estudiante/diplomas"
        className="text-secondary text-decoration-none small d-flex align-items-center gap-1 mb-3"
      >
        <i className="bi bi-arrow-left"></i>
        Volver a Mis Certificados
      </Link>

      <h4 className="fw-bold mb-4">Detalles de Diploma</h4>

      <div className="row g-4">
        <div className="col-12 col-lg-8">
          <div className="card border-0 shadow-sm rounded-3 mb-3">
            <div className="card-body p-4 p-md-5 text-center"
              style={{
                border: '3px solid #e0e7ff',
                borderRadius: '12px',
                background: 'linear-gradient(135deg, #f8faff 0%, #ffffff 100%)'
              }}>
              <div className="mb-2">
                <i className="bi bi-mortarboard text-primary fs-2"></i>
              </div>
              <h5 className="fw-bold mb-1">Diploma de Asistencia</h5>
              <p className="text-uppercase text-secondary small mb-3 ls-wide">
                Se otorga con orgullo a
              </p>
              <h3 className="fw-bold mb-3" style={{ fontSize: '1.8rem' }}>
                David Valenzuela Guijosa
              </h3>
              <p className="text-secondary small mb-2">
                Por haber asistido al evento
              </p>
              <Link
                to="/estudiante/evento/1"
                className="text-primary fw-semibold text-decoration-none"
              >
                Taller de Hackathon
              </Link>
            </div>
          </div>

          <div className="d-flex align-items-center gap-2 p-3 bg-white rounded-3 shadow-sm">
            <div className="rounded-circle bg-primary d-flex align-items-center justify-content-center flex-shrink-0"
              style={{ width: '32px', height: '32px' }}>
              <i className="bi bi-check-lg text-white small"></i>
            </div>
            <div>
              <div className="fw-semibold small">Certificado Verificado</div>
              <div className="text-secondary small">
                Este diploma es auténtico y verificado oficialmente por la UTEZ
              </div>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-4">
          <div className="card border-0 shadow-sm rounded-3 mb-3">
            <div className="card-body p-4">
              <h6 className="fw-bold mb-3">Taller de Hackathon</h6>

              <div className="d-flex align-items-start gap-2 mb-3">
                <i className="bi bi-check-circle text-primary small mt-1"></i>
                <div>
                  <div className="text-secondary small text-uppercase">Finalizado el</div>
                  <div className="fw-semibold small">15 de Julio, 2023</div>
                </div>
              </div>

              <div className="d-flex align-items-start gap-2 mb-3">
                <i className="bi bi-clock text-primary small mt-1"></i>
                <div>
                  <div className="text-secondary small text-uppercase">Duración</div>
                  <div className="fw-semibold small">2 horas de contenido</div>
                </div>
              </div>

              <div className="d-flex align-items-start gap-2 mb-4">
                <i className="bi bi-geo-alt text-primary small mt-1"></i>
                <div>
                  <div className="text-secondary small text-uppercase">Ubicación</div>
                  <div className="fw-semibold small">Auditorio</div>
                </div>
              </div>

              <button className="btn btn-primary w-100 rounded-pill fw-semibold d-flex align-items-center justify-content-center gap-2">
                <i className="bi bi-download"></i>
                Descargar PDF
              </button>
            </div>
          </div>

          <div className="card border-0 rounded-3 text-white"
            style={{ background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 100%)' }}>
            <div className="card-body p-4">
              <h6 className="fw-bold mb-2">Siguiente Paso</h6>
              <p className="small opacity-75 mb-1">Has completado el evento.</p>
              <p className="small opacity-75 mb-3">¿Quieres asistir a otro evento?</p>
              <Link
                to="/estudiante/eventos"
                className="btn btn-light btn-sm rounded-pill fw-semibold w-100"
              >
                Explorar Eventos
              </Link>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default StudentDiplomaDetail
