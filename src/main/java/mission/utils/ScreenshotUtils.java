package mission.utils;

import mission.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Utility class for capturing screenshots during test execution.
 * Single Responsibility: screenshot file handling only.
 */
public class ScreenshotUtils {

    public static void takeScreenshot(WebDriver driver, String scenarioName) {
        String browser = ConfigReader.getProperty("Browser");
        String location = ConfigReader.getProperty("ScreenshotLocation");

        String fileName = scenarioName.replace(" ", "")
                + new Timestamp(new Date().getTime())
                .toString()
                .replaceAll("[^a-zA-Z0-9]", "")
                + "_" + browser + ".jpg";

        // Create screenshots directory if it doesn't exist
        File directory = new File(location);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File srcFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(location + fileName));
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save screenshot: " + fileName, e);
        }
    }
}
