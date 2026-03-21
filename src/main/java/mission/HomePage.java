package mission;

import mission.pages.BasePage;

public class HomePage extends BasePage {

    public static void homePage() {
        driver.get(LoadProp.getProperty("url"));
    }
}
