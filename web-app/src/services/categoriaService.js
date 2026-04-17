import { apiGet, apiPost, apiPut, apiDelete } from './apiHelper'

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: servicio web para catalogo de categorias de eventos.
 * Por que existe: centraliza operaciones CRUD para mantener consistencia entre vistas admin.
 */
export const categoriaService = {
  listar: () => apiGet('/categorias'),
  crear: (nombre) => apiPost('/categorias', { nombre }),
  actualizar: (id, nombre) => apiPut(`/categorias/${id}`, { nombre }),
  eliminar: (id) => apiDelete(`/categorias/${id}`),
}
