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

    String trackCode
    int deckNo
    int questionNo
    int answerNo

    @Component
    static class Handler implements CommandHandler<Map, SubmitAnswerCommand> {

        @Autowired
        TrackRepository trackRepository

        @Override
        rx.Observable<Map> handle(SubmitAnswerCommand command) {

            def track = trackRepository.findByCode(command.trackCode)
            def deck = track.decks[command.deckNo]

            def hasMoreDecks = command.deckNo < track.size() - 1
            def hasMoreQuestionsInDeck = command.questionNo < deck.size() - 1
            def decksUntilNextLevel = track.decksUntilNextLevel(command.deckNo)

            if (hasMoreQuestionsInDeck) {
                def nextQuestionNo = command.questionNo + 1
                def nextQuestion = deck.questions[nextQuestionNo]
                return just([
                        "track.decksUntilNextLevel": decksUntilNextLevel,
                        "deck"                     : command.deckNo,
                        "deck.title"               : deck.title,
                        "deck.level"               : deck.level,
                        "deck.size"                : deck.size(),
                        "question"                 : nextQuestionNo,
                        "question.title"           : nextQuestion.title,
                        "question.answerOptions"   : nextQuestion.answerOptions.collect { ["text": it.text] }
                ])
            }

            if (decksUntilNextLevel > 0) {
                return just([
                        "deck"     : command.deckNo + 1,
                        "deck.next": true
                ])
            }

            if (decksUntilNextLevel == 0 && hasMoreDecks) {
                return just([
                        "deck"      : command.deckNo + 1,
                        "deck.next": true,
                        "level.next": true
                ])
            }

            // sorry, no more decks.
            just([
                    "level.done": true
            ])

        }
    }

}
