import { Link } from 'react-router-dom';

function NotFound() {
  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="text-center p-5 bg-white shadow-sm rounded-4 w-100" style={{ maxWidth: '500px' }}>
        <div className="text-muted mb-4">
          <i className="bi bi-file-earmark-x" style={{ fontSize: '4rem' }}></i>
        </div>
        <h1 className="h3 fw-bold mb-3 text-dark">Página no encontrada</h1>
        <p className="text-secondary mb-4">
          La página que estás buscando está vacía o no existe. Por favor verifica la dirección e inténtalo de nuevo.
        </p>
        <Link to="/" className="btn btn-primary px-4 shadow-sm rounded-3">
          <i className="bi bi-house me-2"></i>Volver al Inicio
        </Link>
      </div>
    </div>
  );
}

export default NotFound;
