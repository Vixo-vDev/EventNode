import { authService } from './authService';

const API_URL = '/api/categorias';

export const categoriaService = {
  obtenerTodas: async () => {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error('Error al obtener categorías');
    }
    return response.json();
  },

  crearCategoria: async (categoriaData) => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(categoriaData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear la categoría');
    }
    return response.json();
  },

  actualizarCategoria: async (idCategoria, categoriaData) => {
    const response = await fetch(`${API_URL}/${idCategoria}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(categoriaData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || errorData.message || 'Error al actualizar categoría');
    }

    return response.json();
  },

  eliminarCategoria: async (idCategoria) => {
    const response = await fetch(`${API_URL}/${idCategoria}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || errorData.message || 'Error al eliminar categoría');
    }

    return response.json();
  }
};
