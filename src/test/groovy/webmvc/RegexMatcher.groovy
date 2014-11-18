package webmvc

import groovy.transform.Immutable
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher;

@Immutable
class RegexMatcher extends TypeSafeMatcher<String> {

    String regex;

    @Override
    void describeTo(Description description) {
        description.appendText "matches regex=`$regex`"
    }

    @Override
    boolean matchesSafely(String string) {
        string.matches regex
    }


    static RegexMatcher matchesRegex(String regex) {
        new RegexMatcher(regex)
    }
}