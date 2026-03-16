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

    if (data.mensaje) {
      return [];
    }

    return data;
  },

  /**
   * Obtener las categorías disponibles para eventos
   */
  getCategorias: async () => {
    const response = await fetch(`${API_URL}/eventos/categorias`);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al consultar categorías');
    }

    return response.json();
  },

  /**
   * Crear un nuevo evento
   */
  crearEvento: async (eventoData) => {
    const response = await fetch(`${API_URL}/eventos/crear`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(eventoData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear evento');
    }

    return response.json();
  },

  /**
   * Actualizar un evento existente
   */
  actualizarEvento: async (idEvento, eventoData) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(eventoData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al actualizar evento');
    }

    return response.json();
  },

  /**
   * Cancelar un evento
   */
  cancelarEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}/cancelar`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al cancelar evento');
    }

    return response.json();
  },
};
