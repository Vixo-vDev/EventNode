import { Routes, Route } from 'react-router-dom'
import DashboardLayout from '../../components/DashboardLayout'
import Sidebar from '../../components/Sidebar'

// Vistas Internas de Estudiante
import StudentHome from './StudentHome'
import StudentEvents from './StudentEvents'
import StudentMyEvents from './StudentMyEvents'
import StudentEventDetail from './StudentEventDetail'
import StudentEventEnrolled from './StudentEventEnrolled'
import StudentDiplomas from './StudentDiplomas'
import StudentDiplomaDetail from './StudentDiplomaDetail'
import StudentProfile from './StudentProfile'
import HistorialPreCheckin from './HistorialPreCheckin'
import MisDiplomas from './MisDiplomas'
import CerrarSesionModal from '../../components/modals/CerrarSesionModal'

function StudentDashboard({ loggedUser, onLogout }) {
  const menuItems = [
    { path: '/estudiante', exact: true, label: 'Inicio', icon: 'bi bi-house' },
    { path: '/estudiante/eventos', exact: false, label: 'Eventos', icon: 'bi bi-calendar-event' },
    { path: '/estudiante/historial', exact: false, label: 'Mis Inscripciones', icon: 'bi bi-clock-history' },
    { path: '/estudiante/diplomas', exact: false, label: 'Eventos Asistidos', icon: 'bi bi-award' },
    { path: '/estudiante/mis-diplomas', exact: false, label: 'Mis Diplomas', icon: 'bi bi-patch-check' },
    { path: '/estudiante/perfil', exact: false, label: 'Perfil', icon: 'bi bi-person' }
  ]

  const sidebar = <Sidebar menuItems={menuItems} user={loggedUser} />

  return (
    <DashboardLayout sidebar={sidebar} user={loggedUser}>
      <Routes>
        <Route path="/" element={<StudentHome />} />
        <Route path="/eventos" element={<StudentEvents />} />
        <Route path="/mis-eventos" element={<StudentMyEvents />} />
        <Route path="/evento/:id" element={<StudentEventDetail />} />
        <Route path="/evento/:id/inscrito" element={<StudentEventEnrolled />} />
        <Route path="/historial" element={<HistorialPreCheckin />} />
        <Route path="/diplomas" element={<StudentDiplomas />} />
        <Route path="/mis-diplomas" element={<MisDiplomas />} />
        <Route path="/diplomas/:id" element={<StudentDiplomaDetail />} />
        <Route path="/perfil" element={<StudentProfile user={loggedUser} />} />
      </Routes>
      <CerrarSesionModal onLogout={onLogout} />
    </DashboardLayout>
  )
}

export default StudentDashboard
