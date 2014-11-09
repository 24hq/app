package app.business.tracks.takechallenge

import app.infrastructure.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import rx.functions.Func1

import static app.infrastructure.rx.Deferrables.deferred
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

@RestController
@RequestMapping("/challenges")
class ChallengeController {

    @Autowired
    CommandBus commandBus

    @RequestMapping(value = "/{track}/{deck}", method = RequestMethod.POST)
    DeferredResult<Map> take(@PathVariable String track, @PathVariable int deck) {

        def command = new TakeChallengeCommand(track, deck)
        def response = commandBus.execute command

        def links = []
        def responseWithLinks = response.map(addLinksIfAny(links) as Func1)

        deferred(responseWithLinks)
    }


    @RequestMapping(value = "/answers/{challenge}/{track}/{deck}/{question}/{answerOption}", method = RequestMethod.POST)
    DeferredResult<Map> submitAnswer(
            @PathVariable String challenge,
            @PathVariable String track,
            @PathVariable int deck,
            @PathVariable int question,
            @PathVariable int answerOption) {

        def command = new SubmitAnswerCommand(challengeId: challenge, trackCode: track, deckNo: deck, questionNo: question, answerOptionNo: answerOption)
        def response = commandBus.execute command

        def links = []
        def responseWithLinks = response.map(addLinksIfAny(links) as Func1)

        deferred(responseWithLinks)
    }

    private Closure<Map> addLinksIfAny(links) {
        { map ->
            map.'question.answerOptions'.eachWithIndex { _, answerNo ->
                links << linkTo(ChallengeController)
                        .slash('answers')
                        .slash(map.'challenge.id')
                        .slash(map.track)
                        .slash(map.deck)
                        .slash(map.question)
                        .slash(answerNo)
                        .withRel("respond")
            }

            if (map.'deck.next') {
                links << linkTo(ChallengeController)
                        .slash(map.track)
                        .slash(map.deck)
                        .withRel("take-challenge")
            }
            map += [links: links]
            map
        }
    }
}
