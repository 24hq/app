package app.business.tracks

class QuestionDeck {

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
}
