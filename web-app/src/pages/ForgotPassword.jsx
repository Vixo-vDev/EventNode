import ForgotPasswordHeader from '../components/ForgotPasswordHeader'
import ForgotPasswordForm from '../components/ForgotPasswordForm'

function ForgotPassword() {
  return (
    <div className="bg-light min-vh-100 d-flex justify-content-center align-items-center p-3">
      <div className="col-11 col-sm-8 col-md-6 col-lg-5 col-xl-4">
        <div className="card shadow-sm border-0 rounded-4">
          <div className="card-body p-4 p-md-5">
            <ForgotPasswordHeader />
            <ForgotPasswordForm />
          </div>
        </div>
      </div>
    </div>
  )
}

export default ForgotPassword
