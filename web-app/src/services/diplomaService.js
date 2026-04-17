import { apiGet, apiPost, apiPut, apiDelete, apiFetch } from './apiHelper'

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: concentra operaciones de diplomas para panel web.
 * Por que existe: unifica llamadas para emision, descarga y consulta sin repetir logica HTTP.
 */
export const diplomaService = {
  crearDiploma: (datos) =>
    apiPost('/diplomas/crear', datos),

  listarDiplomas: () =>
    apiGet('/diplomas/'),

  obtenerDiploma: (idDiploma) =>
    apiGet(`/diplomas/${idDiploma}`),

  emitirDiplomas: async (idDiploma) => {
    const res = await apiFetch(`/diplomas/${idDiploma}/emitir`, { method: 'POST' })
    return res.json()
  },

  descargarDiploma: async (idDiploma, idUsuario) => {
    const res = await apiFetch(`/diplomas/${idDiploma}/descargar/${idUsuario}`, {
      errorMsg: 'Error al descargar diploma',
    })
    return res.blob()
  },

  actualizarDiploma: (idDiploma, datos) =>
    apiPut(`/diplomas/${idDiploma}`, datos),

  eliminarDiploma: (idDiploma) =>
    apiDelete(`/diplomas/${idDiploma}`),

  listarDiplomasEstudiante: (idUsuario) =>
    apiGet(`/diplomas/estudiante/${idUsuario}`),

  previewTemplate: async (datos) => {
    const res = await apiFetch('/diplomas/preview-template', {
      method: 'POST',
      body: JSON.stringify(datos),
    })
    return res.blob()
  },

  previewDiploma: async (idDiploma) => {
    const res = await apiFetch(`/diplomas/${idDiploma}/preview`)
    return res.blob()
  },
}
