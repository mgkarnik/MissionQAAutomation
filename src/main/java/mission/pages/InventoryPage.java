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
            for (WebElement item : inventoryItems) {
                String name = getText(item.findElement(ITEM_NAME));
                if (name.equalsIgnoreCase(itemName)) {
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
