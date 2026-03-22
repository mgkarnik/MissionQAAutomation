package mission.api;

import io.restassured.response.Response;
import mission.api.endpoints.Endpoints;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

/**
 * API client for https://reqres.in/
 * All HTTP request logic here.
 * Single Responsibility: HTTP interaction with Reqres API only.
 */

public class ApiClient {
    /**
     * Gets a page of users
     */
    public Response getUsers(int pageNumber) {
        return given(ApiConfig.getRequestSpec())
                .queryParam("page", pageNumber)
                .when()
                .get(Endpoints.USERS)
                .then()
                .extract()
                .response();
    }

    /**
     * Gets a single user by ID
     */
    public Response getSingleUser(int userId) {
        return given(ApiConfig.getRequestSpec())
                .pathParam("id", userId)
                .when()
                .get(Endpoints.SINGLE_USER)
                .then()
                .extract()
                .response();
    }

    /**
     * Gets users with a delay
     */
    public Response getUsersWithDelay(int delaySeconds) {
        return given(ApiConfig.getRequestSpec())
                .queryParam("delay", delaySeconds)
                .when()
                .get(Endpoints.USERS)
                .then()
                .extract()
                .response();
    }

    /**
     * POST /users
     */
    public Response createUser(String name, String job) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("job", job);

        return given(ApiConfig.getRequestSpec())
                .body(body.toString())
                .when()
                .post(Endpoints.USERS)
                .then()
                .extract()
                .response();
    }

    /**
     * Login with email and password
     * Expects 200 response with token
     * POST /login
     */
    public Response loginWithCredentials(String email, String password) {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);

        return given(ApiConfig.getRequestSpec())
                .body(body.toString())
                .when()
                .post(Endpoints.LOGIN)
                .then().extract()
                .response();
    }

    /**
     * Login with email only — no password in request body
     * Expects 400 response with "Missing password" error
     * POST /login
     */
    public Response loginWithoutPassword(String email) {
        JSONObject body = new JSONObject();
        body.put("email", email);

        return given(ApiConfig.getRequestSpec())
                .body(body.toString())
                .when()
                .post(Endpoints.LOGIN)
                .then()
                .extract()
                .response();

    }

}
