const API_URL = '/api';

const rolMap = {
  'ALUMNO': 'STUDENT',
  'ADMINISTRADOR': 'ADMIN',
  'SUPERADMIN': 'ADMIN',
};

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

    // Mapear la respuesta del backend al formato que espera App.jsx
    // Backend devuelve: { mensaje, rol, idUsuario, nombre, correo }
    // Frontend espera: { id, name, role, email }
    return {
      id: data.idUsuario,
      name: data.nombre || 'Usuario',
      role: rolMap[data.rol] || 'STUDENT',
      originalRole: data.rol,
      email: data.correo || correo,
    };
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
    sessionStorage.removeItem('loggedUser');
  }
};
