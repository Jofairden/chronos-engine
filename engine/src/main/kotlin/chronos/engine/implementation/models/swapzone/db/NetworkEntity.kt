package chronos.engine.implementation.models.swapzone.db

import org.jetbrains.exposed.dao.id.LongIdTable

object NetworkEntity : LongIdTable() {
    val name = varchar("name", length = 50)
    val ticker = varchar("ticker", length = 20)
    val network = varchar("network", length = 20)
    val smartContract = varchar("smartContract", length = 100)
}
