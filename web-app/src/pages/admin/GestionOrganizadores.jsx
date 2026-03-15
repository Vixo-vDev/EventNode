import { useState, useEffect } from 'react';
import { organizadorService } from '../../services/organizadorService';
import { toast } from 'react-toastify';
import OrganizadorModal from '../../components/modals/OrganizadorModal';

function GestionOrganizadores() {
  const [organizadores, setOrganizadores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [organizadorSeleccionado, setOrganizadorSeleccionado] = useState(null);

  const fetchOrganizadores = async () => {
    try {
      setLoading(true);
      const data = await organizadorService.obtenerTodos();
      setOrganizadores(data);
    } catch (error) {
      toast.error(error.message || 'Error al cargar los organizadores');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrganizadores();
  }, []);

  const handleEdit = (org) => {
    setOrganizadorSeleccionado(org);
  };

  const clearEdit = () => {
    setOrganizadorSeleccionado(null);
  };

  const handleDelete = async (idOrganizador) => {
    if (window.confirm('¿Está seguro de que desea eliminar este organizador?')) {
      try {
        const authDetails = JSON.parse(localStorage.getItem('authDetails'));
        await organizadorService.eliminarOrganizador(idOrganizador, authDetails);
        toast.success('Organizador eliminado exitosamente');
        fetchOrganizadores();
      } catch (error) {
        toast.error(error.message || 'Error al eliminar organizador');
      }
    }
  };

  return (
    <div className="container-fluid py-4 max-w-7xl mx-auto">
      <div className="d-flex flex-column flex-sm-row justify-content-between align-items-sm-center gap-3 mb-4">
        <div>
          <h2 className="fw-bold mb-1 text-dark">Gestión de Organizadores</h2>
          <p className="text-secondary mb-0">Administra las personas u organizaciones invitadas.</p>
        </div>
        <button 
          className="btn btn-primary d-inline-flex align-items-center gap-2 px-4 shadow-sm"
          data-bs-toggle="modal" 
          data-bs-target="#organizadorModal"
          onClick={clearEdit}
        >
          <i className="bi bi-plus-lg"></i>
          <span className="fw-medium">Registrar Organizador</span>
        </button>
      </div>

      <div className="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Nombre</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase">Correo</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase w-50">Descripción</th>
                <th className="px-4 py-3 text-secondary fw-semibold small text-uppercase text-end">Acciones</th>
              </tr>
            </thead>
            <tbody className="border-top-0">
              {loading ? (
                <tr>
                  <td colSpan="4" className="text-center py-5 text-secondary">
                    <div className="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    Cargando organizadores...
                  </td>
                </tr>
              ) : organizadores.length === 0 ? (
                <tr>
                  <td colSpan="4" className="text-center py-5 text-secondary">No hay organizadores registrados.</td>
                </tr>
              ) : (
                organizadores.map((org) => (
                  <tr key={org.idOrganizador}>
                    <td className="px-4 py-3">
                      <div className="d-flex align-items-center gap-3">
                        <div className="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center fw-bold flex-shrink-0" style={{width: '40px', height: '40px'}}>
                          {org.nombre.charAt(0).toUpperCase()}
                        </div>
                        <span className="fw-medium text-dark">{org.nombre}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-secondary">{org.correo || '-'}</td>
                    <td className="px-4 py-3 text-secondary small text-truncate" style={{maxWidth: '300px'}}>
                      {org.descripcion || '-'}
                    </td>
                    <td className="px-4 py-3 text-end">
                      <div className="d-flex justify-content-end gap-2">
                        <button 
                          className="btn btn-sm btn-outline-primary rounded-3 px-3"
                          title="Editar organizador"
                          data-bs-toggle="modal" 
                          data-bs-target="#organizadorModal"
                          onClick={() => handleEdit(org)}
                        >
                          <i className="bi bi-pencil-square"></i> Editar
                        </button>
                        <button 
                          className="btn btn-sm btn-outline-danger rounded-3 px-3"
                          title="Eliminar organizador"
                          onClick={() => handleDelete(org.idOrganizador)}
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

      <OrganizadorModal 
        onOrganizadorCreated={fetchOrganizadores} 
        organizadorToEdit={organizadorSeleccionado} 
        onClearEdit={clearEdit}
      />
    </div>
  );
}

export default GestionOrganizadores;
