import { BrowserRouter, Routes, Route } from 'react-router-dom'
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
        {/* Rutas de Estudiante */}
        <Route path="/" element={<StudentHome />} />
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
