package tests;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import java.nio.file.Paths;
import java.io.ByteArrayInputStream;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected LoginPage loginPage;

    @BeforeEach
    @Step("Настройка тестового окружения")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(1000));

        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos"))
                .setRecordVideoSize(1280, 720));

        page = context.newPage();
        page.setDefaultTimeout(60000);

        loginPage = new LoginPage(page);

        // Добавляем информацию о браузере в отчёт
        Allure.addAttachment("Browser", "text/plain", "Chromium");
        Allure.addAttachment("Test URL", "text/plain", "https://test.ppma.ru/portal");
    }

    @AfterEach
    @Step("Очистка после теста")
    void tearDown() {
        // Прикрепляем видео к отчёту, если тест упал
        if (context != null) {
            String videoPath = "videos/" + System.currentTimeMillis() + ".webm";
            Allure.addAttachment("Video", "video/webm",
                    new ByteArrayInputStream("Video saved in videos folder".getBytes()), "webm");
            context.close();
            System.out.println("Video saved");
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Step("Вывод заголовка теста")
    protected void printHeader(String testName) {
        String header = "\n" + "=".repeat(60) + "\n" + testName + "\n" + "=".repeat(60);
        System.out.println(header);
        Allure.addAttachment("Test Name", "text/plain", testName);
    }

    @Step("Обработка ошибки теста")
    protected void handleTestError(Exception e, String testName) {
        System.out.println("Error: " + e.getMessage());
        Allure.addAttachment("Error", "text/plain", e.getMessage());
        if (page != null) {
            byte[] screenshot = page.screenshot();
            Allure.addAttachment("Error Screenshot", new ByteArrayInputStream(screenshot));
        }
    }
}