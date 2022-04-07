package ru.stellarburgers.apiclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.model.Ingredient;

import java.lang.reflect.Type;
import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientApiClient {

    @Step("Получение списка ингридиентов (get)")
    public Response getIngredient(String endpoint) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(endpoint);

    }

    @Step("Заполнение списка ингридиентов (List)")
    public List<Ingredient> getAliIngredient(String endpoint) {

        IngredientApiClient ingredients = new IngredientApiClient();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Ingredient>>(){}.getType();

        Response response = ingredients.getIngredient(endpoint);

        String s = response.then().extract().body().asString();
        s = s.substring(23, s.length()-1);

        return gson.fromJson(s, collectionType);

    }

}
