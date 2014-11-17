package app.business.tracks.takechallenge

import app.business.tracks.QuestionDeck
import app.business.tracks.SubmittedAnswer

interface OngoingChallengeRepository {

    String newChallenge(QuestionDeck questionDeck)

    int submitAnswer(String challengeId, SubmittedAnswer submittedAnswer)

    List<SubmittedAnswer> submittedAnswers(String challengeId)
}
