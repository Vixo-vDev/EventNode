import { authService } from './authService';

const API_URL = '/api/diplomas';

export const diplomaService = {
  configurarDiploma: async (diplomaData) => {
    const response = await fetch(`${API_URL}/configurar`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(diplomaData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al configurar el diploma');
    }
    return response.json();
  }
};
