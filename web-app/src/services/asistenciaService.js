import { authService } from './authService';

const API_URL = '/api';

export const asistenciaService = {
  registrarAsistencia: async (idUsuario, idEvento, metodo = 'QR') => {
    const response = await fetch(`${API_URL}/asistencias/registrar`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify({ idUsuario, idEvento, metodo }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al registrar asistencia');
    }
    return response.json();
  },

  registrarManual: async (matricula, idEvento) => {
    const response = await fetch(`${API_URL}/asistencias/manual`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify({ matricula, idEvento }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al registrar asistencia');
    }
    return response.json();
  },

  listarAsistencias: async (idEvento) => {
    const response = await fetch(`${API_URL}/asistencias/evento/${idEvento}`, {
      headers: { ...authService.getAuthHeader() }
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al listar asistencias');
    }
    return response.json();
  },

  actualizarEstado: async (idAsistencia, estado) => {
    const response = await fetch(`${API_URL}/asistencias/${idAsistencia}/estado`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify({ estado }),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al actualizar estado');
    }
    return response.json();
  },

  contarAsistencias: async (idEvento) => {
    const response = await fetch(`${API_URL}/asistencias/evento/${idEvento}/count`);
    if (!response.ok) return 0;
    const data = await response.json();
    return data.count || 0;
  },
};
