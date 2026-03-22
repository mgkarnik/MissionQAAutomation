package mission.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber TestNG Runner.
 * Entry point for Cucumber execution via TestNG.
 */

@CucumberOptions(
        // Where to find feature files
        features = "src/test/java",

        // Where to find step definitions and hooks
        glue = {
                "mission.steps",
                "mission.hooks"
        },

        // Tags to run - empty means run all scenarios
        tags = "",

        // Report formats
        plugin = {
                "pretty",
                "html:test-output/cucumber-reports/cucumber-pretty.html",
                "json:test-output/cucumber-reports/CucumberTestReport.json",
                "rerun:test-output/cucumber-reports/rerun.txt"
        },

        // Show full stack trace on failure
        monochrome = true
)
public class RunnerTest extends AbstractTestNGCucumberTests {

    /**
     * Enables parallel execution of scenarios.
     * parallel = true runs each scenario in its own thread.
     * ThreadLocal in DriverFactory ensures each thread
     * gets its own browser instance.
     */
    @DataProvider(parallel = false)
    @Override
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
