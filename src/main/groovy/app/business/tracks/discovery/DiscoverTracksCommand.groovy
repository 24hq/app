package app.business.tracks.discovery

import app.business.tracks.TrackRepository
import app.infrastructure.Command
import app.infrastructure.CommandHandler
import groovy.transform.Immutable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

import static rx.Observable.just

@Immutable
class DiscoverTracksCommand implements Command<Collection> {

    int page
    int size

    @Component
    static class Handler implements CommandHandler<Collection, DiscoverTracksCommand> {

        @Autowired
        TrackRepository trackRepository

        @Override
        rx.Observable<Collection> handle(DiscoverTracksCommand command) {
            def pageRequest = new PageRequest(command.page, command.size)
            def tracks = trackRepository.list(pageRequest)

            if (!tracks.hasContent()) {
                return rx.Observable.error(new TrackDiscoveryException())
            }

            def response = tracks.content.stream().map({ track ->
                [
                        "title"      : track.title,
                        "description": track.description
                ]
            }).collect()



            just(response)
        }
    }

}
