import { apiGet, apiPost, apiPut, apiPatch } from './apiHelper'

export const userService = {
  getPerfil: (idUsuario) =>
    apiGet(`/usuarios/${idUsuario}/perfil`),

  getUsuarios: () =>
    apiGet('/usuarios'),

  crearAdmin: (datos) =>
    apiPost('/usuarios/admin', datos),

  actualizarAlumno: (idUsuario, datos) =>
    apiPut(`/alumnos/${idUsuario}`, datos),

  actualizarPerfil: (idUsuario, datos) =>
    apiPut(`/usuarios/${idUsuario}/perfil`, datos),

  cambiarEstado: async (idUsuario) => {
    const res = await apiPatch(`/usuarios/${idUsuario}/estado`, {})
    return res
  },

  crearAlumno: (datos) =>
    apiPost('/alumnos/registro', datos),
}
