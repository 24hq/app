package app.infrastructure.spring

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BasicConfiguration {

    @Bean
    PropertyPlaceholderConfigurer configurer() {
        new PropertyPlaceholderConfigurer()
    }


}
