package webmvc

import app.BootstrapConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import static webmvc.RegexMatcher.matchesRegex

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [BootstrapConfiguration])
@WebAppConfiguration
class DiscoverAndTakeChallengeWebMvcTest {

    MockMvc mockMvc

    @Autowired
    private WebApplicationContext context

    @Before
    void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        mockMvc = webAppContextSetup(context).build()
    }

    @Test
    void "discover tracks and take challenge"() {
        def aLink = discoverCleanCodeChallengeLink()

        6.times {
            aLink = followLinkAndRespondToQuestion(aLink)
        }

        respondToLastQuestion(aLink)
    }

    String discoverCleanCodeChallengeLink() {
        def discoverTracks = mockMvc.perform(
                get("/tracks")
                        .param("page", "0")
                        .param("size", "10"))

                .andExpect(request().asyncStarted())
                .andReturn()

        mockMvc.perform(asyncDispatch(discoverTracks))
                .andExpect(jsonPath('.links[0].href').value("http://localhost/challenges/CLNCDE/0"))
                .andReturn()

        "http://localhost/challenges/CLNCDE/0"
    }

    private String followLinkAndRespondToQuestion(String takeChallengeLink) {
        MvcResult mvcResult = asyncRequest(takeChallengeLink)
        String respondWithFirstOptionLink = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(jsonPath('.links[0].rel').value("respond"))
                .andExpect(jsonPath('.links[0].href').value(matchesRegex("http://localhost/challenges/answers/.+/CLNCDE/0/\\d+/0")))
                .andReturn().json().read(".links[0].href")
        respondWithFirstOptionLink
    }

    private def respondToLastQuestion(String respondLink) {
        MvcResult mvcResult = asyncRequest(respondLink)
        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath('$.[deck.passRate]').value(80))
                .andExpect(jsonPath('$.[deck.successRate]').exists())
                .andExpect(jsonPath('$.[deck.accomplishmentMessage]').exists())
    }

    private MvcResult asyncRequest(String cleanCodeChallengeLink) {
        def takeChallenge = mockMvc.perform(
                post(cleanCodeChallengeLink))
                .andExpect(request().asyncStarted())
                .andReturn()
        takeChallenge
    }


}
