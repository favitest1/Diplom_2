package ru.stellarburgers.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.object.Burger;

import static io.restassured.RestAssured.given;

public class OrderApiClient {

    @Step("Создание заказа")
    public Response postCreateOrder(Burger ingredients, String token, String endpoint) {

        if (token.startsWith("Bearer")) {
            token = token.replaceFirst("Bearer ", "");
        }

        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(ingredients)
                .when()
                .post(endpoint);

    }

    @Step("Получение всех заказов")
    public Response getAllOrders(String endpoint) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(endpoint);

    }

    @Step("Получение заказа")
    public Response getOrders(String token, String endpoint) {

        if (token.startsWith("Bearer")) {
            token = token.replaceFirst("Bearer ", "");
        }

        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(endpoint);

    }

}
