package Tests1.pages;

import Tests1.components.ModalWindow;
import Tests1.utils.BasePageFactory;
import io.qameta.allure.Step;

public class DeviceInfoPage extends BasePage {

    private ModalWindow modalWindow;

    private final String PAGE_TITLE = "//span[text()='Информация об устройстве']";
    private final String DEVICE_SERIAL = "//div[contains(@class, 'device-info')]//span[contains(text(), '%s')]";

    @Override
    public void initComponents() {
        modalWindow = new ModalWindow(page);
    }

    @Step("Проверить, что страница информации об устройстве загружена")
    public boolean isDeviceInfoPageLoaded() {
        waitForLoadState();
        return page.locator(PAGE_TITLE).isVisible();
    }

    @Step("Проверить отображение серийного номера устройства: {serialNumber}")
    public boolean isDeviceSerialVisible(String serialNumber) {
        String selector = String.format(DEVICE_SERIAL, serialNumber);
        return page.locator(selector).isVisible();
    }

    @Step("Проверить наличие вкладок")
    public boolean areTabsVisible() {
        return page.locator("//div[@data-node-key='Измерения']").isVisible() &&
                page.locator("//div[@data-node-key='Информация']").isVisible();
    }

    @Step("Проверить наличие кнопки экспорта")
    public boolean isExportButtonVisible() {
        return page.getByText("Экспорт данных").isVisible();
    }
}