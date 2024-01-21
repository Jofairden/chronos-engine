package chronos.engine.implementation.models.coincodex

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AthData(
    @SerializedName("ath_usd")
    val athUsd: Double,
    @SerializedName("ath_usd_date")
    @Contextual
    val athUsdDate: LocalDateTime,
    @SerializedName("ath_btc")
    val athBtc: Double,
    @SerializedName("ath_btc_date")
    @Contextual
    val athBtcDate: LocalDateTime,
    @SerializedName("ath_eth")
    val athEth: Double,
    @SerializedName("ath_eth_date")
    @Contextual
    val athEthDate: LocalDateTime
)