package com.github.pool_party.flume

import com.github.pool_party.flume.bot.BotBuilder
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

abstract class AbstractDatabaseBotTest(botBuilder: BotBuilder) : AbstractBotTest(botBuilder) {

    /**
     * Should call `.deleteAll()` on all Exposed database DAOs.
     */
    abstract fun clearDatabases()

    companion object {
        @Container
        private val container = KPostgreSQLContainer().withDatabaseName("database")

        @BeforeAll
        @JvmStatic
        fun databaseConnect() {
            container.start()
            println("Connecting to a database...")
            Database.connect(
                "${container.jdbcUrl}&gssEncMode=disable",
                user = container.username,
                password = container.password
            )
            Flyway.configure().dataSource(container.jdbcUrl, container.username, container.password).load().migrate()
        }
    }

    internal class KPostgreSQLContainer : PostgreSQLContainer<KPostgreSQLContainer>("postgres")
}
