package app.business.tracks

class QuestionDeck {

    String title
    long level

    Collection<Question> questions

    int size() {
        questions.size()
    }

}
