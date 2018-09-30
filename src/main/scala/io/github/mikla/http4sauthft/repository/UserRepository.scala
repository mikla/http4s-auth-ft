package io.github.mikla.http4sauthft.repository

import io.github.mikla.http4sauthft.domain.User
import slick.dbio._
import slick.jdbc.JdbcProfile

trait UserRepository[F[_]] {
  def findUserQuery(login: String): F[Option[UserRepository.UserRow]]
}

class UserRepositoryImpl(val jdbcProfile: JdbcProfile) extends UserRepository[DBIO] {

  import jdbcProfile.api._

  class Users(tag: Tag) extends Table[UserRepository.UserRow](tag, "users") {

    override def * = (id, name, email, login, password) <> (
      (UserRepository.UserRow.apply _).tupled, UserRepository.UserRow.unapply)

    val id: Rep[String] = column[String]("id", O.PrimaryKey)
    val name: Rep[String] = column[String]("name")
    val email: Rep[String] = column[String]("email")
    val login: Rep[String] = column[String]("login")
    val password: Rep[String] = column[String]("password")

  }

  object UsersQuery extends TableQuery[Users](new Users(_))

  override def findUserQuery(login: String): DBIO[Option[UserRepository.UserRow]] =
    UsersQuery.filter(_.login === login).result.headOption
}

object UserRepository {

  case class UserRow(
    id: String,
    name: String,
    email: String,
    login: String,
    password: String) {

    def toDomain: User = User(id, name, email, login, password)

  }

}