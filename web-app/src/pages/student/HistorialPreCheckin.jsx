import { useState, useEffect } from 'react';
import { checkinService } from '../../services/checkinService';
import { toast } from 'react-toastify';

function HistorialPreCheckin() {
  const [registros, setRegistros] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRegistros = async () => {
      try {
        setLoading(true);
        const data = await checkinService.obtenerMisRegistros();
        setRegistros(data);
      } catch (error) {
        toast.error('Error al obtener tu historial de pre-checkin');
      } finally {
        setLoading(false);
      }
    };
    fetchRegistros();
  }, []);

  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="mb-4">
        <h2 className="fw-bold mb-1 text-dark">Mi Historial de Registros</h2>
        <p className="text-secondary mb-0">Revisa los eventos a los que te has inscrito mediante Pre-Checkin.</p>
      </div>

      <div className="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Evento</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Ubicación</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Fecha de Registro</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Estado</th>
              </tr>
            </thead>
            <tbody className="border-top-0">
              {loading ? (
                <tr>
                  <td colSpan="4" className="text-center py-5 text-secondary">
                    <div className="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    Cargando tu historial...
                  </td>
                </tr>
              ) : registros.length === 0 ? (
                <tr>
                  <td colSpan="4" className="text-center py-5 text-secondary">
                    No tienes registros en tu historial. ¡Inscríbete a un evento primero!
                  </td>
                </tr>
              ) : (
                registros.map((reg) => (
                  <tr key={reg.idCheckin}>
                    <td className="px-4 py-3">
                      <div className="d-flex align-items-center gap-2">
                        <div className="bg-primary bg-opacity-10 text-primary rounded-circle p-2 d-flex align-items-center justify-content-center" style={{width: '36px', height: '36px'}}>
                          <i className="bi bi-bookmark-check-fill"></i>
                        </div>
                        <span className="fw-semibold text-dark">{reg.evento.titulo}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-secondary small">
                      <i className="bi bi-geo-alt me-1"></i>
                      {reg.evento.ubicacion}
                    </td>
                    <td className="px-4 py-3 text-secondary small">
                      {new Date(reg.fechaRegistro).toLocaleString()}
                    </td>
                    <td className="px-4 py-3">
                      <span className={`badge ${reg.estado === 'ACTIVO' ? 'bg-success' : 'bg-secondary'} bg-opacity-10 text-${reg.estado === 'ACTIVO' ? 'success' : 'secondary'} rounded-pill px-3 py-2 fw-medium border ${reg.estado === 'ACTIVO' ? 'border-success border-opacity-25' : 'border-secondary border-opacity-25'}`}>
                        {reg.estado}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default HistorialPreCheckin;
