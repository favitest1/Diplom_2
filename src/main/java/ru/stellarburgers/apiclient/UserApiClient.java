package ru.stellarburgers.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.model.User;

import static io.restassured.RestAssured.given;

public class UserApiClient {

    @Step("Создание курьера")
    public Response userCreate(User user, String endpiont) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(endpiont);

    }

    @Step("Удаление курьера")
    public void userDelete(User user, String endpoint) {

        if (user.getAccessToken() == null) {
            return;
        }

        String token = user.getAccessToken();
        if (token.startsWith("Bearer")) {
            token = token.replaceFirst("Bearer ", "");
        }

        given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .when()
                .delete(endpoint)
                .then().statusCode(202);

    }

}
