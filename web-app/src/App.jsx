import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import { authService } from './services/authService'

import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

// Vistas de Autenticación
import Login from './pages/Login'
import Register from './pages/Register'
import ForgotPassword from './pages/ForgotPassword'

// Dashboards Modulares
import AdminDashboard from './pages/admin/AdminDashboard'
import StudentDashboard from './pages/student/StudentDashboard'
import GlobalErrorBoundary from './components/GlobalErrorBoundary'
import NotFound from './pages/NotFound'

function App() {
  const [loggedUser, setLoggedUser] = useState(() => authService.getCurrentUser())

  const handleLogin = (userData) => {
    setLoggedUser(userData)
  }

  const handleLogout = () => {
    authService.logout()
    setLoggedUser(null)
  }

  return (
    <GlobalErrorBoundary>
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover />
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

          {/* Catch-all 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </GlobalErrorBoundary>
  )
}

export default App
