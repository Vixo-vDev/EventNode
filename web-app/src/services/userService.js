const API_URL = '/api';

export const userService = {
  getPerfil: async (idUsuario) => {
    const response = await fetch(`${API_URL}/usuarios/${idUsuario}/perfil`);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener el perfil');
    }

    return response.json();
  },

  getUsuarios: async () => {
    const response = await fetch(`${API_URL}/usuarios`);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener los usuarios');
    }

    return response.json();
  },

  crearAdmin: async (datos) => {
    const response = await fetch(`${API_URL}/usuarios/admin`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(datos),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear el administrador');
    }

    return response.json();
  }
};
