import { useState } from 'react';
import { asistenciaService } from '../../services/asistenciaService';
import { toast } from 'react-toastify';

function AsistenciaManualModal({ idEvento, onAsistenciaRegistrada }) {
  const [idUsuario, setIdUsuario] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      await asistenciaService.registrarAsistencia({
        idEvento: parseInt(idEvento),
        idUsuario: parseInt(idUsuario),
        metodo: 'MANUAL'
      });
      toast.success('¡Asistencia manual registrada!');
      setIdUsuario('');
      if (onAsistenciaRegistrada) {
        onAsistenciaRegistrada();
      }
      const closeBtn = document.querySelector('#asistenciaManualModal .btn-close');
      if (closeBtn) closeBtn.click();
    } catch (err) {
      toast.error(err.message || 'Error al registrar asistencia.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal fade" id="asistenciaManualModal" tabIndex="-1" aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 shadow">
          <div className="modal-header bg-light border-bottom-0 pb-0">
            <h5 className="modal-title fw-bold">Registro de Asistencia Manual</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div className="modal-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label className="form-label text-secondary small fw-medium">ID del Alumno</label>
                <input
                  type="number"
                  className="form-control form-control-lg bg-light"
                  placeholder="Ingrese el ID o Matrícula"
                  value={idUsuario}
                  onChange={(e) => setIdUsuario(e.target.value)}
                  required
                />
                <div className="form-text mt-2 text-muted">
                  Escriba el ID exacto del alumno para registrar su entrada al evento.
                </div>
              </div>
              <div className="d-flex justify-content-end gap-2">
                <button type="button" className="btn btn-light" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" className="btn btn-primary px-4" disabled={loading || !idUsuario}>
                  {loading ? 'Registrando...' : 'Marcar Asistencia'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AsistenciaManualModal;
