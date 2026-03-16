import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { authService } from '../services/authService'
import LoginHeader from '../components/LoginHeader'
import LoginForm from '../components/LoginForm'

function Login({ onLogin }) {
  const [correo, setCorreo] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    setIsLoading(true)

    try {
      const userData = await authService.login(correo, password)
      onLogin(userData)
      toast.success(`¡Bienvenido, ${userData.name}!`)

      if (userData.role === 'ADMIN') {
        navigate('/admin')
      } else {
        navigate('/estudiante')
      }
    } catch (err) {
      setError(err.message)
      toast.error(err.message)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="bg-light min-vh-100 d-flex justify-content-center align-items-center p-3">
      <div className="col-11 col-sm-8 col-md-6 col-lg-5 col-xl-4">
        <div className="card shadow-sm border-0 rounded-4">
          <div className="card-body p-4 p-md-5">
            <LoginHeader />
            <LoginForm
              correo={correo}
              setCorreo={setCorreo}
              password={password}
              setPassword={setPassword}
              error={error}
              isLoading={isLoading}
              onSubmit={handleSubmit}
            />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login
