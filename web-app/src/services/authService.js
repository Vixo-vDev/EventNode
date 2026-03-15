import { toast } from 'react-toastify';

const API_URL = '/api';

export const authService = {
  login: async (correo, password) => {
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

    // Almacenar en localStorage
    localStorage.setItem('auth_user', JSON.stringify(userData));
    localStorage.setItem('auth_token', btoa(correo + ':' + password));

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
    toast.info('Sesión cerrada exitosamente');
  },

  getAuthHeader: () => {
    const token = localStorage.getItem('auth_token');
    return token ? { 'Authorization': `Basic ${token}` } : {};
  },
  
  getCurrentUser: () => {
    const userStr = localStorage.getItem('auth_user');
    return userStr ? JSON.parse(userStr) : null;
  }
};
