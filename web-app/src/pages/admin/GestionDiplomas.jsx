import { useState, useEffect } from 'react';
import { eventService } from '../../services/eventService';
import { diplomaService } from '../../services/diplomaService';
import { toast } from 'react-toastify';

function GestionDiplomas() {
  const [eventos, setEventos] = useState([]);
  const [loading, setLoading] = useState(true);
  
  // Formulario del diploma
  const [eventoSeleccionado, setEventoSeleccionado] = useState('');
  const [formData, setFormData] = useState({
    nombreEvento: '',
    firma: '',
    diseno: 'default-red'
  });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const fetchEventos = async () => {
      try {
        setLoading(true);
        const data = await eventoService.obtenerTodos();
        setEventos(data);
      } catch (error) {
        toast.error('Error al cargar eventos para diplomas');
      } finally {
        setLoading(false);
      }
    };
    fetchEventos();
  }, []);

  // Autofill nombre del evento por comodidad
  useEffect(() => {
    if (eventoSeleccionado) {
      const ev = eventos.find(e => e.idEvento.toString() === eventoSeleccionado);
      if (ev) setFormData(prev => ({ ...prev, nombreEvento: ev.titulo }));
    } else {
      setFormData(prev => ({ ...prev, nombreEvento: '' }));
    }
  }, [eventoSeleccionado, eventos]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleGuardarDiploma = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await diplomaService.configurarDiploma({
        idEvento: parseInt(eventoSeleccionado),
        ...formData
      });
      toast.success('Diploma configurado exitosamente.');
      setEventoSeleccionado('');
      setFormData({ nombreEvento: '', firma: '', diseno: 'default-red' });
    } catch (error) {
      toast.error(error.message || 'Error al guardar configuración del diploma');
    } finally {
      setSaving(false);
    }
  };

  // Preview estático basándonos en diseño. 
  // En un caso real renderizaríamos un HTML del diploma.
  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="mb-4">
        <h2 className="fw-bold mb-1 text-dark">Gestión de Diplomas</h2>
        <p className="text-secondary mb-0">Configura la plantilla de diplomas para un evento.</p>
      </div>

      <div className="row g-4">
        <div className="col-12 col-lg-5">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-header bg-white border-bottom-0 pt-4 pb-0 px-4">
              <h5 className="fw-bold mb-0">Configuración Base</h5>
            </div>
            <div className="card-body p-4">
              <form onSubmit={handleGuardarDiploma}>
                <div className="mb-3">
                  <label className="form-label text-secondary small fw-medium">Seleccionar Evento</label>
                  <select 
                    className="form-select bg-light" 
                    value={eventoSeleccionado} 
                    onChange={(e) => setEventoSeleccionado(e.target.value)}
                    required
                  >
                    <option value="">-- Elige un evento --</option>
                    {eventos.map(ev => (
                      <option key={ev.idEvento} value={ev.idEvento}>{ev.titulo}</option>
                    ))}
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label text-secondary small fw-medium">Nombre a mostrar del Evento</label>
                  <input 
                    type="text" 
                    name="nombreEvento"
                    className="form-control bg-light" 
                    value={formData.nombreEvento}
                    onChange={handleChange}
                    placeholder="Certificado de Finalización..."
                    required
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label text-secondary small fw-medium">Firma Autorizada</label>
                  <input 
                    type="text" 
                    name="firma"
                    className="form-control bg-light" 
                    value={formData.firma}
                    onChange={handleChange}
                    placeholder="Ej. Dr. Juan Pérez"
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="form-label text-secondary small fw-medium">Plantilla de Diseño</label>
                  <select 
                    name="diseno"
                    className="form-select bg-light" 
                    value={formData.diseno}
                    onChange={handleChange}
                    required
                  >
                    <option value="default-red">Rojo Corporativo</option>
                    <option value="modern-blue">Azul Moderno</option>
                    <option value="elegant-gold">Dorado Elegante</option>
                  </select>
                </div>

                <button type="submit" className="btn btn-primary w-100 fw-medium" disabled={saving || !eventoSeleccionado || loading}>
                  {saving ? 'Guardando...' : 'Guardar Configuración'}
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-7">
          <div className="card border-0 shadow-sm rounded-4 h-100 bg-light">
            <div className="card-header bg-transparent border-bottom-0 pt-4 pb-0 px-4 text-center">
              <h5 className="fw-bold mb-0 text-secondary">Vista Previa (Borrador)</h5>
            </div>
            <div className="card-body p-4 d-flex align-items-center justify-content-center">
              {eventoSeleccionado ? (
                <div 
                  className={`border border-2 bg-white flex-grow-1 d-flex flex-column align-items-center justify-content-center text-center p-5 position-relative`}
                  style={{ 
                    minHeight: '400px',
                    borderColor: formData.diseno === 'default-red' ? '#dc3545' : (formData.diseno === 'modern-blue' ? '#0d6efd' : '#ffc107') + ' !important',
                    boxShadow: 'inset 0 0 0 10px rgba(0,0,0,0.03)'
                  }}
                >
                  <h1 className="fw-bold mb-4" style={{ fontFamily: 'Georgia, serif', color: formData.diseno === 'default-red' ? '#dc3545' : (formData.diseno === 'modern-blue' ? '#0d6efd' : '#b8860b') }}>CERTIFICADO</h1>
                  <p className="text-secondary fs-5 mb-2">Otorgado a</p>
                  <h2 className="fw-bold border-bottom border-dark pb-2 mb-4 d-inline-block px-5">[Nombre del Alumno]</h2>
                  <p className="text-secondary mb-2">Por su participación destacada en el evento:</p>
                  <h3 className="fw-bold mb-5 text-primary">{formData.nombreEvento || '[Nombre del Evento]'}</h3>
                  
                  <div className="mt-auto d-flex justify-content-center border-top border-secondary pt-3 px-5 w-50 mx-auto">
                    <p className="fw-bold text-dark mb-0">{formData.firma || '[Firma Autorizada]'}</p>
                  </div>
                </div>
              ) : (
                <div className="text-center text-secondary">
                  <i className="bi bi-file-earmark-image fs-1 opacity-50 mb-3 d-block"></i>
                  <p>Selecciona un evento y llena el formulario<br/>para ver la vista previa del diploma.</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default GestionDiplomas;
