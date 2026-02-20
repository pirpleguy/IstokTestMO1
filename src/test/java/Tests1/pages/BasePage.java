package Tests1.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Tests1.config.ConfigurationManager.config;

public abstract class BasePage {
    protected Page page;

    public void setPage(Page page) {
        this.page = page;
        page.setDefaultTimeout(config().timeout());
    }

    public abstract void initComponents();

    protected void click(String selector) {
        page.click(selector);
    }

    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    protected String getText(String selector) {
        return page.textContent(selector);
    }

    protected boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    protected void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    protected void waitForTimeout(int millis) {
        page.waitForTimeout(millis);
    }

    protected void waitForLoadState() {
        page.waitForLoadState();
    }

    @Step("Сделать скриншот: {name}")
    public void takeScreenshot(String name) {
        try {
            // Сохраняем в Allure отчет
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));

            // Сохраняем в файл
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("screenshots/%s_%s.png", name, timestamp);

            Path screenshotPath = Paths.get(fileName);
            Files.createDirectories(screenshotPath.getParent());

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));

            System.out.println("Скриншот сохранен: " + fileName);

        } catch (Exception e) {
            System.err.println("Ошибка при сохранении скриншота: " + e.getMessage());
        }
    }

    protected String getCellText(int index) {
        try {
            return page.locator("tbody tr:first-child td:nth-child(" + index + ")").first().textContent().trim();
        } catch (Exception e) {
            return "N/A";
        }
    }
}