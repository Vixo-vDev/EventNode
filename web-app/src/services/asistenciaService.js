import { authService } from './authService';

const API_URL = '/api/asistencias';

export const asistenciaService = {
  obtenerPorEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/evento/${idEvento}`, {
      headers: {
        ...authService.getAuthHeader()
      }
    });
    if (!response.ok) {
      throw new Error('Error al obtener las asistencias');
    }
    return response.json();
  },

  registrarAsistencia: async (asistenciaData) => {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(asistenciaData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al registrar la asistencia');
    }
    return response.json();
  }
};
