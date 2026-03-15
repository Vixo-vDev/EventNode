import { authService } from './authService';

const API_URL = '/api/organizadores';

export const organizadorService = {
  obtenerTodos: async () => {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error('Error al obtener organizadores');
    }
    return response.json();
  },

  crearOrganizador: async (organizadorData) => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(organizadorData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear el organizador');
    }
    return response.json();
  },

  actualizarOrganizador: async (idOrganizador, organizadorData) => {
    const response = await fetch(`${API_URL}/${idOrganizador}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(organizadorData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || errorData.message || 'Error al actualizar organizador');
    }

    return response.json();
  },

  eliminarOrganizador: async (idOrganizador) => {
    const response = await fetch(`${API_URL}/${idOrganizador}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || errorData.message || 'Error al eliminar organizador');
    }

    return response.json();
  }
};
