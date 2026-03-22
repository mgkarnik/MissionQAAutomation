package mission.api.endpoints;

import mission.config.ConfigReader;

/**
 * Central store for all API endpoint paths.
 */

public class Endpoints {

    public static final String BASE_URL = ConfigReader.getProperty("api.base.url");

    public static final String USERS = "/users";
    public static final String SINGLE_USER = "/users/{id}";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";



}
