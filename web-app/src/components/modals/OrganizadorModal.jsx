import { useState, useEffect } from 'react';
import { organizadorService } from '../../services/organizadorService';
import { toast } from 'react-toastify';

function OrganizadorModal({ onOrganizadorCreated, organizadorToEdit, onClearEdit }) {
  const [formData, setFormData] = useState({
    nombre: '',
    descripcion: '',
    correo: ''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (organizadorToEdit) {
      setFormData({
        nombre: organizadorToEdit.nombre || '',
        descripcion: organizadorToEdit.descripcion || '',
        correo: organizadorToEdit.correo || ''
      });
    } else {
      setFormData({ nombre: '', descripcion: '', correo: '' });
    }
  }, [organizadorToEdit]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (organizadorToEdit) {
        await organizadorService.actualizarOrganizador(organizadorToEdit.idOrganizador, formData);
        toast.success('¡Organizador actualizado exitosamente!');
      } else {
        await organizadorService.crearOrganizador(formData);
        toast.success('¡Organizador registrado exitosamente!');
      }
      
      setFormData({ nombre: '', descripcion: '', correo: '' });
      if (onOrganizadorCreated) {
        onOrganizadorCreated();
      }
      
      const closeBtn = document.querySelector('#organizadorModal .btn-close');
      if (closeBtn) closeBtn.click();

      if (onClearEdit) {
        onClearEdit();
      }
    } catch (err) {
      toast.error(err.message || 'Error al procesar organizador.');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setFormData({ nombre: '', descripcion: '', correo: '' });
    if (onClearEdit) {
      onClearEdit();
    }
  };

  return (
    <div className="modal fade" id="organizadorModal" tabIndex="-1" aria-labelledby="organizadorModalLabel" aria-hidden="true" onHiddenCapture={handleClose}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 shadow">
          <div className="modal-header bg-light border-bottom-0 pb-0">
            <h5 className="modal-title fw-bold" id="organizadorModalLabel">
              {organizadorToEdit ? 'Editar Organizador' : 'Registrar Organizador'}
            </h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={handleClose}></button>
          </div>
          <div className="modal-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label text-secondary small fw-medium">Nombre Completo de Organización / Persona</label>
                <input
                  type="text"
                  name="nombre"
                  className="form-control bg-light"
                  value={formData.nombre}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label text-secondary small fw-medium">Correo de Contacto</label>
                <input
                  type="email"
                  name="correo"
                  className="form-control bg-light"
                  value={formData.correo}
                  onChange={handleChange}
                />
              </div>
              <div className="mb-4">
                <label className="form-label text-secondary small fw-medium">Descripción Breve</label>
                <textarea
                  name="descripcion"
                  className="form-control bg-light"
                  rows="3"
                  value={formData.descripcion}
                  onChange={handleChange}
                ></textarea>
              </div>
              <div className="d-flex justify-content-end gap-2">
                <button type="button" className="btn btn-light" data-bs-dismiss="modal" onClick={handleClose}>Cancelar</button>
                <button type="submit" className="btn btn-primary px-4" disabled={loading}>
                  {loading ? 'Guardando...' : (organizadorToEdit ? 'Actualizar' : 'Registrar')}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default OrganizadorModal;
