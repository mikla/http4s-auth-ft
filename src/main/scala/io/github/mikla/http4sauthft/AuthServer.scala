package io.github.mikla.http4sauthft

import cats.~>
import com.rms.miu.slickcats.DBIOInstances._
import fs2.StreamApp
import io.github.mikla.http4sauthft.controller.AuthController
import io.github.mikla.http4sauthft.repository.UserRepositoryImpl
import io.github.mikla.http4sauthft.service.{AuthService, UserServiceImpl}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import slick.dbio._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.{H2Profile, JdbcBackend, JdbcProfile}

object AuthServer extends StreamApp[Task] {
  def stream(args: List[String], requestShutdown: Task[Unit]) = ServerStream.stream
}

object ServerStream {

  val h2db: JdbcBackend.Database = Database.forConfig("h2mem")

  def userRepository = new UserRepositoryImpl(H2Profile)
  def userService = new UserServiceImpl[Task, DBIO](userRepository, dbioToIo)
  def authService = new AuthService[Task](userService)
  def authController: HttpService[Task] = new AuthController(authService).service

  lazy val dbioToIo = dBIOTransformation(H2Profile, h2db)

  private def dBIOTransformation(profile: JdbcProfile, db: JdbcBackend.Database): DBIO ~> Task = new (DBIO ~> Task) {
    import profile.api._
    override def apply[A](fa: DBIO[A]): Task[A] = Task.fromFuture(db.run(fa.transactionally))
  }

  def stream =
    BlazeBuilder[Task]
      .bindHttp(8080, "0.0.0.0")
      .mountService(authController, "/auth")
      .serve
}
