package io.github.mikla.http4sauthft

import io.github.mikla.http4sauthft.TestData.User1
import io.github.mikla.http4sauthft.controller.AuthController
import io.github.mikla.http4sauthft.domain.User
import io.github.mikla.http4sauthft.service.{AuthService, UserService}
import monix.eval.Task
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.duration.Duration

class AuthControllerSpec extends org.specs2.mutable.Specification {

  "Auth controller" >> {
    "return session" >> {
      successLogin()
    }
  }

  private[this] def retUserSession(login: String, password: String): Response[Task] = {
    val getHW = Request[Task](Method.POST, Uri.unsafeFromString(s"/$login/$password"))
    new AuthController[Task](authServiceOk).service.orNotFound(getHW).runSyncUnsafe(Duration.Inf)
  }

  lazy val authServiceOk = new AuthService[Task](new UserService[Task] {
    override def findUser(login: String): Task[Option[User]] = Task.pure(Some(User1))
  })

  private[this] def successLogin(): MatchResult[String] = {
    val loginSession = retUserSession(User1.login, User1.password)
    loginSession.status must beEqualTo(Status.Ok)
    loginSession.as[String].runSyncUnsafe(Duration.Inf) must contain("session")
  }

}
