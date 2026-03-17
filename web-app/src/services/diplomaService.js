const API_URL = '/api';

export const diplomaService = {
  crearDiploma: async (datos) => {
    const response = await fetch(`${API_URL}/diplomas/crear`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(datos),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear diploma');
    }
    return response.json();
  },

  listarDiplomas: async () => {
    const response = await fetch(`${API_URL}/diplomas/`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar diplomas');
    }
    return response.json();
  },

  obtenerDiploma: async (idDiploma) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener diploma');
    }
    return response.json();
  },

  emitirDiplomas: async (idDiploma) => {
    const response = await fetch(`${API_URL}/diplomas/${idDiploma}/emitir`, {
      method: 'POST',
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al emitir diplomas');
    }
    return response.json();
  },

  listarDiplomasEstudiante: async (idUsuario) => {
    const response = await fetch(`${API_URL}/diplomas/estudiante/${idUsuario}`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar diplomas');
    }
    return response.json();
  },
};
