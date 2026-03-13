import RegisterHeader from '../components/RegisterHeader'
import RegisterForm from '../components/RegisterForm'

function Register() {
  return (
    <div className="bg-light min-vh-100 d-flex justify-content-center align-items-center p-3">
      <div className="col-11 col-sm-10 col-md-7 col-lg-6 col-xl-5">
        <div className="card shadow-sm border-0 rounded-4">
          <div className="card-body p-4 p-md-5">
            <RegisterHeader />
            <RegisterForm />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Register
