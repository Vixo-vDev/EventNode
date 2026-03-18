import { authService } from './authService';

const API_URL = '/api';

export const diplomaService = {
  crearDiploma: async (datos) => {
    const response = await fetch(`${API_URL}/diplomas/crear`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(datos),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear diploma');
    }
    return response.json();
  },

  listarDiplomas: async () => {
    const response = await fetch(`${API_URL}/diplomas/`, {
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar diplomas');
    }
    return response.json();
  },

  obtenerDiploma: async (idDiploma) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}`, {
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener diploma');
    }
    return response.json();
  },

  emitirDiplomas: async (idDiploma) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}/emitir`, {
      method: 'POST',
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al emitir diplomas');
    }
    return response.json();
  },

  descargarDiploma: async (idDiploma, idUsuario) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}/descargar/${idUsuario}`, {
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      throw new Error('Error al descargar diploma');
    }
    return response.blob();
  },

  actualizarDiploma: async (idDiploma, datos) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(datos),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al actualizar diploma');
    }
    return response.json();
  },

  eliminarDiploma: async (idDiploma) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}`, {
      method: 'DELETE',
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al eliminar diploma');
    }
    return response.json();
  },

  listarDiplomasEstudiante: async (idUsuario) => {
    const response = await fetch(`${API_URL}/diplomas/estudiante/${idUsuario}`, {
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar diplomas');
    }
    return response.json();
  },
};
