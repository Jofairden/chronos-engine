package chronos.engine.implementation.models.coincodex

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Coin(
    val symbol: String,
    @SerializedName("coin_name")
    val coinName: String,
    val shortname: String,
    val slug: String,
    @SerializedName("display_symbol")
    val displaySymbol: String,
    @SerializedName("display")
    val display: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("ico_price")
    val icoPrice: Double?,
    @SerializedName("today_open")
    val todayOpen: Int,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("volume_rank")
    val volumeRank: Int,
    @SerializedName("price_high_24_usd")
    val priceHigh24Usd: Double,
    @SerializedName("price_low_24_usd")
    val priceLow24Usd: Double,
    val message: String,
    val website: String,
    val whitepaper: String,
    @SerializedName("total_supply")
    val totalSupply: String,
    val supply: Double,
    val platform: String,
    @SerializedName("how_to_buy_url")
    val howToBuyUrl: String?,
    @SerializedName("last_price_usd")
    val lastPriceUsd: Double,
    @SerializedName("price_change_1H_percent")
    val priceChange1HPercent: Double,
    @SerializedName("price_change_1D_percent")
    val priceChange1DPercent: Double,
    @SerializedName("price_change_7D_percent")
    val priceChange7DPercent: Double,
    @SerializedName("price_change_30D_percent")
    val priceChange30DPercent: Double,
    @SerializedName("price_change_90D_percent")
    val priceChange90DPercent: Double,
    @SerializedName("price_change_180D_percent")
    val priceChange180DPercent: Double,
    @SerializedName("price_change_365D_percent")
    val priceChange365DPercent: Double,
    @SerializedName("price_change_3Y_percent")
    val priceChange3YPercent: Double,
    @SerializedName("price_change_5Y_percent")
    val priceChange5YPercent: Double,
    @SerializedName("price_change_ALL_percent")
    val priceChangeALLPercent: Double,
    @SerializedName("price_change_YTD_percent")
    val priceChangeYTDPercent: Double,
    @SerializedName("volume_24_usd")
    val volume24Usd: Long,
    @SerializedName("trading_since")
    @Contextual
    val tradingSince: LocalDateTime,
    @SerializedName("stages_start")
    @Contextual
    val stagesStart: LocalDateTime?,
    @SerializedName("stages_end")
    @Contextual
    val stagesEnd: LocalDateTime?,
    @SerializedName("include_supply")
    val includeSupply: Boolean,
    @SerializedName("use_volume")
    val useVolume: Boolean,
    @SerializedName("ath_usd")
    val athUsd: Double,
    @SerializedName("ath_date")
    @Contextual
    val athDate: LocalDateTime,
    @SerializedName("not_trading_since")
    @Contextual
    val notTradingSince: LocalDateTime,
    @SerializedName("last_update")
    @Contextual
    val lastUpdate: LocalDateTime,
    @SerializedName("cycle_low_usd")
    val cycleLowUsd: Double,
    @SerializedName("cycle_high_usd")
    val cycleHighUsd: Double,
    @SerializedName("cycle_low_date")
    @Contextual
    val cycleLowDate: LocalDateTime,
    @SerializedName("cycle_high_date")
    @Contextual
    val cycleHighDate: LocalDateTime,
    @SerializedName("image_id")
    val imageId: String,
    @SerializedName("image_t")
    val imageT: String,
    @SerializedName("total_total_supply")
    val totalTotalSupply: String,
    @SerializedName("initial_data")
    val initialData: InitialData,
    @SerializedName("ath_data")
    val athData: AthData,
    @SerializedName("atl_data")
    val atlData: AtlData,
    val social: Social,
    val socials: List<@Contextual Socials>
)