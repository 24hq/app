package app.business.tracks.takechallenge

import app.business.tracks.QuestionDeck
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

import java.util.concurrent.TimeUnit

@Repository
class RedisOngoingChallengeRepository implements OngoingChallengeRepository {

    RedisTemplate<String, Long> redisOps

    @Autowired
    RedisOngoingChallengeRepository(RedisTemplate<String, Long> redisOps) {
        this.redisOps = redisOps
    }

    @Override
    String newChallenge(QuestionDeck questionDeck) {
        def newId = UUID.randomUUID().toString()
        newId
    }

    @Override
    int submitAnswer(String challengeId, int optionNo) {

        def answers = redisOps.boundListOps(challengeId)

        def numberOfAnswers = answers.rightPush(optionNo)
        answers.expire(30, TimeUnit.MINUTES)

        numberOfAnswers
    }


    @Override
    List<Integer> answers(String challengeId) {
        redisOps.boundListOps(challengeId).range(0, -1)
    }
}
