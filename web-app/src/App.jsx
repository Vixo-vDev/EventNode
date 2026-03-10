import { BrowserRouter, Routes, Route } from 'react-router-dom'
import AdminHome from './pages/admin/AdminHome'
import GestionEventos from './pages/admin/GestionEventos'
import AdminEventDetail from './pages/admin/AdminEventDetail'
import AdminPreCheckIn from './pages/admin/AdminPreCheckIn'
import AdminCheckIn from './pages/admin/AdminCheckIn'
import AdminDiplomas from './pages/admin/AdminDiplomas'
import AdminDiplomaDetail from './pages/admin/AdminDiplomaDetail'
import AdminEstudiantes from './pages/admin/AdminEstudiantes'
import AdminPerfil from './pages/admin/AdminPerfil'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rutas de Administrador */}
        <Route path="/" element={<AdminHome />} />
        <Route path="/admin" element={<AdminHome />} />
        <Route path="/admin/eventos" element={<GestionEventos />} />
        <Route path="/admin/evento/:id" element={<AdminEventDetail />} />
        <Route path="/admin/evento/:id/pre-check-in" element={<AdminPreCheckIn />} />
        <Route path="/admin/evento/:id/check-in" element={<AdminCheckIn />} />
        <Route path="/admin/diplomas" element={<AdminDiplomas />} />
        <Route path="/admin/diploma/:id" element={<AdminDiplomaDetail />} />
        <Route path="/admin/estudiantes" element={<AdminEstudiantes />} />
        <Route path="/admin/perfil" element={<AdminPerfil />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
