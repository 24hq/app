package app.business.tracks

import spock.lang.Specification

class QuestionDeckSpec extends Specification {

    def "calculates success rate for a given submitted answers"() {
        when:
        def submittedAnswers = [
                new SubmittedAnswer(0),
                new SubmittedAnswer(0),
                new SubmittedAnswer(1),
                new SubmittedAnswer(1),
                new SubmittedAnswer(2),
                new SubmittedAnswer(2)]

        and:
        def deck = new QuestionDeck()
        deck.questions = [
                questionWithCorrectAnswerNo(0),
                questionWithCorrectAnswerNo(1),
                questionWithCorrectAnswerNo(0),
                questionWithCorrectAnswerNo(1),
                questionWithCorrectAnswerNo(1),
                questionWithCorrectAnswerNo(2)
        ]

        then:
        deck.calculateSuccessRateForGiven(submittedAnswers) == 50
    }

    def questionWithCorrectAnswerNo(int correctAnswerNo) {

        def question = new Question()
        def answerOptions = []

        (0..2).each {
            answerOptions << new AnswerOptions(text: "whatever", correct: it == correctAnswerNo)
        }

        question.answerOptions = answerOptions
        question
    }

}
