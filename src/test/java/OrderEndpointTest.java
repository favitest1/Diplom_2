import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.apiclient.IngredientApiClient;
import ru.stellarburgers.apiclient.OrderApiClient;
import ru.stellarburgers.apiclient.UserApiClient;
import ru.stellarburgers.model.Burger;
import ru.stellarburgers.model.Ingredient;
import ru.stellarburgers.model.User;

import static org.hamcrest.Matchers.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;


public class OrderEndpointTest {

    private User user;

    private UserApiClient userApiClient = new UserApiClient();

    private List<Ingredient> ingredientsList;

    private ArrayList<Object> ingredients = new ArrayList<Object>();

    private Response responseCreateOrder;

    @Before
    @Step("Создание пользователя перед тестом, получение списка ингридиентов")
    public void setUp() {
        RestAssured.baseURI= BaseURI.BASE_URI;
        createUniqueUser();
        getAliIngredient();
        responseCreateOrder = createOrder();
    }

    @After
    @Step("Удаление пользователя")
    public void deleteUser() {
        userApiClient.userDelete(user, BaseURI.AUTH_USER_ENDPOINT);
    }

    @Step("Получение списка ингридиентов")
    private void getAliIngredient() {

        IngredientApiClient ingredients = new IngredientApiClient();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Ingredient>>(){}.getType();

        Response response = ingredients.getIngredient(BaseURI.INGREDIENTS_ENDPOINT);

        String s = response.then().extract().body().asString();
        s = s.substring(23, s.length()-1);

        ingredientsList = gson.fromJson(s, collectionType);

    }

    @Test
    @DisplayName("Создание заказа с авторизацией с ингридиентами")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkCreateOrderWithTokenWithIngredientsTest() {

        //Аssert
        responseCreateOrder.then().assertThat().statusCode(200);
        responseCreateOrder.then().assertThat().body("success", equalTo(true));
        responseCreateOrder.then().assertThat().body("name", notNullValue());
        responseCreateOrder.then().assertThat().body("order", notNullValue());
        responseCreateOrder.then().assertThat().body("order.ingredients", notNullValue());

    }

    @Test
    @DisplayName("Создание заказа без авторизации, с ингридиентами")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkCreateOrderWithoutTokenWithIngredientsTest() {

        //Аrrange
        String token = "";
        OrderApiClient orderApiClient = new OrderApiClient();

        //Аct
        for (Ingredient ingredient : ingredientsList) {
            ingredients.add(ingredient.getId());
        }

        Burger burger = new Burger(ingredients);
        Response response = orderApiClient.postCreateOrder(burger, token, BaseURI.ORDERS_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("name", notNullValue());
        response.then().assertThat().body("order", notNullValue());

    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингридиентов")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkCreateOrderWithTokenWithoutIngredientsTest() {

        //Аrrange
        String token = user.getAccessToken();
        OrderApiClient orderApiClient = new OrderApiClient();

        //Аct
        Burger burger = new Burger(new ArrayList<Object>());
        Response response = orderApiClient.postCreateOrder(burger, token, BaseURI.ORDERS_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalToIgnoringCase("Ingredient ids must be provided"));

    }

    @Test
    @DisplayName("Создание заказа без авторизации, без ингридиентов")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkCreateOrderWithoutTokenWithoutIngredientsTest() {

        //Аrrange
        String token = "";
        OrderApiClient orderApiClient = new OrderApiClient();

        //Аct
        Burger burger = new Burger(new ArrayList<Object>());
        Response response = orderApiClient.postCreateOrder(burger, token, BaseURI.ORDERS_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(400);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalToIgnoringCase("Ingredient ids must be provided"));

    }

    @Test
    @DisplayName("Создание заказа с авторизацией, с неверными")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkCreateOrderWithTokenWitWrongIngredientsTest() {

        //Аrrange
        String token = user.getAccessToken();
        OrderApiClient orderApiClient = new OrderApiClient();
        ingredients.add(RandomStringUtils.randomAlphabetic(10));
        ingredients.add(RandomStringUtils.randomAlphabetic(10));
        ingredients.add(RandomStringUtils.randomAlphabetic(10));

        //Аct
        Burger burger = new Burger(ingredients);
        Response response = orderApiClient.postCreateOrder(burger, token, BaseURI.ORDERS_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(500);

    }

    @Test
    @DisplayName("Получение всех заказов")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkGetAllOrdersTest() {

        //Аrrange
        OrderApiClient orderApiClient = new OrderApiClient();

        //Аct
        Response response = orderApiClient.getAllOrders(BaseURI.ORDERS_ALL_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("orders", notNullValue());

    }

    @Test
    @DisplayName("Получение заказов c токеном")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkGetAllOrdersWithTokenTest() {

        String token = user.getAccessToken();
        OrderApiClient orderApiClient = new OrderApiClient();

        Response response = null;

        if (responseCreateOrder.statusCode() == 200) {
            response = orderApiClient.getOrders(token, BaseURI.ORDERS_ENDPOINT);
        }

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("orders", notNullValue());

    }

    @Test
    @DisplayName("Получение заказов без токена")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkGetAllOrdersWithoutTokenTest() {

        //Аrrange
        String token = "";
        OrderApiClient orderApiClient = new OrderApiClient();

        //Аct
        Response response = orderApiClient.getOrders(token, BaseURI.ORDERS_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalToIgnoringCase("You should be authorised"));

    }

    @Step("Создание заказа")
    public Response createOrder() {

        String token = user.getAccessToken();
        OrderApiClient orderApiClient = new OrderApiClient();

        for (Ingredient ingredient : ingredientsList) {
            ingredients.add(ingredient.getId());
        }

        Burger burger = new Burger(ingredients);
        Response response = orderApiClient.postCreateOrder(burger, token, BaseURI.ORDERS_ENDPOINT);

        return response;

    }

    @Step("Создание уникального пользователя")
    private void createUniqueUser() {

        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        //UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        if (response.statusCode() == 200) {
            user.setAccessToken(response.then().extract().path("accessToken"));
            user.setRefreshToken(response.then().extract().path("refreshToken"));
        }

    }

}
