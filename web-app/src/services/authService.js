import { toast } from 'react-toastify';

const API_URL = '/api';

export const authService = {
  login: async (correo, password, rememberMe = false) => {
    const response = await fetch(`${API_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ correo, password }),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al iniciar sesión');
    }

    const data = await response.json();

    const rolMap = {
      'ALUMNO': 'STUDENT',
      'ADMINISTRADOR': 'ADMIN',
      'SUPERADMIN': 'ADMIN',
    };

    const userRol = data.rol ? data.rol.toUpperCase() : '';

    const userData = {
      id: data.idUsuario,
      name: userRol === 'ADMINISTRADOR' || userRol === 'SUPERADMIN' ? 'Administrador' : 'Estudiante',
      role: rolMap[userRol] || 'STUDENT',
      email: correo,
    };

    // Almacenar según preferencia
    const storage = rememberMe ? localStorage : sessionStorage;
    storage.setItem('auth_user', JSON.stringify(userData));
    storage.setItem('auth_token', btoa(correo + ':' + password));

    // Limpiar el otro storage por si acaso quedaron residuos
    const otherStorage = rememberMe ? sessionStorage : localStorage;
    otherStorage.removeItem('auth_user');
    otherStorage.removeItem('auth_token');

    return userData;
  },

  register: async (alumnoData) => {
    const response = await fetch(`${API_URL}/alumnos/registro`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(alumnoData),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || 'Error al registrar la cuenta');
    }

    return response.json();
  },

  logout: () => {
    localStorage.removeItem('auth_user');
    localStorage.removeItem('auth_token');
    sessionStorage.removeItem('auth_user');
    sessionStorage.removeItem('auth_token');
    toast.info('Sesión cerrada exitosamente');
  },

  getAuthHeader: () => {
    const token = localStorage.getItem('auth_token') || sessionStorage.getItem('auth_token');
    return token ? { 'Authorization': `Basic ${token}` } : {};
  },
  
  getCurrentUser: () => {
    const userStr = localStorage.getItem('auth_user') || sessionStorage.getItem('auth_user');
    return userStr ? JSON.parse(userStr) : null;
  }
};
