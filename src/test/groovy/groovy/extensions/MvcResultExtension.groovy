package groovy.extensions

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import org.springframework.test.web.servlet.MvcResult

class MvcResultExtension {

    public static ReadContext json(MvcResult mvcResult) {
        JsonPath.parse(mvcResult.response.contentAsString)
    }

}
