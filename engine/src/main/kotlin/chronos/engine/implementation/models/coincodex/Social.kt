package chronos.engine.implementation.models.coincodex

import kotlinx.serialization.Serializable

@Serializable
@Suppress("ConstructorParameterNaming")
data class Social(
  val Bitcointalk: String,
  val Explorer: String,
  val Explorer1: String,
  val Explorer2: String,
  val Explorer3: String,
  val Reddit: String,
)
