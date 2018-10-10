package testtask;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected static int WRONG_ID = 9998999;

    @BeforeSuite()
    public void suiteStart() {
        initializeApi();
    }

    private void initializeApi() {
        RestAssured.baseURI = "https://swapi.co/api/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
