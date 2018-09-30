package io.github.mikla.http4sauthft.service

import cats.Id
import io.github.mikla.http4sauthft.domain.{NotAuthorized, Session, User}

class AuthServiceSpec extends org.specs2.mutable.Specification {

  "AuthService" >> {
    "Login if login and password match" >> {
      val loginResult = userServiceImplOk.login("any", "qwerty")
      loginResult should beRight.like {
        case Session(sessionId, login, name) if login == "any" => true
       }
    }

    "Return auth error if wrong login or password provided" >> {
      val loginResult = userServiceImplOk.login("any", "wrong")
      loginResult should beLeft(NotAuthorized("Wrong login or password."))
    }

    "Return auth error if user not found" >> {
      val loginResult = userServiceImplFailure.login("guest", "qwerty")
      loginResult should beLeft(NotAuthorized("User not found."))
    }
  }

  val userServiceImplOk = new AuthService(new UserService[Id] {
    override def findUser(login: String): Id[Option[User]] = Some(User("1", "test", "email.com", login, "qwerty"))
  })

  val userServiceImplFailure = new AuthService(new UserService[Id] {
    override def findUser(login: String): Id[Option[User]] = None
  })

}
