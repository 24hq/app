//package webmvc
//
//import app.BootstrapConfiguration
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.MediaType
//import org.springframework.mock.web.MockHttpServletRequest
//import org.springframework.test.context.ContextConfiguration
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//import org.springframework.test.context.web.WebAppConfiguration
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers
//import org.springframework.web.context.WebApplicationContext
//
//import static org.hamcrest.Matchers.hasSize
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = [BootstrapConfiguration])
//@WebAppConfiguration
//class TakingChallengeWebMvcTest {
//
//    MockMvc mockMvc
//
//    @Autowired
//    private WebApplicationContext context
//
//    @Before
//    void setup() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setRemoteAddr("127.0.0.1");
//
//        mockMvc = webAppContextSetup(context).build()
//    }
//
//    @Test
//    void "take challenge"() {
//        mockMvc.perform(
//                post("/challenges/CLNCDE")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath('$.links', hasSize(1)));
//    }
//
//
//}
