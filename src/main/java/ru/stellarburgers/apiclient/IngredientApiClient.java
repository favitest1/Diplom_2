package ru.stellarburgers.apiclient;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientApiClient {

    @Step("Получение списка ингридиентов")
    public Response getIngredient(String endpoint) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(endpoint);

    }
}
