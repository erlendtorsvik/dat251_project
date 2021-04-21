package hvl.no.dat251.group3project.CucumberTestRunner;

import hvl.no.dat251.group3project.Group3ProjectApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;




@CucumberContextConfiguration
@SpringBootTest(classes = Group3ProjectApplication.class)
public class RunnerOne {

}
