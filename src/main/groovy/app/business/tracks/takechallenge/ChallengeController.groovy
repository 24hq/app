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

    @RequestMapping(value = "/{challenge}", method = RequestMethod.POST)
    DeferredResult<Map> take(@PathVariable String challenge) {

        def command = new TakeChallengeCommand(challenge)
        def response = commandBus.execute command

        def links = []
        def responseWithLinks = response.map(addLinksIfAny(links, challenge) as Func1)

        deferred(responseWithLinks)
    }

    @RequestMapping(value = "/challenge-answers/{challenge}/{deck}/{question}/{answer}", method = RequestMethod.POST)
    DeferredResult<Map> submitAnswer(
            @PathVariable String challenge,
            @PathVariable int deck,
            @PathVariable int question,
            @PathVariable int answer) {

        def command = new SubmitAnswerCommand(challengeCode: challenge, deckNo: deck, questionNo: question, answerNo: answer)
        def response = commandBus.execute command

        def links = []
        def responseWithLinks = response.map(addLinksIfAny(links, challenge) as Func1)

        deferred(responseWithLinks)
    }

    private Closure<Map> addLinksIfAny(links, challenge) {
        { map ->
            map.'question.answerOptions'.eachWithIndex { _, answerNo ->
                links << linkTo(ChallengeController)
                        .slash('challenge-answers')
                        .slash(challenge)
                        .slash(map.deck)
                        .slash(map.question)
                        .slash(answerNo)
                        .withRel("respond")
            }
            map += [links: links]
            map
        }
    }
}
