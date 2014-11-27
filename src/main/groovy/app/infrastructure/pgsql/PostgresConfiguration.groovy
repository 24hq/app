package app.infrastructure.pgsql

import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@EnableTransactionManagement
@Configuration
class PostgresConfiguration {

    @Bean(destroyMethod = "close")
    DataSource dataSource(
            @Value('${jdbc.url:jdbc:postgresql://localhost:5432/postgres}') String url,
            @Value('${jdbc.username:postgres}') String username,
            @Value('${jdbc.password:postgres}') String password) {

        def dataSource = new BasicDataSource()
        dataSource.driverClassName = "org.postgresql.Driver"
        dataSource.url = url
        dataSource.username = username
        dataSource.password = password
        dataSource
    }

    @Bean
    PlatformTransactionManager txManager(DataSource dataSource) {
        new DataSourceTransactionManager(dataSource)
    }

}
