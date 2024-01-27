package chronos.engine.implementation.models.coincodex

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Socials(
  val name: String,
  val value: String,
  val label: String,
  @SerializedName("coincodex_socials_id")
  val coincodexSocialsId: Int,
)
