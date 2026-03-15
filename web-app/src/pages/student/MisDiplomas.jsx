import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { authService } from '../../services/authService';

function MisDiplomas() {
  const [diplomas, setDiplomas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDiplomas = async () => {
      try {
        setLoading(true);
        // We'll use a direct fetch here or assuming a diplomaEmitidoService exists... Wait, I didn't create a diplomaEmitidoService.
        // I will do a direct fetch for now.
        const currentUser = authService.getCurrentUser();
        const response = await fetch(`/api/diplomas-emitidos/usuario/${currentUser.id}`, {
          headers: authService.getAuthHeader()
        });

        if (!response.ok) {
            if(response.status === 404) {
                // Not found just means empty
                setDiplomas([]);
            } else {
                throw new Error('Error de servidor al cargar diplomas');
            }
        } else {
            const data = await response.json();
            setDiplomas(data);
        }
      } catch (error) {
        toast.error('Atención: Aún no tienes diplomas o no se pudo cargar la vista.');
      } finally {
        setLoading(false);
      }
    };
    fetchDiplomas();
  }, []);

  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="mb-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
        <div>
          <h2 className="fw-bold mb-1 text-dark">Mis Certificados y Diplomas</h2>
          <p className="text-secondary mb-0">Tus constancias por participación en eventos.</p>
        </div>
      </div>

      <div className="row g-4">
        {loading ? (
          <div className="col-12 text-center py-5">
            <div className="spinner-border text-primary" role="status"></div>
            <p className="mt-2 text-secondary">Buscando tus diplomas...</p>
          </div>
        ) : diplomas.length === 0 ? (
          <div className="col-12 text-center py-5 border rounded-4 bg-light">
            <div className="mx-auto bg-white rounded-circle d-flex align-items-center justify-content-center shadow-sm mb-3" style={{width: '80px', height: '80px'}}>
              <i className="bi bi-award fs-1 text-primary"></i>
            </div>
            <h5 className="fw-bold text-dark mb-2">Aún no tienes certificados</h5>
            <p className="text-secondary opacity-75 max-w-2xl mx-auto">
              Participa en eventos y utiliza nuestros puntos de Check-in para demostrar tu asistencia y ganar certificados oficiales.
            </p>
          </div>
        ) : (
          diplomas.map((emitido) => (
            <div key={emitido.idEmitido} className="col-12 col-md-6 col-lg-4">
              <div 
                className="card h-100 border border-2 shadow-sm rounded-4 overflow-hidden position-relative"
                style={{
                  borderColor: emitido.diploma.diseno === 'default-red' ? '#dc3545' : (emitido.diploma.diseno === 'modern-blue' ? '#0d6efd' : '#ffc107') + ' !important'
                }}
              >
                <div className="card-body p-4 d-flex flex-column align-items-center justify-content-center text-center">
                  <i 
                    className="bi bi-patch-check-fill fs-1 mb-3"
                    style={{ color: emitido.diploma.diseno === 'default-red' ? '#dc3545' : (emitido.diploma.diseno === 'modern-blue' ? '#0d6efd' : '#ffc107') }}
                  ></i>
                  <h5 className="fw-bold text-dark">{emitido.diploma.nombreEvento}</h5>
                  <p className="text-secondary small mb-4 flex-grow-1">
                    Otorgado a {emitido.usuario.nombre} {emitido.usuario.apellidoPaterno}
                  </p>
                  
                  <div className="w-100 d-flex gap-2">
                    <button className="btn btn-primary d-flex align-items-center justify-content-center gap-2 flex-grow-1 fw-medium">
                      <i className="bi bi-download"></i>
                      Descargar PDF
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default MisDiplomas;
