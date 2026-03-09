import { Link } from 'react-router-dom'

function LoginForm() {
  return (
    <form>
      <div className="mb-3">
        <label className="form-label small fw-semibold" htmlFor="loginEmail">
          Correo Institucional
        </label>
        <div className="input-group">
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-envelope text-secondary"></i>
          </span>
          <input
            type="email"
            className="form-control border-start-0"
            id="loginEmail"
            placeholder="20243ds01@utez.edu.mx"
          />
        </div>
      </div>

      <div className="mb-3">
        <div className="d-flex justify-content-between align-items-center mb-1">
          <label className="form-label small fw-semibold mb-0" htmlFor="loginPassword">
            Contraseña
          </label>
          <Link
            to="/forgot-password"
            className="small text-primary text-decoration-none"
          >
            ¿Haz olvidado la contraseña?
          </Link>
        </div>
        <div className="input-group">
          <span className="input-group-text bg-white border-end-0">
            <i className="bi bi-lock text-secondary"></i>
          </span>
          <input
            type="password"
            className="form-control border-start-0 border-end-0"
            id="loginPassword"
            placeholder="••••••••"
          />
          <span className="input-group-text bg-white border-start-0" role="button">
            <i className="bi bi-eye text-secondary"></i>
          </span>
        </div>
      </div>

      <div className="form-check mb-4">
        <input
          type="checkbox"
          className="form-check-input"
          id="keepSession"
        />
        <label className="form-check-label small" htmlFor="keepSession">
          Mantener Sesión Iniciada
        </label>
      </div>

      <button
        type="submit"
        className="btn btn-primary w-100 py-2 rounded-pill fw-semibold"
      >
        Iniciar Sesión <i className="bi bi-arrow-right ms-1"></i>
      </button>

      <p className="text-center mt-4 mb-0 small text-secondary">
        ¿No tienes una cuenta?{' '}
        <Link
          to="/register"
          className="text-primary text-decoration-none fw-semibold"
        >
          Crea una cuenta
        </Link>
      </p>
    </form>
  )
}

export default LoginForm
