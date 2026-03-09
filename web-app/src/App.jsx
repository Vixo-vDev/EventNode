import { BrowserRouter, Routes, Route } from 'react-router-dom'
import AdminHome from './pages/admin/AdminHome'
import AdminPerfil from './pages/admin/AdminPerfil'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rutas de Administrador */}
        <Route path="/" element={<AdminHome />} />
        <Route path="/admin" element={<AdminHome />} />
        <Route path="/admin/perfil" element={<AdminPerfil />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
