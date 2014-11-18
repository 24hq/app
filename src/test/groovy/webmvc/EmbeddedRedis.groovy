package webmvc

import org.junit.rules.ExternalResource
import redis.embedded.RedisServer

class EmbeddedRedis extends ExternalResource {

    def redisServer = new RedisServer(6379)

    @Override
    protected void before() throws Throwable {
        redisServer.start()
    }

    @Override
    protected void after() {
        redisServer.stop()
    }
}
