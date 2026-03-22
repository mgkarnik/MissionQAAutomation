package mission.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mission.pages.CartPage;
import mission.pages.CheckoutPage;
import mission.pages.InventoryPage;
import mission.pages.LoginPage;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Step definitions for UI scenarios.
 */
public class UIStepDefinitions {

    // Page objects instantiated once per scenario
    // Driver is already initialized in Hooks @Before

    private final LoginPage loginPage = new LoginPage();
    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();

    private double itemTotal;

    /**
     * LOGIN STEPS
     */
    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        // Navigation already handled in Hooks @Before
    }

    @And("I login in with the following details")
    public void iLoginWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps();
        String username = credentials.get(0).get("userName");
        String password = credentials.get(0).get("Password");
        loginPage.login(username, password);
    }

    /**
     * INVENTORY STEPS
     */

    @And("I add the following items to the basket")
    public void iAddTheFollowingItemsToTheBasket(DataTable dataTable) {
        List<String> itemNames = dataTable.asList(String.class);
        inventoryPage.addItemsToCart(itemNames);
    }

    @And("I should see {int} items added to the shopping cart")
    public void iShouldSeeItemsAddedToShoppingCart(int expectedCount) {
        int actualCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(actualCount, expectedCount,
                "Cart badge count mismatch. Expected: "
                        + expectedCount + " but found: " + actualCount);
    }

    @And("I click on the shopping cart")
    public void iClickOnTheShoppingCart() {
        inventoryPage.goToCart();
    }

    /**
     * CART STEPS
     */

    @And("I verify that the QTY count for each item should be 1")
    public void iVerifyQtyCountForEachItemShouldBe1() {
        List<?> items = cartPage.getCartItems();
        for (int i = 0; i < items.size(); i++) {
            int qty = cartPage.getItemQuantity(i);
            Assert.assertEquals(qty, 1,
                    "Item at index " + i + " has quantity "
                            + qty + " instead of 1");
        }
    }

    @And("I remove the following item:")
    public void iRemoveTheFollowingItem(DataTable dataTable) {
        List<String> itemNames = dataTable.asList(String.class);
        cartPage.removeItemFromCart(itemNames.get(0));
    }

    @And("I click on the CHECKOUT button")
    public void iClickOnTheCheckoutButton() {
        cartPage.checkout();
    }

    /**
     * CHECKOUT STEPS
     */
    @And("I type {string} for First Name")
    public void iTypeForFirstName(String firstName) {
        checkoutPage.fillFirstName(firstName);
    }

    @And("I type {string} for Last Name")
    public void iTypeForLastName(String lastName) {
        checkoutPage.fillLastName(lastName);
    }

    @And("I type {string} for ZIP\\/Postal Code")
    public void iTypeForZipPostalCode(String zipCode) {
        checkoutPage.fillPostalCode(zipCode);
    }

    @When("I click on the CONTINUE button")
    public void iClickOnTheContinueButton() {
        checkoutPage.clickContinue();
    }

    /**
     * ASSERTION STEPS
     */
    @Then("Item total will be equal to the total of items on the list")
    public void itemTotalWillBeEqualToTotalOfItemsOnList() {
        double calculatedTotal = checkoutPage.calculateItemTotal();
        double displayedTotal = checkoutPage.getDisplayedItemTotal();
        Assert.assertEquals(calculatedTotal, displayedTotal,
                "Item total mismatch. Sum of items: "
                        + calculatedTotal + " but page shows: " + displayedTotal);

        // Store for use in tax assertion step
        itemTotal = displayedTotal;
    }

    @And("a Tax rate of {int} % is applied to the total")
    public void aTaxRateIsAppliedToTotal(int taxRate) {
        double expectedTax = checkoutPage.calculateExpectedTax(itemTotal, taxRate);
        double displayedTax = checkoutPage.getDisplayedTax();
        Assert.assertEquals(expectedTax, displayedTax,
                "Tax mismatch. Expected " + taxRate + "% of "
                        + itemTotal + " = " + expectedTax
                        + " but page shows: " + displayedTax);
    }
}
