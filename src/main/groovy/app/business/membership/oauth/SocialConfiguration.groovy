package app.business.membership.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.social.UserIdSource
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.config.annotation.SocialConfigurer
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository
import org.springframework.social.connect.web.ConnectController
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.social.google.connect.GoogleConnectionFactory
import org.springframework.social.security.AuthenticationNameUserIdSource

import javax.sql.DataSource

@EnableSocial
@Configuration
class SocialConfiguration implements SocialConfigurer {

    @Autowired
    DataSource dataSource

    @Override
    void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        def facebookConnectionFactory = new FacebookConnectionFactory(
                environment.getProperty("facebook.appId", "undefined.facebook.appId"),
                environment.getProperty("facebook.appSecret", "undefined.facebook.appSecret"))


        def googleConnectionFactory = new GoogleConnectionFactory(
                environment.getProperty("google.clientId", "undefined.google.clientId"),
                environment.getProperty("google.clientSecret", "undefined.google.clientSecret"))


        connectionFactoryConfigurer.addConnectionFactory facebookConnectionFactory
        connectionFactoryConfigurer.addConnectionFactory googleConnectionFactory
    }

    @Override
    UserIdSource getUserIdSource() {
        new AuthenticationNameUserIdSource()
    }

    @Override
    UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText())
    }

    @Bean
    ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        new ConnectController(connectionFactoryLocator, connectionRepository)
    }
}