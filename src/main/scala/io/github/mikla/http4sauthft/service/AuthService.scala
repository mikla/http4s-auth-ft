package io.github.mikla.http4sauthft.service

import java.util.UUID

import cats.Monad
import cats.data.OptionT
import io.github.mikla.http4sauthft.domain.{AuthError, NotAuthorized, Session}

class AuthService[F[_] : Monad](userService: UserService[F]) {

  def login(login: String, password: String): F[Either[AuthError, Session]] = {
    OptionT(userService.findUser(login)).map(user =>
      if (password == user.password) Right(Session(UUID.randomUUID(), user.login, user.name))
      else Left(NotAuthorized("Wrong login or password."))
    ).getOrElseF(Monad[F].pure(Left(NotAuthorized("User not found."))))
  }

}

object AuthService {
  type Token = String
}