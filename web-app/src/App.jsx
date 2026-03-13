import { BrowserRouter, Routes, Route } from 'react-router-dom'

// Auth Pages
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import ForgotPassword from './pages/ForgotPassword'

// Admin Pages
import AdminHome from './pages/admin/AdminHome'
import GestionEventos from './pages/admin/GestionEventos'
import AdminEventDetail from './pages/admin/AdminEventDetail'
import AdminPreCheckIn from './pages/admin/AdminPreCheckIn'
import AdminCheckIn from './pages/admin/AdminCheckIn'
import AdminDiplomas from './pages/admin/AdminDiplomas'
import AdminDiplomaDetail from './pages/admin/AdminDiplomaDetail'
import AdminEstudiantes from './pages/admin/AdminEstudiantes'
import AdminPerfil from './pages/admin/AdminPerfil'

// Student Pages
import StudentHome from './pages/student/StudentHome'
import StudentEvents from './pages/student/StudentEvents'
import StudentMyEvents from './pages/student/StudentMyEvents'
import StudentEventDetail from './pages/student/StudentEventDetail'
import StudentEventEnrolled from './pages/student/StudentEventEnrolled'
import StudentDiplomas from './pages/student/StudentDiplomas'
import StudentDiplomaDetail from './pages/student/StudentDiplomaDetail'
import StudentProfile from './pages/student/StudentProfile'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rutas de Autenticación */}
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />

        {/* Rutas de Administrador */}
        <Route path="/admin" element={<AdminHome />} />
        <Route path="/admin/eventos" element={<GestionEventos />} />
        <Route path="/admin/evento/:id" element={<AdminEventDetail />} />
        <Route path="/admin/evento/:id/pre-check-in" element={<AdminPreCheckIn />} />
        <Route path="/admin/evento/:id/check-in" element={<AdminCheckIn />} />
        <Route path="/admin/diplomas" element={<AdminDiplomas />} />
        <Route path="/admin/diploma/:id" element={<AdminDiplomaDetail />} />
        <Route path="/admin/estudiantes" element={<AdminEstudiantes />} />
        <Route path="/admin/perfil" element={<AdminPerfil />} />

        {/* Rutas de Estudiante */}
        <Route path="/estudiante" element={<StudentHome />} />
        <Route path="/estudiante/eventos" element={<StudentEvents />} />
        <Route path="/estudiante/mis-eventos" element={<StudentMyEvents />} />
        <Route path="/estudiante/evento/:id" element={<StudentEventDetail />} />
        <Route path="/estudiante/evento/:id/inscrito" element={<StudentEventEnrolled />} />
        <Route path="/estudiante/diplomas" element={<StudentDiplomas />} />
        <Route path="/estudiante/diplomas/:id" element={<StudentDiplomaDetail />} />
        <Route path="/estudiante/perfil" element={<StudentProfile />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
