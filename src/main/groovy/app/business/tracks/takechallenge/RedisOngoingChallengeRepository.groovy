package app.business.tracks.takechallenge

import app.business.tracks.QuestionDeck
import app.business.tracks.SubmittedAnswer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

import java.util.concurrent.TimeUnit

@Repository
class RedisOngoingChallengeRepository implements OngoingChallengeRepository {

    RedisTemplate<String, Integer> redisOps

    @Autowired
    RedisOngoingChallengeRepository(RedisTemplate<String, Integer> redisOps) {
        this.redisOps = redisOps
    }

    @Override
    String newChallenge(QuestionDeck questionDeck) {
        def newId = UUID.randomUUID().toString()
        newId
    }

    @Override
    int submitAnswer(String challengeId, SubmittedAnswer submittedAnswer) {

        def answers = redisOps.boundListOps(challengeId)

        def numberOfAnswers = answers.rightPush(submittedAnswer.optionNo)
        answers.expire(30, TimeUnit.MINUTES)

        numberOfAnswers
    }


    @Override
    List<SubmittedAnswer> submittedAnswers(String challengeId) {
        redisOps.boundListOps(challengeId).range(0, -1).collect { new SubmittedAnswer(it) }
    }
}
