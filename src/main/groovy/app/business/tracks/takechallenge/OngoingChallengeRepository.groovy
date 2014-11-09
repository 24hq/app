package app.business.tracks.takechallenge

import app.business.tracks.QuestionDeck

interface OngoingChallengeRepository {

    String newChallenge(QuestionDeck questionDeck)

    int submitAnswer(String challengeId, int optionNo)

    List<Integer> answers(String challengeId)
}
