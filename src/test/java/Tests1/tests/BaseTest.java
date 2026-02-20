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

    @RegisterExtension
    AfterTestExecutionCallback callback = context -> {
        Optional<Throwable> exception = context.getExecutionException();
        if (exception.isPresent()) {
            needVideo = true;
            captureScreenshot();
        }
    };

    @BeforeAll
    public void initBrowser() {
        playwright = Playwright.create();
        browser = BrowserManager.getBrowser(playwright);

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

    @BeforeEach
    public void createContextAndPage() {
        if (config().video()) {
            context = browser.newContext(new Browser.NewContextOptions()
                    .setRecordVideoDir(Paths.get(config().videoDir()))
                    .setAcceptDownloads(true));
        } else {
            context = browser.newContext();
        }

        page = context.newPage();
        loginPage = createInstance(LoginPage.class);
    }

    @AfterEach
    public void closeContext() {
        if (context != null) {
            context.close();
        }

        if (config().video() && needVideo) {
            captureVideo();
        }
        needVideo = false;
    }

    @AfterAll
    public void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Attachment(value = "Скриншот при падении", type = "image/png")
    private byte[] captureScreenshot() {
        return page.screenshot();
    }

    @Attachment(value = "Видео теста", type = "video/webm")
    @SneakyThrows
    private byte[] captureVideo() {
        return Files.readAllBytes(page.video().path());
    }

    protected <T extends BasePage> T createInstance(Class<T> pageClass) {
        return BasePageFactory.createInstance(page, pageClass);
    }

    protected void printHeader(String testName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(testName);
        System.out.println("=".repeat(60));
    }
}