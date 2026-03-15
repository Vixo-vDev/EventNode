import { authService } from './authService';

const API_URL = '/api/checkin';

export const checkinService = {
  obtenerMisRegistros: async () => {
    const response = await fetch(`${API_URL}/mis-registros`, {
      headers: {
        ...authService.getAuthHeader()
      }
    });
    if (!response.ok) {
      throw new Error('Error al obtener tus registros de check-in');
    }
    return response.json();
  },

  obtenerPorEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/evento/${idEvento}`, {
      headers: {
        ...authService.getAuthHeader()
      }
    });
    if (!response.ok) {
      throw new Error('Error al obtener los check-ins del evento');
    }
    return response.json();
  },

  realizarPreCheckin: async (idEvento) => {
    const response = await fetch(`${API_URL}/pre`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify({ idEvento }),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al realizar el pre-checkin');
    }
    return response.json();
  }
};
