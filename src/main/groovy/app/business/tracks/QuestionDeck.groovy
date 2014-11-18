package app.business.tracks

class QuestionDeck {

    String onSuccess
    String onFailure
    String title
    String trackCode
    int no
    int level

    Collection<Question> questions

    int size() {
        questions.size()
    }

    void setQuestions(Collection<Question> questions) {
        questions.eachWithIndex { question, i ->
            question.no = i
            question.trackCode = trackCode
            question.deckNo = no
        }
        this.questions = questions
    }

    int calculateSuccessRateForGiven(Collection<SubmittedAnswer> submittedAnswers) {
        def numberOfCorrectAnswers = questions.count { question ->
            question.isCorrect(submittedAnswers[question.no])
        }

        (numberOfCorrectAnswers / questions.size()) * 100
    }
}
