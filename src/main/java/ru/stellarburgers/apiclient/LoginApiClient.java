package ru.stellarburgers.apiclient;

import ru.stellarburgers.object.User;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class LoginApiClient {

    @Step("Авторизация курьера")
    public Response loginUser(User user, String endpiont) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(endpiont);

    }
}
