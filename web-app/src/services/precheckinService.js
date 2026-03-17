const API_URL = '/api';

export const precheckinService = {
  inscribirse: async (idUsuario, idEvento) => {
    const response = await fetch(`${API_URL}/precheckin/inscribirse`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idUsuario, idEvento }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al inscribirse');
    }
    return response.json();
  },

  cancelarInscripcion: async (idUsuario, idEvento) => {
    const response = await fetch(`${API_URL}/precheckin/cancelar`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idUsuario, idEvento }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al cancelar inscripción');
    }
    return response.json();
  },

  listarInscritos: async (idEvento) => {
    const response = await fetch(`${API_URL}/precheckin/evento/${idEvento}`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar inscritos');
    }
    return response.json();
  },

  listarMisEventos: async (idUsuario) => {
    const response = await fetch(`${API_URL}/precheckin/usuario/${idUsuario}`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar eventos');
    }
    return response.json();
  },

  contarInscritos: async (idEvento) => {
    const response = await fetch(`${API_URL}/precheckin/evento/${idEvento}/count`);
    if (!response.ok) return 0;
    const data = await response.json();
    return data.count || 0;
  },
};
