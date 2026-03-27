package Tests1.tests;

import Tests1.pages.BasePage;
import Tests1.pages.LoginPage;
import Tests1.utils.BasePageFactory;
import Tests1.utils.BrowserManager;
import com.google.common.collect.ImmutableMap;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static Tests1.config.ConfigurationManager.config;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected LoginPage loginPage;

    private boolean needVideo = false;

    // Регистрируем расширение для автоматического захвата видео при падении теста
    @RegisterExtension
    AfterTestExecutionCallback callback = context -> {
        Optional<Throwable> exception = context.getExecutionException();
        if (exception.isPresent()) {
            needVideo = true;
            captureScreenshot();
        }
    };

    // ==================== НАСТРОЙКА ПЕРЕД ВСЕМИ ТЕСТАМИ ====================

    @BeforeAll
    public void initBrowser() {
        // Создаем Playwright и браузер
        playwright = Playwright.create();
        browser = BrowserManager.getBrowser(playwright);

        // Записываем информацию о окружении в Allure отчет
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Platform", System.getProperty("os.name"))
                        .put("Version", System.getProperty("os.version"))
                        .put("Browser", config().browser().toUpperCase())
                        .put("Environment", "Test")
                        .put("URL", config().baseUrl())
                        .build(),
                config().allureResultsDir() + "/");
    }

    // ==================== НАСТРОЙКА ПЕРЕД КАЖДЫМ ТЕСТОМ ====================

    @BeforeEach
    public void createContextAndPage() {
        // Создаем контекст браузера (с видео или без)
        if (config().video()) {
            context = browser.newContext(new Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get(config().videoDir()))
                    .setAcceptDownloads(true)
                    .setViewportSize(1920, 1080));
        } else {
            context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(1920, 1080));
        }

        // Создаем страницу
        page = context.newPage();
        page.setDefaultTimeout(config().timeout());

        // Создаем страницу логина через фабрику
        loginPage = createInstance(LoginPage.class);
    }

    // ==================== ОЧИСТКА ПОСЛЕ КАЖДОГО ТЕСТА ====================

    @AfterEach
    public void closeContext() {
        // Закрываем контекст (видео сохраняется здесь)
        if (context != null) {
            context.close();
        }

        // Если тест упал и нужно видео — прикрепляем его к отчету
        if (config().video() && needVideo) {
            captureVideo();
        }
        needVideo = false;
    }

    // ==================== ЗАВЕРШЕНИЕ ====================

    @AfterAll
    public void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Создает экземпляр страницы через фабрику
     */
    protected <T extends BasePage> T createInstance(Class<T> pageClass) {
        return BasePageFactory.createInstance(page, pageClass);
    }

    /**
     * Выводит красивый заголовок в консоль
     */
    protected void printHeader(String testName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(testName);
        System.out.println("=".repeat(60));
    }

    // ==================== ПРИКРЕПЛЕНИЕ ВЛОЖЕНИЙ К ALLURE ====================

    /**
     * Делает скриншот и прикрепляет его к Allure отчету (вызывается автоматически при падении)
     */
    @Attachment(value = "Скриншот при падении", type = "image/png")
    private byte[] captureScreenshot() {
        if (page != null) {
            return page.screenshot();
        }
        return new byte[0];
    }

    /**
     * Прикрепляет видео к Allure отчету (вызывается автоматически при падении)
     */
    @Attachment(value = "Видео теста", type = "video/webm")
    @SneakyThrows
    private byte[] captureVideo() {
        if (page != null && page.video() != null) {
            return Files.readAllBytes(page.video().path());
        }
        return new byte[0];
    }
}