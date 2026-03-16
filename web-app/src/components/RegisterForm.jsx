import { Link } from 'react-router-dom'

function RegisterForm({ formData, age, error, success, isLoading, onChange, onBirthDateChange, onSubmit }) {
  return (
    <form onSubmit={onSubmit}>
      {error && <div className="alert alert-danger mb-3 p-2 small text-center">{error}</div>}
      {success && <div className="alert alert-success mb-3 p-2 small text-center">¡Cuenta creada con éxito! Redirigiendo al login...</div>}
      
      <div className="row mb-3">
        <div className="col-12 col-md-4 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regName">
            Nombre(s)*
          </label>
          <input
            type="text"
            className="form-control"
            id="regName"
            name="nombre"
            value={formData.nombre}
            onChange={onChange}
            placeholder="Ingresa tu nombre"
            required
          />
        </div>
        <div className="col-12 col-md-4 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regLastNameP">
            Apellido Paterno*
          </label>
          <input
            type="text"
            className="form-control"
            id="regLastNameP"
            name="apellidoPaterno"
            value={formData.apellidoPaterno}
            onChange={onChange}
            placeholder="Apellido paterno"
            required
          />
        </div>
        <div className="col-12 col-md-4">
          <label className="form-label small fw-semibold" htmlFor="regLastNameM">
            Apellido Materno*
          </label>
          <input
            type="text"
            className="form-control"
            id="regLastNameM"
            name="apellidoMaterno"
            value={formData.apellidoMaterno}
            onChange={onChange}
            placeholder="Apellido materno"
            required
          />
        </div>
      </div>

      <div className="row mb-3">
        <div className="col-12 col-md-6 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regMatricula">
            Matrícula*
          </label>
          <input
            type="text"
            className="form-control"
            id="regMatricula"
            name="matricula"
            value={formData.matricula}
            onChange={onChange}
            placeholder="Ej: 20243ds01"
            required
          />
        </div>
        <div className="col-12 col-md-6">
          <label className="form-label small fw-semibold" htmlFor="regEmail">
            Correo institucional*
          </label>
          <input
            type="email"
            className="form-control"
            id="regEmail"
            name="correo"
            value={formData.correo}
            onChange={onChange}
            placeholder="matricula@utez.edu.mx"
            required
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
            name="password"
            value={formData.password}
            onChange={onChange}
            placeholder="••••••••"
            required
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
        <div className="col-12 col-md-4 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regBirthDate">
            Fecha de nacimiento*
          </label>
          <input
            type="date"
            className="form-control"
            id="regBirthDate"
            name="fechaNacimiento"
            value={formData.fechaNacimiento}
            onChange={onBirthDateChange}
            required
          />
        </div>
        <div className="col-12 col-md-2 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regAge">
            Edad
          </label>
          <input
            type="text"
            className="form-control bg-light"
            id="regAge"
            value={age}
            readOnly
            disabled
          />
        </div>
        <div className="col-12 col-md-3 mb-3 mb-md-0">
          <label className="form-label small fw-semibold" htmlFor="regSex">
            Sexo*
          </label>
          <select 
            className="form-select" 
            id="regSex" 
            name="sexo" 
            value={formData.sexo} 
            onChange={onChange} 
            required
          >
            <option value="">Seleccionar</option>
            <option value="M">Masculino</option>
            <option value="F">Femenino</option>
          </select>
        </div>
        <div className="col-12 col-md-3">
          <label className="form-label small fw-semibold" htmlFor="regQuarter">
            Cuatrimestre*
          </label>
          <select 
            className="form-select" 
            id="regQuarter" 
            name="cuatrimestre" 
            value={formData.cuatrimestre} 
            onChange={onChange} 
            required
          >
            <option value="">Seleccionar</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
          </select>
        </div>
      </div>

      <button
        type="submit"
        className="btn btn-primary w-100 py-2 rounded-pill fw-semibold mt-2"
        disabled={isLoading || success}
      >
        {isLoading ? 'Creando cuenta...' : 'Crear cuenta'}
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
