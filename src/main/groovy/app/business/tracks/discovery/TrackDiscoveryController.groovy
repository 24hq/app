package app.business.tracks.discovery

import app.infrastructure.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

import static app.infrastructure.rx.Deferrables.deferred

@RestController
@RequestMapping("/tracks")
class TrackDiscoveryController {

    @Autowired
    CommandBus commandBus

    @RequestMapping(method = RequestMethod.GET)
    DeferredResult<Map> discover(@RequestParam int page, @RequestParam int size) {

        def command = new DiscoverTracksCommand(page: page, size: size)
        def response = commandBus.execute command

        deferred(response)
    }


}


