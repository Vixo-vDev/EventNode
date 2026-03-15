import { useState, useEffect } from 'react';
import { categoriaService } from '../../services/categoriaService';
import { toast } from 'react-toastify';

function CategoriaModal({ onCategoryCreated, categoriaToEdit, onClearEdit }) {
  const [nombre, setNombre] = useState('');
  const [loading, setLoading] = useState(false);

  // Si nos pasan una categoria para editar, poblamos el estado
  useEffect(() => {
    if (categoriaToEdit) {
      setNombre(categoriaToEdit.nombre);
    } else {
      setNombre('');
    }
  }, [categoriaToEdit]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (categoriaToEdit) {
        await categoriaService.actualizarCategoria(categoriaToEdit.idCategoria, { nombre });
        toast.success('¡Categoría actualizada exitosamente!');
      } else {
        await categoriaService.crearCategoria({ nombre });
        toast.success('¡Categoría creada exitosamente!');
      }

      setNombre('');
      if (onCategoryCreated) {
        onCategoryCreated();
      }
      
      const closeBtn = document.querySelector('#categoriaModal .btn-close');
      if (closeBtn) closeBtn.click();

      if (onClearEdit) {
        onClearEdit();
      }

    } catch (err) {
      toast.error(err.message || 'Error al procesar la categoría.');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setNombre('');
    if (onClearEdit) {
      onClearEdit();
    }
  };

  return (
    <div className="modal fade" id="categoriaModal" tabIndex="-1" aria-labelledby="categoriaModalLabel" aria-hidden="true" onHiddenCapture={handleClose}>
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content border-0 shadow">
          <div className="modal-header bg-light border-bottom-0 pb-0">
            <h5 className="modal-title fw-bold" id="categoriaModalLabel">
              {categoriaToEdit ? 'Editar Categoría' : 'Nueva Categoría'}
            </h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" onClick={handleClose}></button>
          </div>
          <div className="modal-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label htmlFor="nombreCategoria" className="form-label text-secondary small fw-medium">Nombre de la Categoría</label>
                <input
                  type="text"
                  className="form-control form-control-lg bg-light"
                  id="nombreCategoria"
                  placeholder="Ej. Tecnología"
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value)}
                  required
                />
              </div>
              <div className="d-flex justify-content-end gap-2">
                <button type="button" className="btn btn-light" data-bs-dismiss="modal" onClick={handleClose}>Cancelar</button>
                <button type="submit" className="btn btn-primary px-4" disabled={loading}>
                  {loading ? 'Guardando...' : (categoriaToEdit ? 'Actualizar' : 'Crear Categoría')}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CategoriaModal;
