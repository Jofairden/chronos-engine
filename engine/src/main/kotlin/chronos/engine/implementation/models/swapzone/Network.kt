package chronos.engine.implementation.models.swapzone

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@optics
@Serializable
data class NetworkObject(
    @SerializedName("name")
    val name: String,
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("network")
    val network: String,
    @SerializedName("smartContract")
    val smartContract: String
)

@JvmInline
value class NetworkId(val value: Long)