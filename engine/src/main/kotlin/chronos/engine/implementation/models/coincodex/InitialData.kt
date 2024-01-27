package chronos.engine.implementation.models.coincodex

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class InitialData(
  @Contextual
  val date: LocalDateTime,
  @SerializedName("price_usd")
  val priceUsd: Double,
  @SerializedName("price_btc")
  val priceBtc: Double,
  @SerializedName("price_eth")
  val priceEth: Double,
)
