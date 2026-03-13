import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState } from 'react'

// Vistas de Autenticación
import Login from './pages/Login'
import Register from './pages/Register'
import ForgotPassword from './pages/ForgotPassword'

// Dashboards Modulares
import AdminDashboard from './pages/admin/AdminDashboard'
import StudentDashboard from './pages/student/StudentDashboard'

function App() {
  // Estado para simular el usuario logueado.
  // Puede ser null (no logueado), o un objeto con id, nombre y rol ('ADMIN' o 'STUDENT')
  const [loggedUser, setLoggedUser] = useState(null)

  // Funciones simuladas de login/logout
  const handleLogin = (userData) => {
    setLoggedUser(userData)
  }

  const handleLogout = () => {
    setLoggedUser(null)
  }

  return (
    <BrowserRouter>
      <Routes>
        {/* Rutas Públicas (Autenticación) */}
        <Route 
          path="/" 
          element={loggedUser ? <Navigate to={loggedUser.role === 'ADMIN' ? '/admin' : '/estudiante'} /> : <Navigate to="/login" />} 
        />
        <Route 
          path="/login" 
          element={!loggedUser ? <Login onLogin={handleLogin} /> : <Navigate to={loggedUser.role === 'ADMIN' ? '/admin' : '/estudiante'} />} 
        />
        <Route 
          path="/register" 
          element={!loggedUser ? <Register /> : <Navigate to={loggedUser.role === 'ADMIN' ? '/admin' : '/estudiante'} />} 
        />
        <Route 
          path="/forgot-password" 
          element={!loggedUser ? <ForgotPassword /> : <Navigate to={loggedUser.role === 'ADMIN' ? '/admin' : '/estudiante'} />} 
        />

        {/* Rutas Privadas (Admin) */}
        <Route 
          path="/admin/*" 
          element={
            loggedUser && loggedUser.role === 'ADMIN' 
              ? <AdminDashboard loggedUser={loggedUser} onLogout={handleLogout} /> 
              : <Navigate to="/login" />
          } 
        />

        {/* Rutas Privadas (Estudiante) */}
        <Route 
          path="/estudiante/*" 
          element={
            loggedUser && loggedUser.role === 'STUDENT' 
              ? <StudentDashboard loggedUser={loggedUser} onLogout={handleLogout} /> 
              : <Navigate to="/login" />
          } 
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
