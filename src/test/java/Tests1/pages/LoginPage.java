package Tests1.pages;

import Tests1.components.Header;
import Tests1.utils.BasePageFactory;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static Tests1.config.ConfigurationManager.config;

public class LoginPage extends BasePage {

    private Header header;
    private final String USERNAME_FIELD = "#username";
    private final String PASSWORD_FIELD = "#password";
    private final String LOGIN_BUTTON = "button[type='submit']";

    @Override
    public void initComponents() {
        header = new Header(page);
    }

    @Step("Открыть страницу входа")
    public LoginPage open() {
        page.navigate(config().baseUrl());
        waitForLoadState();
        takeScreenshot("login-page-opened");
        return this;
    }

    @Step("Ввести логин")
    public LoginPage enterUsername(String username) {
        waitForElement(USERNAME_FIELD);
        fill(USERNAME_FIELD, username);
        takeScreenshot("after-username-entered");
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        fill(PASSWORD_FIELD, password);
        takeScreenshot("after-password-entered");
        return this;
    }

    @Step("Нажать кнопку входа")
    public HomePage clickLogin() {
        click(LOGIN_BUTTON);
        waitForLoadState();
        takeScreenshot("after-login");
        // ИСПРАВЛЕНО: возвращаем HomePage вместо DevicesPage
        return BasePageFactory.createInstance(page, HomePage.class);
    }

    @Step("Выполнить вход")
    public HomePage loginAs(String username, String password) {
        return open()
                .enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }
}