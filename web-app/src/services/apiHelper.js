import { authService } from './authService'

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: fachada unica para llamadas HTTP de la web hacia el backend.
 * Por que existe: estandariza inyeccion de JWT, parseo de errores y metodos REST para toda la UI.
 *
 * Contrato clave: errores del backend usan la propiedad `mensaje`.
 * Autenticacion: authService aporta `Authorization: Bearer <token>` salvo cuando `auth: false`.
 */
const API_URL = '/api'

/**
 * Núcleo de todas las llamadas HTTP del sistema web.
 * @param {string} endpoint  - Ruta relativa sin /api, ej: '/eventos'
 * @param {object} options   - Opciones fetch + custom: { auth: false, errorMsg: '...' }
 */
export async function apiFetch(endpoint, options = {}) {
  const config = { ...options }

  // Inyectar header de autorización JWT (omitir con { auth: false })
  if (options.auth !== false) {
    config.headers = {
      ...config.headers,
      ...authService.getAuthHeader(),
    }
  }

  // Inyectar Content-Type JSON automáticamente si hay body string
  if (config.body && typeof config.body === 'string') {
    config.headers = {
      'Content-Type': 'application/json',
      ...config.headers,
    }
  }

  // Eliminar props custom antes de pasar a fetch nativo
  delete config.auth
  delete config.errorMsg

  const response = await fetch(`${API_URL}${endpoint}`, config)

  if (!response.ok) {
    // El backend siempre devuelve { mensaje: "..." } en errores (ver buildError() en controllers)
    const errorData = await response.json().catch(() => ({}))
    throw new Error(errorData.mensaje || options.errorMsg || 'Error en la solicitud')
  }

  return response
}

/** GET → devuelve JSON parseado */
export async function apiGet(endpoint, options = {}) {
  const res = await apiFetch(endpoint, options)
  return res.json()
}

/** POST con body JSON → devuelve JSON parseado */
export async function apiPost(endpoint, body, options = {}) {
  const res = await apiFetch(endpoint, {
    method: 'POST',
    body: JSON.stringify(body),
    ...options,
  })
  return res.json()
}

/** PUT con body JSON → devuelve JSON parseado */
export async function apiPut(endpoint, body, options = {}) {
  const res = await apiFetch(endpoint, {
    method: 'PUT',
    body: JSON.stringify(body),
    ...options,
  })
  return res.json()
}

/** PATCH con body JSON → devuelve JSON parseado */
export async function apiPatch(endpoint, body, options = {}) {
  const res = await apiFetch(endpoint, {
    method: 'PATCH',
    body: JSON.stringify(body),
    ...options,
  })
  return res.json()
}

/** DELETE → devuelve JSON parseado */
export async function apiDelete(endpoint, options = {}) {
  const res = await apiFetch(endpoint, { method: 'DELETE', ...options })
  return res.json()
}

/**
 * Cierra un modal Bootstrap por su ID.
 * Patrón centralizado para no repetir window.bootstrap.Modal en cada página admin.
 * @param {string} modalId - ID HTML del elemento modal (sin #)
 */
export function closeModal(modalId) {
  const el = document.getElementById(modalId)
  if (el && window.bootstrap) {
    window.bootstrap.Modal.getOrCreateInstance(el).hide()
  }
}
