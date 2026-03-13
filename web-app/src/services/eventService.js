const API_URL = '/api';

export const eventService = {
  /**
   * Obtener todos los eventos disponibles.
   * Soporta filtros opcionales: nombre, mes, categoriaId
   */
  getEventos: async (nombre, mes, categoriaId) => {
    const params = new URLSearchParams();
    if (nombre) params.append('nombre', nombre);
    if (mes) params.append('mes', mes);
    if (categoriaId) params.append('categoriaId', categoriaId);

    const queryString = params.toString();
    const url = `${API_URL}/eventos${queryString ? `?${queryString}` : ''}`;

    const response = await fetch(url);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al consultar eventos');
    }

    const data = await response.json();

    // Si el backend devuelve {mensaje: "No se encontraron resultados"}, retornar array vacío
    if (data.mensaje) {
      return [];
    }

    return data;
  },

  /**
   * Crear un nuevo evento (requiere autenticación HTTP Basic)
   */
  crearEvento: async (eventoData, credentials) => {
    const response = await fetch(`${API_URL}/eventos/crear`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(`${credentials.email}:${credentials.password}`),
      },
      body: JSON.stringify(eventoData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear evento');
    }

    return response.json();
  },

  /**
   * Actualizar un evento existente (requiere autenticación HTTP Basic)
   */
  actualizarEvento: async (idEvento, eventoData, credentials) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(`${credentials.email}:${credentials.password}`),
      },
      body: JSON.stringify(eventoData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al actualizar evento');
    }

    return response.json();
  },

  /**
   * Cancelar un evento (requiere autenticación HTTP Basic)
   */
  cancelarEvento: async (idEvento, credentials) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}/cancelar`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa(`${credentials.email}:${credentials.password}`),
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al cancelar evento');
    }

    return response.json();
  },
};
