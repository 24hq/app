package app.business.tracks

import org.springframework.core.io.ClassPathResource
import spock.lang.Specification

class FilesystemTrackRepositorySpec extends Specification {

    def "fails with exception during warm-up if track contains decks that are not ordered ascending by level"() {
        given:
        def repository = new FilesystemTrackRepository(new ClassPathResource('tracks'))

        when:
        repository.warmUp()

        then:
        def e = thrown(DecksAreNotInAscendingOrderException)
        e.trackCode == 'BROKEN'
    }

}
