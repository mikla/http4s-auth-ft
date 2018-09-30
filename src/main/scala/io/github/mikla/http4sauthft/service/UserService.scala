package io.github.mikla.http4sauthft.service

import cats.implicits._
import cats.{Monad, ~>}
import io.github.mikla.http4sauthft.domain.User
import io.github.mikla.http4sauthft.repository.UserRepository

trait UserService[F[_]] {
  def findUser(login: String): F[Option[User]]
}

class UserServiceImpl[F[_], DB[_] : Monad](
  userRepository: UserRepository[DB],
  run: DB ~> F) extends UserService[F] {
  override def findUser(login: String): F[Option[User]] = {
    run(userRepository.findUserQuery(login).map(_.map(_.toDomain)))
  }
}
