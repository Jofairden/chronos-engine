package chronos.engine.implementation.models.coincodex

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AtlData(
  @SerializedName("atl_usd")
  val atlUsd: Double,
  @SerializedName("atl_usd_date")
  @Contextual
  val atlUsdDate: LocalDateTime,
  @SerializedName("atl_btc")
  val atlBtc: Double,
  @SerializedName("atl_btc_date")
  @Contextual
  val atlBtcDate: LocalDateTime,
  @SerializedName("atl_eth")
  val atlEth: Double,
  @SerializedName("atl_eth_date")
  @Contextual
  val atlEthDate: LocalDateTime,
)
