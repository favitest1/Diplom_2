package ru.stellarburgers.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.model.User;

import static io.restassured.RestAssured.given;

public class AuthApiClient {

    @Step("Получение данных пользователя")
    public Response getUserData(String token, String endpiont) {

        if (token.startsWith("Bearer")) {
            token = token.replaceFirst("Bearer ", "");
        }

        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .get(endpiont);

    }

    @Step("Обновлвение данных пользователя")
    public Response patchUserData(User user, String endpiont) {

        String token = user.getAccessToken();

        if (token.startsWith("Bearer")) {
            token = token.replaceFirst("Bearer ", "");
        }

        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(endpiont);

    }

}
