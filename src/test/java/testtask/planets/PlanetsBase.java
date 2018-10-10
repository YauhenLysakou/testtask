package testtask.planets;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.reporters.Files;
import testtask.BaseTest;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.is;

public abstract class PlanetsBase extends BaseTest {
    String planet2json;
    int planet2_id;
    String planet2Name;

    @BeforeClass()
    public void planetsStart() {
        readTestData();
    }

    /**
     * Shortcut load, from a file, no wrappers.
     * Planet2 is a planet Alderaan with { '_id' : 2 }
     */
    private void readTestData() {
        planet2_id = 2;
        planet2Name = "Alderaan";
        URL resource = getClass().getClassLoader().getResource("planet2.json");
        if (resource != null) {
            try {
                planet2json = Files.readFile(resource.openStream());
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    ResponseSpecification resultsNotFound() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectBody("size()", is(1)).
                expectBody("detail", is("Not found"));
        return builder.build();
    }
}
