package pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.nio.file.Paths;
import java.io.ByteArrayInputStream;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    @Step("Клик по элементу: {selector}")
    protected void click(String selector) {
        page.click(selector);
    }

    @Step("Заполнение поля {selector} значением: {text}")
    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    @Step("Получение текста элемента: {selector}")
    protected String getText(String selector) {
        return page.textContent(selector);
    }

    protected boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    protected void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    protected void waitForTimeout(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    @Step("Получение текста ячейки #{index}")
    protected String getCellText(int index) {
        try {
            return page.locator("tbody tr:first-child td:nth-child(" + index + ")").first().textContent().trim();
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Step("Сохранение скриншота: {name}")
    public void takeScreenshot(String name) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true));
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));

        // Сохраняем и файл (опционально)
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(name + ".png"))
                .setFullPage(true));
    }

    @Step("Прикрепление видео к отчёту")
    public void attachVideo() {
        // Playwright сохраняет видео в контексте
        // Мы можем прикрепить информацию о видео
        Allure.addAttachment("Video path", "Video saved in videos folder");
    }
}