import React from 'react';

class GlobalErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null, errorInfo: null };
  }

  static getDerivedStateFromError(error) {
    // Update state so the next render will show the fallback UI.
    return { hasError: true, error: error };
  }

  componentDidCatch(error, errorInfo) {
    // You can also log the error to an error reporting service
    console.error("ErrorBoundary caught an error:", error, errorInfo);
    this.setState({
      error: error,
      errorInfo: errorInfo
    });
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
          <div className="text-center p-5 bg-white shadow-lg rounded-4 w-100" style={{ maxWidth: '600px' }}>
            <div className="text-danger mb-4">
              <i className="bi bi-exclamation-triangle-fill" style={{ fontSize: '4rem' }}></i>
            </div>
            <h1 className="h3 fw-bold mb-3 text-dark">Ups... Algo salió mal</h1>
            <p className="text-secondary mb-4">
              La aplicación ha encontrado un error inesperado y no puede continuar. Si ves una página vacía, puede ser debido a esto.
            </p>
            
            {this.state.error && (
              <div className="bg-light p-3 rounded-3 text-start mb-4 overflow-auto border border-danger border-opacity-25" style={{ maxHeight: '200px' }}>
                <p className="fw-bold text-danger mb-1 small">Código de Error / Mensaje:</p>
                <code className="text-danger small">{this.state.error.toString()}</code>
                
                {this.state.errorInfo && (
                  <div className="mt-2 text-muted" style={{ fontSize: '0.75rem', whiteSpace: 'pre-wrap' }}>
                    {this.state.errorInfo.componentStack}
                  </div>
                )}
              </div>
            )}
            
            <div className="d-flex gap-2 justify-content-center">
              <button 
                className="btn btn-primary px-4"
                onClick={() => window.location.href = '/'}
              >
                Volver al Inicio
              </button>
              <button 
                className="btn btn-outline-secondary px-4"
                onClick={() => window.location.reload()}
              >
                Recargar Página
              </button>
            </div>
          </div>
        </div>
      );
    }

    return this.props.children; 
  }
}

export default GlobalErrorBoundary;
