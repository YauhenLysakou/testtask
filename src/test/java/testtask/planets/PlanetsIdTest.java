package testtask.planets;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static testtask.JsonMatcher.equalToJson;

public class PlanetsIdTest extends PlanetsBase {
    private static String path = "planets/{id}";

    @Test(description = "Get specific planet by planet _id")
    public void getPlanetById() {
        get(path, planet2_id).then().statusCode(200).
                body(".", equalToJson(planet2json));
    }

    @Test(description = "Request a planet by a wrong _id")
    public void getPlanetByWrongId() {
        get(path, WRONG_ID).then().statusCode(404).
                spec(resultsNotFound());
    }

    @Test(description = "Request a planet using bad _id")
    public void getPlanetByBadId() {
        get(path, "snafu").then().statusCode(404).
                spec(resultsNotFound());
    }
}
