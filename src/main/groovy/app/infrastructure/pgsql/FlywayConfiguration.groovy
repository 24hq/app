package app.infrastructure.pgsql

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource

@Configuration
class FlywayConfiguration {

    @Bean
    Flyway flyway(DataSource dataSource) {
        def flyway = new Flyway()
        flyway.dataSource = dataSource
        flyway.migrate()
        flyway
    }

}
