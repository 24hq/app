package app.business.tracks.discovery

import app.business.tracks.takechallenge.ChallengeController
import app.infrastructure.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import rx.functions.Func1

import static app.infrastructure.rx.Deferrables.deferred
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

@RestController
@RequestMapping("/tracks")
class TrackDiscoveryController {

    @Autowired
    CommandBus commandBus

    @RequestMapping(method = RequestMethod.GET)
    DeferredResult<Map> discover(@RequestParam int page, @RequestParam int size) {

        def command = new DiscoverTracksCommand(page: page, size: size)
        def response = commandBus.execute command

        def links = []
        def responseWithLinks = response.map(addLinksIfAny(links) as Func1)
        deferred(responseWithLinks)
    }


    private Closure<Map> addLinksIfAny(links) {
        { map ->
            map.each { track ->
                links << linkTo(ChallengeController)
                        .slash(track.code)
                        .slash(0)
                        .withRel("take-challenge")
            }
            map += [links: links]
            map
        }
    }

}


