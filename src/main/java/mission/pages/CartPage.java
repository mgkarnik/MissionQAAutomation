package mission.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_BUTTON = By.tagName("button");
    private static final By ITEM_QUANTITY = By.className("cart_quantity");

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public List<WebElement> getCartItems() {
        return cartItems;
    }

    public int getItemQuantity(int index) {
        String quantity = getText(cartItems.get(index).findElement(ITEM_QUANTITY));
        return Integer.parseInt(quantity);
    }

    public void removeItemFromCart(String itemName) {
        for (WebElement item : cartItems) {
            String name = getText(item.findElement(ITEM_NAME));
            if (name.equalsIgnoreCase(itemName)) {
                click(item.findElement(ITEM_BUTTON));
                break;
            }
        }
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void checkout() {
        click(checkoutButton);
    }
}
