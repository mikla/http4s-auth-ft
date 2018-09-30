package io.github.mikla.http4sauthft.domain

sealed trait AuthError {
  def message: String
}

case object PasswordExpired extends AuthError {
  def message: String = "Password expired, please reset it via 'Forgot password' link."
}

case class NotAuthorized(underlyingErrorMessage: String) extends AuthError {
  def message: String = s"Failed to authorize with given credentials. Cause: $underlyingErrorMessage"
}

case class ProtocolError(message: String) extends AuthError
case class UnknownError(message: String) extends AuthError