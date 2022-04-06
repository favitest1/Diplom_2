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
import ru.stellarburgers.apiclient.UserApiClient;
import ru.stellarburgers.model.User;

import java.util.List;

import static org.hamcrest.Matchers.*;


public class UserEndpointTest {

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
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку ответа на наличие кода ответа - 200, кодового сообщения, данных пользователя, токенов")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkSuccessCreateUniqueUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);
        if (response.statusCode() == 200) {
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
    @DisplayName("Создание неуникального пользователя")
    @Description("Проверка производится на произвольных значениях, включает в себя проверку кода ответа - 403, сообщения с кодом и сообщения с текстом")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledCreateNonUniqueUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        //UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        List<User> userList = List.of(user, user);

        //Аct
        Response response = userApiClient.userCreate(userList.get(0), BaseURI.AUTH_REGISTER_ENDPOINT);

        if (response.statusCode() == 200) {
            user.setAccessToken(response.then().extract().path("accessToken"));
            user.setRefreshToken(response.then().extract().path("refreshToken"));

            response = userApiClient.userCreate(userList.get(1), BaseURI.AUTH_REGISTER_ENDPOINT);

        }

        //Аssert
        response.then().assertThat().statusCode(403);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message",equalToIgnoringCase("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без обязательного атрибута")
    @Description("Проверка производится на произвольных значениях, без атрибута name, включает в себя проверку кода ответа - 403, сообщения с кодом и сообщения с текстом")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledCreateNonNameUserTest() {

        //Аrrange
        String name = "";
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = name + "@" + "yandex.ru";
        //UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(403);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message",equalToIgnoringCase("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без обязательного атрибута")
    @Description("Проверка производится на произвольных значениях, без атрибута password, включает в себя проверку кода ответа - 403, сообщения с кодом и сообщения с текстом")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledCreateNonPasswordUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = "";
        String email = name + "@" + "yandex.ru";
        //UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(403);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message",equalToIgnoringCase("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без обязательного атрибута")
    @Description("Проверка производится на произвольных значениях, без атрибута email, включает в себя проверку кода ответа - 403, сообщения с кодом и сообщения с текстом")
    @TmsLink("TestCase-112")
    @Issue("BUG-985")
    public void checkFiledCreateNonEmailUserTest() {

        //Аrrange
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = "";
        //UserApiClient userApiClient = new UserApiClient();
        user = new User(name, password, email);

        //Аct
        Response response = userApiClient.userCreate(user, BaseURI.AUTH_REGISTER_ENDPOINT);

        //Аssert
        response.then().assertThat().statusCode(403);
        response.then().assertThat().body("success", equalTo(false));
        response.then().assertThat().body("message",equalToIgnoringCase("Email, password and name are required fields"));

    }

}
