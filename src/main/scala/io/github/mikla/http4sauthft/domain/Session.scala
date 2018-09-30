package io.github.mikla.http4sauthft.domain

import java.util.UUID

case class Session(
  sessionId: UUID,
  login: String,
  name: String)
