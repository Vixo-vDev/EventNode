import { useState, useEffect } from 'react';
import { eventService } from '../../services/eventService';
import { asistenciaService } from '../../services/asistenciaService';
import { toast } from 'react-toastify';
import AsistenciaManualModal from '../../components/modals/AsistenciaManualModal';

function TomaAsistencia() {
  const [eventos, setEventos] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // Estado para el modal
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);

  const fetchEventos = async () => {
    try {
      setLoading(true);
      const data = await eventoService.obtenerTodos();
      // Filtrar aquellos que ya pasaron
      const eventosActivos = data.filter(e => new Date(e.fechaFin) >= new Date());
      setEventos(eventosActivos);
    } catch (error) {
      toast.error(error.message || 'Error al cargar los eventos');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEventos();
  }, []);

  const handleOpenScanner = (idEvento) => {
    // Al ser una pre-integración podemos simular esto o simplemente mostrar error
    // ya que la lectura de QR nativa no fue requerida aún más que en UX
    toast.info("Escáner de QR abriéndose para evento " + idEvento);
  };

  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="mb-4">
        <h2 className="fw-bold mb-1 text-dark">Lector QR y Asistencias</h2>
        <p className="text-secondary mb-0">Selecciona un evento para registrar asistencia manual o por QR.</p>
      </div>

      <div className="row g-4">
        {loading ? (
          <div className="col-12 text-center py-5">
            <div className="spinner-border text-primary" role="status"></div>
            <p className="mt-2 text-secondary">Cargando eventos vigentes...</p>
          </div>
        ) : eventos.length === 0 ? (
          <div className="col-12 text-center py-5 border rounded-4 bg-light">
            <i className="bi bi-calendar-x fs-1 text-secondary mb-3"></i>
            <h5 className="fw-bold text-dark">No hay eventos vigentes</h5>
            <p className="text-secondary">No tienes eventos disponibles para tomar asistencia en este momento.</p>
          </div>
        ) : (
          eventos.map((evento) => (
            <div key={evento.idEvento} className="col-12 col-md-6 col-xl-4">
              <div className="card h-100 border-0 shadow-sm rounded-4 overflow-hidden">
                <div className="card-body p-4 d-flex flex-column">
                  <div className="d-flex align-items-center gap-3 mb-3">
                    <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center p-3">
                      <i className="bi bi-qr-code-scan fs-4"></i>
                    </div>
                    <div>
                      <h5 className="fw-bold mb-0 text-dark">{evento.titulo}</h5>
                      <span className="badge bg-success bg-opacity-10 text-success rounded-pill mt-1 px-3 py-2 fw-medium border border-success border-opacity-25">En curso</span>
                    </div>
                  </div>
                  
                  <div className="d-flex flex-column gap-2 mb-4 mt-2 flex-grow-1">
                    <div className="d-flex align-items-center gap-2 text-secondary small">
                      <i className="bi bi-geo-alt text-primary"></i>
                      <span>{evento.ubicacion}</span>
                    </div>
                    <div className="d-flex align-items-center gap-2 text-secondary small">
                      <i className="bi bi-calendar-event text-primary"></i>
                      <span>Capacidad Mínima: {evento.capacidadMinima}</span>
                    </div>
                  </div>

                  <div className="d-flex gap-2 mt-auto">
                    <button 
                      className="btn btn-outline-primary flex-grow-1 d-flex align-items-center justify-content-center gap-2 fw-medium"
                      onClick={() => handleOpenScanner(evento.idEvento)}
                    >
                      <i className="bi bi-qr-code-scan"></i>
                      Escanear
                    </button>
                    <button 
                      className="btn btn-primary d-flex align-items-center justify-content-center gap-2 fw-medium px-4"
                      onClick={() => setEventoSeleccionado(evento.idEvento)}
                      data-bs-toggle="modal" 
                      data-bs-target="#asistenciaManualModal"
                    >
                      <i className="bi bi-person-check-fill"></i>
                      Manual
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {eventoSeleccionado && (
        <AsistenciaManualModal idEvento={eventoSeleccionado} />
      )}
    </div>
  );
}

export default TomaAsistencia;
