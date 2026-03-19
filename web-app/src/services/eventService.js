import { authService } from './authService';

const API_URL = '/api';

export const eventService = {
  /**
   * Obtener todos los eventos disponibles.
   * Soporta filtros opcionales: nombre, mes, categoriaId
   */
  getEventos: async (nombre, mes, categoriaId, estado) => {
    const params = new URLSearchParams();
    if (nombre) params.append('nombre', nombre);
    if (mes) params.append('mes', mes);
    if (categoriaId) params.append('categoriaId', categoriaId);
    if (estado) params.append('estado', estado);

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
   * Buscar organizadores por nombre
   */
  buscarOrganizadores: async (nombre = '') => {
    const params = nombre ? `?nombre=${encodeURIComponent(nombre)}` : '';
    const response = await fetch(`${API_URL}/eventos/organizadores${params}`);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al buscar organizadores');
    }

    return response.json();
  },

  /**
   * Crear un nuevo organizador
   */
  crearOrganizador: async (datos) => {
    const response = await fetch(`${API_URL}/eventos/organizadores`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(datos),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al crear organizador');
    }

    return response.json();
  },

  /**
   * Actualizar un organizador existente
   */
  actualizarOrganizador: async (id, datos) => {
    const response = await fetch(`${API_URL}/eventos/organizadores/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(datos),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al actualizar organizador');
    }
    return response.json();
  },

  /**
   * Eliminar un organizador
   */
  eliminarOrganizador: async (id) => {
    const response = await fetch(`${API_URL}/eventos/organizadores/${id}`, {
      method: 'DELETE',
      headers: authService.getAuthHeader(),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al eliminar organizador');
    }
    return response.json();
  },

  /**
   * Crear un nuevo evento
   */
  crearEvento: async (eventoData) => {
    const response = await fetch(`${API_URL}/eventos/crear`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
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
   * Actualizar un evento existente
   */
  actualizarEvento: async (idEvento, eventoData) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}`, {
      method: 'PUT',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
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
   * Cancelar un evento
   */
  cancelarEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}/cancelar`, {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al cancelar evento');
    }

    return response.json();
  },

  /**
   * Eliminar un evento
   */
  eliminarEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}`, {
      method: 'DELETE',
      headers: authService.getAuthHeader(),
    });
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al eliminar evento');
    }
    return response.json();
  },

  /**
   * Obtener un evento por su ID
   */
  getEvento: async (idEvento) => {
    const response = await fetch(`${API_URL}/eventos/${idEvento}`);
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al obtener evento');
    }
    return response.json();
  },
};
