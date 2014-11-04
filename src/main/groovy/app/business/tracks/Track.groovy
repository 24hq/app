package app.business.tracks

class Track {

    String code
    String title
    String description

    Collection<QuestionDeck> decks

    int size() {
        decks.size()
    }

}
