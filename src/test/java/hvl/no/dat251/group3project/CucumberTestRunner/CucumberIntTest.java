package hvl.no.dat251.group3project.CucumberTestRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/hvl/no/dat251/group3project/AppFeatures"}, plugin = {"pretty"}, strict = true
)
public class CucumberIntTest extends RunnerOne {
}