import { Link } from 'react-router-dom'

function RegisterForm() {
  return (
    <form>
      <div className="row mb-3">
        <div className="col-6">
          <label className="form-label small fw-semibold" htmlFor="regName">
            Nombre(s)*
          </label>
          <input
            type="text"
            className="form-control"
            id="regName"
            placeholder="Ingresa tu nombre"
          />
        </div>
        <div className="col-6">
          <label className="form-label small fw-semibold" htmlFor="regLastName">
            Apellidos*
          </label>
          <input
            type="text"
            className="form-control"
            id="regLastName"
            placeholder="Ingresa tus apellidos"
          />
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-6">
          <label className="form-label small fw-semibold" htmlFor="regMatricula">
            Matrícula*
          </label>
          <input
            type="text"
            className="form-control"
            id="regMatricula"
            placeholder="Ej: 20243ds01"
          />
        </div>
        <div className="col-6">
          <label className="form-label small fw-semibold" htmlFor="regEmail">
            Correo institucional*
          </label>
          <input
            type="email"
            className="form-control"
            id="regEmail"
            placeholder="matricula@utez.edu.mx"
          />
        </div>
      </div>

      <div className="mb-3">
        <label className="form-label small fw-semibold" htmlFor="regPassword">
          Contraseña*
        </label>
        <div className="input-group">
          <input
            type="password"
            className="form-control border-end-0"
            id="regPassword"
            placeholder="••••••••"
          />
          <span className="input-group-text bg-white border-start-0" role="button">
            <i className="bi bi-eye text-secondary"></i>
          </span>
        </div>
        <small className="text-primary d-block mt-1">
          Requisitos: mínimo 8 caracteres, mayúsculas, minúsculas, números y símbolos.
        </small>
      </div>

      <div className="row mb-3">
        <div className="col-4">
          <label className="form-label small fw-semibold" htmlFor="regAge">
            Edad*
          </label>
          <input
            type="number"
            className="form-control"
            id="regAge"
            placeholder="18"
          />
        </div>
        <div className="col-4">
          <label className="form-label small fw-semibold" htmlFor="regSex">
            Sexo*
          </label>
          <select className="form-select" id="regSex">
            <option value="">Seleccionar</option>
            <option value="M">Masculino</option>
            <option value="F">Femenino</option>
          </select>
        </div>
        <div className="col-4">
          <label className="form-label small fw-semibold" htmlFor="regQuarter">
            Cuatrimestre*
          </label>
          <input
            type="number"
            className="form-control"
            id="regQuarter"
            placeholder="1"
          />
        </div>
      </div>

      <div className="mb-4">
        <label className="form-label small fw-semibold">
          Identidad de Género*
        </label>
        <div className="d-flex gap-4">
          <div className="form-check">
            <input
              type="radio"
              className="form-check-input"
              name="gender"
              id="genderMale"
              value="hombre"
            />
            <label className="form-check-label small" htmlFor="genderMale">
              Hombre
            </label>
          </div>
          <div className="form-check">
            <input
              type="radio"
              className="form-check-input"
              name="gender"
              id="genderFemale"
              value="mujer"
            />
            <label className="form-check-label small" htmlFor="genderFemale">
              Mujer
            </label>
          </div>
          <div className="form-check">
            <input
              type="radio"
              className="form-check-input"
              name="gender"
              id="genderOther"
              value="otro"
            />
            <label className="form-check-label small" htmlFor="genderOther">
              Otro
            </label>
          </div>
        </div>
      </div>

      <button
        type="submit"
        className="btn btn-primary w-100 py-2 rounded-pill fw-semibold"
      >
        Crear cuenta
      </button>

      <p className="text-center mt-4 mb-0 small text-secondary">
        ¿Ya tienes una cuenta?{' '}
        <Link
          to="/login"
          className="text-primary text-decoration-none fw-semibold"
        >
          Inicia sesión
        </Link>
      </p>
    </form>
  )
}

export default RegisterForm
