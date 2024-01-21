package chronos.engine.configuration

import chronos.engine.implementation.models.swapzone.db.NetworkEntity
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration

/**
 * This class is a configuration class that implements the `ApplicationRunner` interface.
 * It is responsible for running a `transaction` and creating the `NetworkEntity` schema
 * using the `SchemaUtils.create` method within its `run` method. Required for configuring the
 * schemas in Exposed
 *
 * @see ApplicationRunner
 * @see SchemaUtils
 */
@Configuration
class SchemaConfiguration : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        transaction {
            SchemaUtils.create(NetworkEntity)
        }
    }
}
