import { Routes, Route } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import Sidebar from '../../components/Sidebar'

// Vistas Internas de Admin
import AdminHome from './AdminHome'
import GestionEventos from './GestionEventos'
import AdminEventDetail from './AdminEventDetail'
import AdminPreCheckIn from './AdminPreCheckIn'
import AdminCheckIn from './AdminCheckIn'
import AdminDiplomas from './AdminDiplomas'
import AdminDiplomaDetail from './AdminDiplomaDetail'
import AdminEstudiantes from './AdminEstudiantes'
import AdminPerfil from './AdminPerfil'
import GestionCategorias from './GestionCategorias'
import GestionOrganizadores from './GestionOrganizadores'
import TomaAsistencia from './TomaAsistencia'
import GestionDiplomas from './GestionDiplomas'
import CerrarSesionModal from '../../components/modals/CerrarSesionModal'

function AdminDashboard({ loggedUser, onLogout }) {
  const menuItems = [
    { path: '/admin', exact: true, label: 'Inicio', icon: 'bi bi-house' },
    { path: '/admin/eventos', exact: false, label: 'Eventos', icon: 'bi bi-calendar-event' },
    { path: '/admin/categorias', exact: false, label: 'Categorías', icon: 'bi bi-tags' },
    { path: '/admin/organizadores', exact: false, label: 'Organizadores', icon: 'bi bi-building' },
    { path: '/admin/asistencia', exact: false, label: 'Asistencia', icon: 'bi bi-qr-code-scan' },
    { path: '/admin/diplomas', exact: true, label: 'Diplomas', icon: 'bi bi-award' },
    { path: '/admin/diplomas/gestion', exact: false, label: 'Plantillas Diplomas', icon: 'bi bi-file-earmark-richtext' },
    { path: '/admin/estudiantes', exact: false, label: 'Estudiantes', icon: 'bi bi-people' },
    { path: '/admin/perfil', exact: false, label: 'Perfil', icon: 'bi bi-person' }
  ]

  const sidebar = <Sidebar menuItems={menuItems} user={loggedUser} />

  return (
    <DashboardLayout sidebar={sidebar} user={loggedUser}>
      <Routes>
        <Route path="/" element={<AdminHome />} />
        <Route path="/eventos" element={<GestionEventos />} />
        <Route path="/categorias" element={<GestionCategorias />} />
        <Route path="/organizadores" element={<GestionOrganizadores />} />
        <Route path="/asistencia" element={<TomaAsistencia />} />
        <Route path="/evento/:id" element={<AdminEventDetail />} />
        <Route path="/evento/:id/pre-check-in" element={<AdminPreCheckIn />} />
        <Route path="/evento/:id/check-in" element={<AdminCheckIn />} />
        <Route path="/diplomas" element={<AdminDiplomas />} />
        <Route path="/diplomas/gestion" element={<GestionDiplomas />} />
        <Route path="/diploma/:id" element={<AdminDiplomaDetail />} />
        <Route path="/estudiantes" element={<AdminEstudiantes />} />
        <Route path="/perfil" element={<AdminPerfil user={loggedUser} />} />
      </Routes>
      <CerrarSesionModal onLogout={onLogout} />
    </DashboardLayout>
  )
}

export default AdminDashboard
