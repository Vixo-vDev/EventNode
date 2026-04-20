import { apiGet, apiPost, apiPatch } from './apiHelper'

export const asistenciaService = {
  registrarAsistencia: (idUsuario, idEvento, metodo = 'QR') =>
    apiPost('/asistencias/registrar', { idUsuario, idEvento, metodo }),

  registrarManual: (matricula, idEvento) =>
    apiPost('/asistencias/manual', { matricula, idEvento }),

  listarAsistencias: (idEvento,estado = null) => {
    const url = estado
    ? `/asistencias/evento/${idEvento}?estado=${estado}`
    : `/asistencias/evento/${idEvento}`
    return apiGet(url)
  },

  actualizarEstado: (idAsistencia, estado) =>
    apiPatch(`/asistencias/${idAsistencia}/estado`, { estado }),

  contarAsistencias: async (idEvento) => {
    try {
      const data = await apiGet(`/asistencias/evento/${idEvento}/count`)
      return data.totalAsistencias || 0
    } catch {
      return 0
    }
  },
}
