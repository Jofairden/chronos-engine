package chronos.engine.implementation.apis.swapzone

import chronos.engine.core.dsl.asLoggable
import chronos.engine.implementation.models.swapzone.NetworkId
import chronos.engine.implementation.models.swapzone.NetworkObject
import chronos.engine.implementation.models.swapzone.db.NetworkEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class SwapzoneService {

    data class NetworkCreateRequest(
        val name: String?,
        val ticker: String,
        val network: String,
        val smartContract: String
    )

    data class NetworkUpdateRequest(
        val name: String?,
        val ticker: String?,
        val network: String?,
        val smartContract: String?
    )

    fun create(request: NetworkCreateRequest): NetworkId {
        asLoggable("Creating new NetworkEntity from request: $request") {
            info()
        }
        return NetworkId(NetworkEntity.insertAndGetId {
            it[name] = request.name ?: ""
            it[ticker] = request.ticker
            it[network] = request.network
            it[smartContract] = request.smartContract
        }.value)
    }

    fun delete(id: NetworkId) {
        asLoggable("Creating new NetworkEntity with id: $id") {
            info()
        }
        NetworkEntity.deleteWhere { NetworkEntity.id eq id.value }
    }

    fun findById(id: NetworkId): NetworkObject? {
        return NetworkEntity.selectAll().where {
            NetworkEntity.id eq id.value
        }.firstOrNull()?.let {
            NetworkObject(
                name = it[NetworkEntity.name],
                ticker = it[NetworkEntity.ticker],
                network = it[NetworkEntity.network],
                smartContract = it[NetworkEntity.smartContract]
            )
        }
    }

    suspend fun getAll() = newSuspendedTransaction(Dispatchers.IO) {
        NetworkEntity.selectAll().map {
            NetworkObject(
                name = it[NetworkEntity.name],
                ticker = it[NetworkEntity.ticker],
                network = it[NetworkEntity.network],
                smartContract = it[NetworkEntity.smartContract],
            )
        }

    }
}