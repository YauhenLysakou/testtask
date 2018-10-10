package testtask;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashMap;

public class JsonMatcher extends TypeSafeMatcher<Object> {

    private final String expectedJson;
    private String errMsg;

    private JsonMatcher(final String expectedJson) {
        this.expectedJson = expectedJson;
    }

    protected boolean matchesSafely(Object actualJson) {
        try {
            JSONObject actual = new JSONObject((HashMap)actualJson);
            JSONObject expected = new JSONObject(expectedJson);
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception ex) {
            this.errMsg = ex.getMessage();
            return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        if (this.errMsg != null) {
            description.appendText(format(this.errMsg));
        }
    }

    /**
     * Compares json string against expected Json string.
     * @param expected string of expected Json to compare against.
     * @return Hamcrest Matcher for equality check for JsonNodes within RestAssured
     */
    public static JsonMatcher equalToJson(final String expected) {
        return new JsonMatcher(expected);
    }

    private String format(String messOriginal) {
        String mess = "\n\t- - - - -\n";
        mess += messOriginal.replaceAll("\n *; *\n?", "\n\t- - - - -\n");
        mess += "----json-----";
        return mess;
    }

}
