package mission.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CheckoutPage extends BasePage{

    // Customer Info

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    // Order Summary

    @FindBy(className = "inventory_item_price")
    private List<WebElement> itemPrices;

    @FindBy(className = "summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    /**
     * Fills in customer info form
     */
    public void fillFirstName(String firstName) {
        sendKeys(firstNameField, firstName);
    }

    public void fillLastName(String lastName) {
        sendKeys(lastNameField, lastName);
    }

    public void fillPostalCode(String zip) {
        sendKeys(postalCodeField, zip);
    }

    /**
     * Clicks continue button to proceed to order summary
     */
    public void clickContinue() {
        click(continueButton);
    }

    /**
     * Calculates sum of all individual item prices on summary page
     */
    public double calculateItemTotal() {
        double total = 0.0;
        for (WebElement price : itemPrices) {
            String priceText = price.getText().replace("$", "").trim();
            total += Double.parseDouble(priceText);
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Returns the Item Total value shown on the summary page
     */
    public double getDisplayedItemTotal() {
        String text = getText(itemTotalLabel);
        String value = text.replace("Item total: $", "").trim();
        return Double.parseDouble(value);
    }

    /**
     * Returns the Tax value shown on the summary page
     */
    public double getDisplayedTax() {
        String text = getText(taxLabel);
        String value = text.replace("Tax: $", "").trim();
        return Double.parseDouble(value);
    }

    /**
     * Calculates expected tax at given percentage
     */
    public double calculateExpectedTax(double itemTotal, double taxRatePercent) {
        double tax = itemTotal * (taxRatePercent / 100);
        return Math.round(tax * 100.0) / 100.0;
    }
}
