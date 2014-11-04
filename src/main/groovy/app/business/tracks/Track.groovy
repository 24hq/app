package app.business.tracks

import static com.google.common.base.Preconditions.checkArgument

class Track {

    String code
    String title
    String description

    Collection<QuestionDeck> decks

    int decksUntilNextLevel(int currentDeckNo) {
        checkArgument(currentDeckNo >= 0 && currentDeckNo < decks.size(), "Deck is out of bounds ($currentDeckNo / ${decks.size()})", '')

        def deck = decks[currentDeckNo]
        def level = deck.level

        def lastDeckWithAtTheSameLevel = decks.findLastIndexOf { it.level == level }


        (lastDeckWithAtTheSameLevel - currentDeckNo) + 1
    }

    int size() {
        decks.size()
    }

}
