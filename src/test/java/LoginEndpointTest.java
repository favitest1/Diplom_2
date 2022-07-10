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
import ru.stellarburgers.apiclient.LoginApiClient;
import ru.stellarburgers.apiclient.UserApiClient;
import ru.stellarburgers.model.User;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;


public class LoginEndpointTest {

    private User user;
    private UserApiClient userApiClient = new UserApiClient();

    @Before
    @Step("Инициализация")
    public void setUp() {
        RestAssured.baseURI= BaseURI.BASE_URI;
    }

    @After
    @Step ("Удаление пользователя")
    public void deleteUser() {
        userApiClient.userDelete(user, BaseURI.AUTH_USER_ENDPOINT);
    }

    @Test
    @DisplayName("Авторизация пользователя с существующим логином/паролем")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkSuccessLoginUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        LoginApiClient loginApiClient = new LoginApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        if (response.statusCode() == 200) {

            response = loginApiClient.loginUser(user, BaseURI.AUTH_LOGIN_ENDPOINT);

            user.setAccessToken(response.then().extract().path("accessToken"));
            user.setRefreshToken(response.then().extract().path("refreshToken"));
        }

        //Аssert
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
        response.then().assertThat().body("user.email",equalToIgnoringCase(email));
        response.then().assertThat().body("user.name",equalToIgnoringCase(name));
        response.then().assertThat().body("accessToken", notNullValue());
        response.then().assertThat().body("refreshToken", notNullValue());

    }

    @Test
    @DisplayName("Авторизация пользователя с несуществующим логином/паролем")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 401, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledLoginUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        LoginApiClient loginApiClient = new LoginApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = loginApiClient.loginUser(user, BaseURI.AUTH_LOGIN_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message",equalToIgnoringCase("email or password are incorrect"));

    }

}
