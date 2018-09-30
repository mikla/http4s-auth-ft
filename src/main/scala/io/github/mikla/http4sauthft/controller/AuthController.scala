package io.github.mikla.http4sauthft.controller

import cats.effect.Async
import cats.implicits._
import io.circe.Json
import io.github.mikla.http4sauthft.service.AuthService
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class AuthController[F[_] : Async](authService: AuthService[F]) extends Http4sDsl[F] {

  val service: HttpService[F] = {
    HttpService[F] {
      case POST -> Root / name / password =>
        authService.login(name, password).flatMap {
          case Left(error) =>
            Ok(Json.fromString(error.message))
          case Right(session) =>
            Ok(Json.obj("session" -> Json.fromString(session.sessionId.toString)))
        }

    }
  }
}
