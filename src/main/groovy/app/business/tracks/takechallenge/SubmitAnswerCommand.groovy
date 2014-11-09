package app.business.tracks.takechallenge

import app.business.tracks.QuestionDeck
import app.business.tracks.TrackRepository
import app.infrastructure.Command
import app.infrastructure.CommandHandler
import groovy.transform.Immutable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static rx.Observable.just

@Immutable
class SubmitAnswerCommand implements Command<Map> {

    String challengeId
    String trackCode

    int deckNo
    int questionNo
    int answerOptionNo


    @Component
    static class Handler implements CommandHandler<Map, SubmitAnswerCommand> {

        @Autowired
        TrackRepository trackRepository

        @Autowired
        OngoingChallengeRepository ongoingChallengeRepository

        @Override
        rx.Observable<Map> handle(SubmitAnswerCommand command) {

            def track = trackRepository.findByCode(command.trackCode)
            def deck = track.decks[command.deckNo]

            def numberOfAnswers = ongoingChallengeRepository.submitAnswer command.challengeId, command.answerOptionNo
            if (numberOfAnswers > deck.size()) {
                throw new NoMoreQuestionsToAnswerException()
            }

            def hasMoreDecks = deck.no < track.size() - 1
            def hasMoreQuestionsInDeck = command.questionNo < deck.size() - 1
            def decksUntilNextLevel = track.decksUntilNextLevel(deck.no)

            if (hasMoreQuestionsInDeck) {
                def nextQuestionNo = command.questionNo + 1
                def nextQuestion = deck.questions[nextQuestionNo]
                return just([
                        "track"                    : track.code,
                        "track.decksUntilNextLevel": decksUntilNextLevel,
                        "deck"                     : deck.no,
                        "deck.title"               : deck.title,
                        "deck.level"               : deck.level,
                        "deck.size"                : deck.size(),
                        "question"                 : nextQuestionNo,
                        "question.title"           : nextQuestion.title,
                        "question.answerOptions"   : nextQuestion.answerOptions.collect { ["text": it.text] },
                        "challenge.id"             : command.challengeId
                ])
            }

            if (decksUntilNextLevel == 0 && hasMoreDecks) {
                return just([
                        "track"     : track.code,
                        "deck"      : deck.no + 1,
                        "deck.next" : true,

                        "level.next": true,
                ])
            }


            if (decksUntilNextLevel > 0) {
                return just([
                        "track"    : track.code,
                        "deck"     : deck.no + 1,
                        "deck.next": true,
                ])
            }

            // sorry, no more decks.
            def answers = ongoingChallengeRepository.answers(command.challengeId)
            just([
                    "deck.successRate": calculateSuccessRate(answers, deck),
                    "track.done"      : true
            ])

        }

        private int calculateSuccessRate(List<Long> answers, QuestionDeck deck) {
            def numberOfCorrectAnswers = deck.questions.collect { question ->
                def correctOptionNo = question.correctAnswerOption().no
                correctOptionNo == answers[question.no]
            }.size()

            (numberOfCorrectAnswers / deck.size()) * 100
        }

    }

}
