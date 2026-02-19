package pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class DevicesPage extends BasePage {

    private final String DEVICES_MENU = ".ant-menu-item >> nth=1";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";
    private final String DEVICE_TEXT = "text=%s";

    public DevicesPage(Page page) {
        super(page);
        waitForElement(".ant-menu-item");
        click(DEVICES_MENU);
        waitForLoadState();
        waitForTimeout(3000);
        takeScreenshot("devices-page-opened");
    }

    private void waitForLoadState() {
        page.waitForLoadState();
    }

    @Step("Поиск устройства: {deviceName}")
    public DevicesPage searchDevice(String deviceName) {
        System.out.println("Waiting for search input...");
        waitForElement(SEARCH_INPUT);
        waitForTimeout(1000);
        takeScreenshot("before-search");

        System.out.println("Searching for: " + deviceName);
        fill(SEARCH_INPUT, deviceName);
        waitForTimeout(1000);

        System.out.println("Clicking search button...");
        click(SEARCH_BUTTON);
        waitForTimeout(3000);

        takeScreenshot("after-search");
        return this;
    }

    @Step("Проверка наличия устройства: {deviceName}")
    public boolean isDeviceFound(String deviceName) {
        boolean found = page.locator(String.format(DEVICE_TEXT, deviceName)).count() > 0;
        if (found) {
            Allure.addAttachment("Device found", "text/plain", "Device " + deviceName + " was found");
        }
        return found;
    }

    @Step("Получение серийного номера устройства")
    public String getDeviceSerial() {
        return getCellText(2);
    }

    @Step("Получение модели устройства")
    public String getDeviceModel() {
        return getCellText(4);
    }

    @Step("Получение статуса устройства")
    public String getDeviceStatus() {
        return getCellText(11);
    }

    @Step("Вывод деталей устройства")
    public void printDeviceDetails() {
        String details = String.format("""
                \nDevice details:
                  Serial number: %s
                  Model: %s
                  Status: %s""",
                getDeviceSerial(), getDeviceModel(), getDeviceStatus());
        System.out.println(details);
        Allure.addAttachment("Device Details", "text/plain", details);
    }
}