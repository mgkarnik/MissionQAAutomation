package mission.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import mission.api.endpoints.Endpoints;
import mission.config.ConfigReader;

/**
 * Builds and provides a shared RequestSpecification.
 * All common request settings defined in one place.
 * If base URL, headers, or content type change - change here only.
 */
public class ApiConfig {
    private static final RequestSpecification requestSpec =
            new RequestSpecBuilder()
                    .setBaseUri(Endpoints.BASE_URL)
                    .setContentType(ContentType.JSON)
                    .build();

    public static RequestSpecification getRequestSpec(){
        return requestSpec;
    }
}

