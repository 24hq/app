package app.business.tracks

import spock.lang.Specification

class TrackSpec extends Specification {

    def decks = [
            new QuestionDeck(level: 1),
            new QuestionDeck(level: 1),
            new QuestionDeck(level: 1),
            new QuestionDeck(level: 2),
            new QuestionDeck(level: 2)]

    def track = new Track(decks: decks)

    def "calculates number of decks until next level (inclusive)"() {
        expect:
        track.decksUntilNextLevel(0) == 3
        track.decksUntilNextLevel(1) == 2
        track.decksUntilNextLevel(2) == 1
        track.decksUntilNextLevel(3) == 2
        track.decksUntilNextLevel(4) == 1
    }

    def "throws IllegalStateException if passed-in deck is out of bounds"() {
        when:
        track.decksUntilNextLevel(currentDeck) == 1

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Deck is out of bounds ($currentDeck / ${decks.size()}) []"

        where:
        currentDeck << [-1, 5]
    }

}
