package mission.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import mission.api.ApiClient;
import mission.api.models.CreatedUserResponse;
import mission.api.models.UserData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Step definitions for API scenarios.
 */

public class APIStepDefinitions {
    private final ApiClient apiClient = new ApiClient();

    // Shared state between steps within a scenario
    private Response response;
    private List<Integer> allUserIds = new ArrayList<>();
    private int totalUsersCount;

    /**
     * SCENARIO 1 - LIST USERS
     */

    @Given("I get the default list of users for on 1st page")
    public void iGetTheDefaultListOfUsersOnFirstPage() {
        response = apiClient.getUsers(1);
        // Read total users count and total pages from first page response
        totalUsersCount = response.jsonPath().getInt("total");
        int totalPages   = response.jsonPath().getInt("total_pages");

        // Collect user IDs from page 1
        List<Integer> pageOneIds = response.jsonPath().getList("data.id");
        allUserIds.addAll(pageOneIds);
    }

    @When("I get the list of all users within every page")
    public void iGetTheListOfAllUsersWithinEveryPage() {
        // We already have page 1 data, start from page 2
        int totalPages = response.jsonPath().getInt("total_pages");

        for (int page = 2; page <= totalPages; page++) {
            Response pageResponse = apiClient.getUsers(page);
            List<Integer> pageIds = pageResponse.jsonPath().getList("data.id");
            allUserIds.addAll(pageIds);
        }
    }

    @Then("I should see total users count equals the number of user ids")
    public void iShouldSeeTotalUsersCountEqualsNumberOfUserIds() {
        Assert.assertEquals(allUserIds.size(), totalUsersCount,
                "Total users count mismatch. " +
                        "Expected " + totalUsersCount + " user IDs " +
                        "but collected " + allUserIds.size());
    }

    /**
     * SCENARIO 2 & 3 - SINGLE USER
     */

    @Given("I make a search for user {int}")
    public void iMakeASearchForUser(int userId) {
        response = apiClient.getSingleUser(userId);
    }

    @Then("I should see the following user data")
    public void iShouldSeeFollowingUserData(DataTable dataTable) {
        List<Map<String, String>> expectedData = dataTable.asMaps();
        String expectedFirstName = expectedData.get(0).get("first_name");
        String expectedEmail     = expectedData.get(0).get("email");

        // Deserialize response data object into UserData model
        UserData user = response.jsonPath()
                .getObject("data", UserData.class);

        Assert.assertEquals(user.getFirstName(), expectedFirstName,
                "First name mismatch. Expected: " + expectedFirstName
                        + " but found: " + user.getFirstName());

        Assert.assertEquals(user.getEmail(), expectedEmail,
                "Email mismatch. Expected: " + expectedEmail
                        + " but found: " + user.getEmail());
    }

    @Then("I receive error code {int} in response")
    public void iReceiveErrorCodeInResponse(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Status code mismatch. Expected: " + expectedStatusCode
                        + " but received: " + actualStatusCode);
    }

    /**
     * SCENARIO 4 - CREATE USER
     */

    @Given("I create a user with following {word} {word}")
    public void iCreateAUserWithFollowing(String name, String job) {
        response = apiClient.createUser(name, job);
    }

    @Then("response should contain the following data")
    public void responseShouldContainTheFollowingData(DataTable dataTable) {
        // Deserialize response into CreatedUserResponse model
        CreatedUserResponse created = response.as(CreatedUserResponse.class);

        // Get expected field names from the data table header row
        List<String> expectedFields = dataTable.asList();

        // Assert each expected field is present and not null
        for (String field : expectedFields) {
            switch (field) {
                case "name":
                    Assert.assertNotNull(created.getName(),
                            "Field 'name' is null in response");
                    break;
                case "job":
                    Assert.assertNotNull(created.getJob(),
                            "Field 'job' is null in response");
                    break;
                case "id":
                    Assert.assertNotNull(created.getId(),
                            "Field 'id' is null in response");
                    break;
                case "createdAt":
                    Assert.assertNotNull(created.getCreatedAt(),
                            "Field 'createdAt' is null in response");
                    break;
                default:
                    Assert.fail("Unexpected field in test data: " + field);
            }
        }
    }

    /**
     * SCENARIO 5 & 6 - LOGIN
     */

    @Given("I login successfully with the following data")
    public void iLoginSuccessfullyWithTheFollowingData(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps();
        String email    = credentials.get(0).get("Email");
        String password = credentials.get(0).get("Password");
        response = apiClient.loginWithCredentials(email, password);
    }

    @Given("I login unsuccessfully with the following data")
    public void iLoginUnsuccessfullyWithTheFollowingData(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps();
        String email = credentials.get(0).get("Email");
        // Password is intentionally empty in the feature file
        // loginWithoutPassword sends request body without password field
        response = apiClient.loginWithoutPassword(email);
    }

    @Then("I should get a response code of {int}")
    public void iShouldGetAResponseCodeOf(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Response code mismatch. Expected: " + expectedStatusCode
                        + " but received: " + actualStatusCode);
    }

    @And("I should see the following response message:")
    public void iShouldSeeTheFollowingResponseMessage(DataTable dataTable) {
        String expectedMessage = dataTable.asList().get(0);
        String responseBody    = response.getBody().asString();

        Assert.assertTrue(responseBody.contains("Missing password"),
                "Expected error message not found in response. " +
                        "Response body was: " + responseBody);
    }

    /**
     * SCENARIO 7 - DELAYED RESPONSE
     */

    @Given("I wait for the user list to load")
    public void iWaitForTheUserListToLoad() {
        // Reqres.in default delay is 3 seconds
        response = apiClient.getUsersWithDelay(3);
    }

    @Then("I should see that every user has a unique id")
    public void iShouldSeeThatEveryUserHasAUniqueId() {
        List<Integer> userIds = response.jsonPath().getList("data.id");

        // HashSet removes duplicates automatically
        // If sizes differ, duplicates existed
        Set<Integer> uniqueIds = new HashSet<>(userIds);

        Assert.assertEquals(uniqueIds.size(), userIds.size(),
                "Duplicate user IDs found in response. " +
                        "Total IDs: " + userIds.size() +
                        " Unique IDs: " + uniqueIds.size());
    }
}
