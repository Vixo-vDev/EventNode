import { authService } from './authService';

const API_URL = '/api';

export const userService = {
  getPerfil: async (idUsuario) => {
    const response = await fetch(`${API_URL}/usuarios/${idUsuario}/perfil`, {
      headers: { ...authService.getAuthHeader() }
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener el perfil');
    }

    return response.json();
  },

  getUsuarios: async () => {
    const response = await fetch(`${API_URL}/usuarios`, {
      headers: { ...authService.getAuthHeader() }
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener los usuarios');
    }

    return response.json();
  },

  crearAdmin: async (datos) => {
    const response = await fetch(`${API_URL}/usuarios/admin`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(datos),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear el administrador');
    }

    return response.json();
  }
};
