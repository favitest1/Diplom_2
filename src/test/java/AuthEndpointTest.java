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
import ru.stellarburgers.apiclient.AuthApiClient;
import ru.stellarburgers.apiclient.UserApiClient;
import ru.stellarburgers.model.User;

import static org.hamcrest.Matchers.*;

public class AuthEndpointTest {

    private User user;
    private AuthApiClient authApiClient = new AuthApiClient();
    private UserApiClient userApiClient = new UserApiClient();

    @Before
    @Step ("Создание пользователя перед тестом")
    public void setUp() {
        RestAssured.baseURI= BaseURI.BASE_URI;
        createUniqueUser();
    }

    @After
    @Step ("Удаление пользователя")
    public void deleteUser() {
        userApiClient.userDelete(user, BaseURI.AUTH_USER_ENDPOINT);
    }

    @Test
    @DisplayName("Получение токена пользователя")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkGetTokenTest() {

        //Аrrange
        String accessToken = user.getAccessToken();

        //Аct
        Response response = authApiClient.getUserData(accessToken, BaseURI.AUTH_USER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("user.email", notNullValue());
        response.then().assertThat().body("user.name", notNullValue());

    }

    @Test
    @DisplayName("Изменение Name пользователя с авторизацией")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkSuccessChangeNameUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        user.setName(name);

        //Аct
        Response response = authApiClient.patchUserData(user, BaseURI.AUTH_USER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("user.email", notNullValue());
        response.then().assertThat().body("user.name", equalToIgnoringCase(name));

    }

    @Test
    @DisplayName("Изменение Email пользователя с авторизацией")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkSuccessChangeEmailUserTest() {

        //Аrrange
        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        user.setEmail(email);

        //Аct
        Response response = authApiClient.patchUserData(user, BaseURI.AUTH_USER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("user.email", equalToIgnoringCase(email));
        response.then().assertThat().body("user.name", notNullValue());

    }

    @Test
    @DisplayName("Изменение Name пользователя без авторизации")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 401, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledChangeNameUserTest() {

        //Аrrange
        String token = user.getAccessToken();
        String name = RandomStringUtils.randomAlphabetic(10);
        user.setName(name);

        //Аct
        if (token != null && token != "") {
            user.setAccessToken("");
        }

        Response response = authApiClient.patchUserData(user, BaseURI.AUTH_USER_ENDPOINT);

        user.setAccessToken(token);

        //Аssert
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalToIgnoringCase("You should be authorised"));

    }

    @Test
    @DisplayName("Изменение Email пользователя без авторизации")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 401, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledChangeEmailUserTest() {

        //Аrrange
        String token = user.getAccessToken();
        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        user.setEmail(email);

        //Аct
        if (token != null && token != "") {
            user.setAccessToken("");
        }

        Response response = authApiClient.patchUserData(user, BaseURI.AUTH_USER_ENDPOINT);

        user.setAccessToken(token);

        //Аssert
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message", equalToIgnoringCase("You should be authorised"));

    }

    @Step ("Создание уникального пользователя")
    private void createUniqueUser() {

        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        if (response.statusCode() == 200) {
            user.setAccessToken(response.then().extract().path("accessToken"));
            user.setRefreshToken(response.then().extract().path("refreshToken"));
        }

    }

}
