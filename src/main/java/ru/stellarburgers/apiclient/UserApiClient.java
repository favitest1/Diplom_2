package ru.stellarburgers.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.object.User;

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

}
