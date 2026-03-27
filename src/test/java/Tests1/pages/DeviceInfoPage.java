package Tests1.pages;

import Tests1.components.ModalWindow;
import Tests1.utils.BasePageFactory;
import io.qameta.allure.Step;

public class DeviceInfoPage extends BasePage {

    private ModalWindow modalWindow;

    // ==================== ЛОКАТОРЫ ====================
    private final String PAGE_TITLE = "//span[text()='Информация об устройстве']";
    private final String MEASUREMENTS_TAB = "//div[@data-node-key='Измерения']";
    private final String INFO_TAB = "//div[contains(text(), 'Основная информация')]";
    private final String EXPORT_BUTTON = "//span[text()='Экспорт данных']";
    private final String BACK_BUTTON = "//a[contains(@class, 'ant-btn')]//span[text()='Назад']";

    @Override
    public void initComponents() {
        modalWindow = new ModalWindow(page);
    }

    // ==================== ПРОВЕРКА СТРАНИЦЫ ====================

    @Step("Проверить, что страница информации об устройстве загружена")
    public boolean isDeviceInfoPageLoaded() {
        waitForLoadState();
        waitForTimeout(3000);
        return page.locator(PAGE_TITLE).isVisible();
    }

    @Step("Проверить наличие вкладок на странице")
    public boolean areTabsVisible() {
        waitForTimeout(3000);
        boolean hasMeasurements = page.locator(MEASUREMENTS_TAB).count() > 0 ||
                page.locator("//div[contains(text(), 'Измерения')]").count() > 0;
        boolean hasInfo = page.locator(INFO_TAB).count() > 0;
        return hasMeasurements && hasInfo;
    }

    @Step("Проверить видимость вкладки 'Измерения'")
    public boolean isMeasurementsTabVisible() {
        waitForTimeout(2000);
        return page.locator(MEASUREMENTS_TAB).count() > 0 ||
                page.locator("//div[contains(text(), 'Измерения')]").count() > 0;
    }

    @Step("Проверить видимость вкладки 'Основная информация'")
    public boolean isInfoTabVisible() {
        waitForTimeout(2000);
        return page.locator(INFO_TAB).count() > 0;
    }

    @Step("Кликнуть на вкладку 'Измерения'")
    public DeviceInfoPage clickMeasurementsTab() {
        waitForTimeout(2000);

        if (page.locator(MEASUREMENTS_TAB).count() > 0) {
            page.locator(MEASUREMENTS_TAB).first().click();
        } else {
            page.locator("//div[contains(text(), 'Измерения')]").first().click();
        }

        waitForTimeout(2000);
        takeScreenshot("measurements-tab");
        return this;
    }

    @Step("Кликнуть на вкладку 'Основная информация'")
    public DeviceInfoPage clickInfoTab() {
        waitForTimeout(2000);

        if (page.locator(INFO_TAB).count() > 0) {
            page.locator(INFO_TAB).first().click();
        }

        waitForTimeout(2000);
        takeScreenshot("info-tab");
        return this;
    }

    @Step("Проверить наличие кнопки экспорта")
    public boolean isExportButtonVisible() {
        waitForTimeout(2000);
        return page.locator(EXPORT_BUTTON).count() > 0;
    }

    @Step("Нажать кнопку экспорта измерений")
    public DeviceInfoPage clickExportButton() {
        if (page.locator(EXPORT_BUTTON).count() > 0) {
            page.locator(EXPORT_BUTTON).first().click();
        }
        waitForTimeout(2000);
        takeScreenshot("export-measurements");
        return this;
    }

    @Step("Вернуться на страницу устройств")
    public DevicesPage goBackToDevices() {
        waitForElement(BACK_BUTTON);
        click(BACK_BUTTON);
        waitForLoadState();
        return BasePageFactory.createInstance(page, DevicesPage.class);
    }

    @Step("Получить серийный номер устройства со страницы")
    public String getDeviceSerialNumber() {
        waitForElement(PAGE_TITLE);
        try {
            String serial = page.locator("//div[contains(@class, 'device-info')]//div[contains(text(), 'Серийный номер')]/following-sibling::div").textContent();
            return serial != null ? serial.trim() : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Step("Вывести информацию об устройстве")
    public void printDeviceInfo() {
        System.out.println("\nИНФОРМАЦИЯ ОБ УСТРОЙСТВЕ:");
        System.out.println("  Серийный номер: " + getDeviceSerialNumber());
    }
}