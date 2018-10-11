package testtask.planets;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static testtask.JsonMatcher.equalToJson;

public class PlanetsTest extends PlanetsBase {
    private static String path = "planets";

    private int totalCount;

    @BeforeClass()
    public void startAllPlanets() {
        //Local test objects supposed to be loaded here
        totalCount = 61;
    }

    @Test(description = "Perform request w/o parameters which should return first 10 planets")
    public void allPlanets() {
        get(path).then().statusCode(200).
                body("count", is(totalCount)).
                body("next", endsWith("planets/?page=2")).
                body("previous", nullValue()).
                body("results.size()", is(10)).
                body("results.name", not(hasItem(nullValue()))).
                body("results", hasItem(equalToJson(planet2json)));
    }

    @Test(description = "Perform request targeting 1st page, which should return first 10 planets")
    public void allPlanetsFirstPage() {
        given().
                param("page", 1).
        get(path).then().statusCode(200).
                body("count", is(totalCount)).
                body("next", endsWith("planets/?page=2")).
                body("previous", nullValue()).
                body("results.size()", is(10)).
                body("results.name", not(hasItem(nullValue()))).
                body("results", hasItem(equalToJson(planet2json)));
    }

    @Test(description = "Perform request targeting 2nd page, which should return next 10 planets")
    public void allPlanetsSecondPage() {
        given().
                param("page", 2).
        get(path).then().statusCode(200).
                body("count", is(totalCount)).
                body("next", endsWith("planets/?page=3")).
                body("previous", endsWith("planets/?page=1")).
                body("results.size()", is(10)).
                body("results.name", not(hasItem(planet2Name)));
    }

    @Test(description = "Perform request for the last page.")
    public void allPlanetsLastPage() {
        int lastPage = (int) Math.floor(totalCount / 10);
        if (totalCount % 10 != 0)
            lastPage = lastPage + 1;

        given().
                param("page", lastPage).
        get(path).then().statusCode(200).
                body("count", is(totalCount)).
                body("next", nullValue()).
                body("previous", endsWith("planets/?page=" + (lastPage - 1))).
                body("results.size()", is(totalCount % 10)).
                body("results.name", not(hasItem(planet2Name)));
    }

    @Test(description = "Perform request with page out of results range")
    public void allPlanetsWrongPage() {
        int emptyPage = (int) Math.floor(totalCount / 10) + 2;
        given().
                param("page", emptyPage).
        get(path).then().statusCode(404).
                spec(resultsNotFound());
    }

    @Test(description = "Perform request with bad page parameter")
    public void allPlanetsBadPage() {
        given().
                param("page", "snafu").
        get(path).then().statusCode(404).
                spec(resultsNotFound());
    }


}
