import { Link } from 'react-router-dom'

function ForgotPasswordForm() {
  return (
    <form>
      <div className="mb-4">
        <label className="form-label small fw-semibold" htmlFor="recoveryEmail">
          Correo institucional
        </label>
        <div className="input-group">
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-envelope text-secondary"></i>
          </span>
          <input
            type="email"
            className="form-control border-start-0"
            id="recoveryEmail"
            placeholder="matricula@utez.edu.mx"
          />
        </div>
      </div>

      <button
        type="submit"
        className="btn btn-primary w-100 py-2 rounded-pill fw-semibold"
      >
        Enviar código <i className="bi bi-arrow-right ms-1"></i>
      </button>

      <p className="text-center mt-4 mb-0 small">
        <Link
          to="/login"
          className="text-primary text-decoration-none fw-semibold"
        >
          <i className="bi bi-arrow-left me-1"></i>
          Volver al inicio de sesión
        </Link>
      </p>
    </form>
  )
}

export default ForgotPasswordForm
