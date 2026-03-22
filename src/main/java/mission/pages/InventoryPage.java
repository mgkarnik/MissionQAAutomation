package mission.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_BUTTON = By.tagName("button");

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    public void addItemsToCart(List<String> itemNames) {
        for (String itemName : itemNames) {
            // Re-fetch inventory items each iteration
            // Avoids StaleElementReferenceException after DOM updates
            List<WebElement> items = driver.findElements(
                    By.className("inventory_item"));
            for (WebElement item : items) {
                String name = item.findElement(ITEM_NAME).getText();
                if (name.equalsIgnoreCase(itemName.trim())) {
                    click(item.findElement(ITEM_BUTTON));
                    break;
                }
            }
        }
    }

    public int getCartBadgeCount() {
        waitForElementVisible(cartBadge);
        return Integer.parseInt(getText(cartBadge));
    }

    public void goToCart() {
        click(cartIcon);
    }
}
