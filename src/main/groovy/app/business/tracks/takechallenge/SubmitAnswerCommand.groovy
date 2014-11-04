package app.business.tracks.takechallenge

import app.business.tracks.TrackRepository
import app.infrastructure.Command
import app.infrastructure.CommandHandler
import groovy.transform.Immutable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static rx.Observable.just

@Immutable
class SubmitAnswerCommand implements Command<Map> {

    String challengeCode
    int deckNo
    int questionNo
    int answerNo

    @Component
    static class Handler implements CommandHandler<Map, SubmitAnswerCommand> {

        @Autowired
        TrackRepository trackRepository

        @Override
        rx.Observable<Map> handle(SubmitAnswerCommand command) {

            def track = trackRepository.findByCode(command.challengeCode)
            def deck = track.decks[command.deckNo]

            boolean hasMoreDecks = command.deckNo < track.decks.size() - 1
            boolean hasMoreQuestionsInDeck = command.questionNo < deck.questions.size() - 1

            if (hasMoreQuestionsInDeck) {
                def nextQuestionNo = command.questionNo + 1
                def nextQuestion = deck.questions[nextQuestionNo]
                return just([
                        "track.decksUntilNextLevel": track.decksUntilNextLevel(command.deckNo),
                        "deck"                     : command.deckNo,
                        "deck.title"               : deck.title,
                        "deck.level"               : deck.level,
                        "deck.size"                : deck.size(),
                        "question"                 : nextQuestionNo,
                        "question.title"           : nextQuestion.title,
                        "question.answerOptions"   : nextQuestion.answerOptions.collect { ["text": it.text] }
                ])
            }

            if (hasMoreDecks) {
                def nextDeckNo = command.deckNo + 1
                def nextDeck = track.decks[nextDeckNo]
                def nextQuestionNo = 0
                def nextQuestion = nextDeck.questions[nextQuestionNo]
                return just([
                        "track.decksUntilNextLevel": track.decksUntilNextLevel(nextDeckNo),
                        "deck"                     : nextDeckNo,
                        "deck.title"               : nextDeck.title,
                        "deck.level"               : nextDeck.level,
                        "deck.size"                : nextDeck.size(),
                        "deck.done"                : true,
                        "question"                 : 0,
                        "question.title"           : nextQuestion.title,
                        "question.answerOptions"   : nextQuestion.answerOptions.collect { ["text": it.text] }
                ])
            }

            return just([
                    "track.done": true
            ])
        }
    }

}
