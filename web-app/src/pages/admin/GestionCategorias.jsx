import { useState, useEffect } from 'react';
import { categoriaService } from '../../services/categoriaService';
import { toast } from 'react-toastify';
import CategoriaModal from '../../components/modals/CategoriaModal';

function GestionCategorias() {
  const [categorias, setCategorias] = useState([]);
  const [loading, setLoading] = useState(true);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);

  const fetchCategorias = async () => {
    try {
      setLoading(true);
      const data = await categoriaService.obtenerTodas();
      setCategorias(data);
    } catch (error) {
      toast.error(error.message || 'Error al cargar las categorías');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategorias();
  }, []);

  const handleEdit = (cat) => {
    setCategoriaSeleccionada(cat);
  };

  const clearEdit = () => {
    setCategoriaSeleccionada(null);
  };

  const handleDelete = async (idCategoria) => {
    if (window.confirm('¿Está seguro de que desea eliminar esta categoría?')) {
      try {
        const authDetails = JSON.parse(localStorage.getItem('authDetails'));
        await categoriaService.eliminarCategoria(idCategoria, authDetails);
        toast.success('Categoría eliminada exitosamente');
        fetchCategorias();
      } catch (error) {
        toast.error(error.message || 'Error al eliminar categoría');
      }
    }
  };

  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="d-flex flex-column flex-sm-row justify-content-between align-items-sm-center gap-3 mb-4">
        <div>
          <h2 className="fw-bold mb-1 text-dark">Gestión de Categorías</h2>
          <p className="text-secondary mb-0">Administra las categorías de los eventos.</p>
        </div>
        <button 
          className="btn btn-primary d-inline-flex align-items-center gap-2 px-4 shadow-sm"
          data-bs-toggle="modal" 
          data-bs-target="#categoriaModal"
          onClick={clearEdit}
        >
          <i className="bi bi-plus-lg"></i>
          <span className="fw-medium">Nueva Categoría</span>
        </button>
      </div>

      <div className="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">ID</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase w-100">Nombre</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase text-end">Acciones</th>
              </tr>
            </thead>
            <tbody className="border-top-0">
              {loading ? (
                <tr>
                  <td colSpan="3" className="text-center py-5 text-secondary">
                    <div className="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    Cargando categorías...
                  </td>
                </tr>
              ) : categorias.length === 0 ? (
                <tr>
                  <td colSpan="3" className="text-center py-5 text-secondary">No hay categorías registradas.</td>
                </tr>
              ) : (
                categorias.map((cat) => (
                  <tr key={cat.idCategoria}>
                    <td className="px-4 py-3 fw-medium text-dark">#{cat.idCategoria}</td>
                    <td className="px-4 py-3">
                      <div className="d-flex align-items-center gap-2">
                        <div className="bg-primary bg-opacity-10 text-primary rounded-circle p-2 d-flex align-items-center justify-content-center" style={{width: '32px', height: '32px'}}>
                          <i className="bi bi-tag-fill small"></i>
                        </div>
                        <span className="fw-medium">{cat.nombre}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-end">
                      <div className="d-flex justify-content-end gap-2">
                        <button 
                          className="btn btn-sm btn-outline-primary rounded-3 px-3"
                          title="Editar categoría"
                          data-bs-toggle="modal" 
                          data-bs-target="#categoriaModal"
                          onClick={() => handleEdit(cat)}
                        >
                          <i className="bi bi-pencil-square"></i> Editar
                        </button>
                        <button 
                          className="btn btn-sm btn-outline-danger rounded-3 px-3"
                          title="Eliminar categoría"
                          onClick={() => handleDelete(cat.idCategoria)}
                        >
                          <i className="bi bi-trash"></i> Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <CategoriaModal 
        onCategoryCreated={fetchCategorias} 
        categoriaToEdit={categoriaSeleccionada} 
        onClearEdit={clearEdit}
      />
    </div>
  );
}

export default GestionCategorias;
