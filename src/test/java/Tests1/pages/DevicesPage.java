package Tests1.pages;

import Tests1.components.*;
import Tests1.models.DeviceEntity;
import Tests1.utils.BasePageFactory;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

public class DevicesPage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;
    private ModalWindow modalWindow;
    private Button button;

    private final String PAGE_TITLE = "//span[text()='Устройства ПМП']";
    private final String TABLE = "//div[@class='ant-table-content']";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";
    private final String TABLE_ROWS = "//tr[@class='ant-table-row ant-table-row-level-0']";
    private final String DEVICE_LINK = "//tr[@class='ant-table-row ant-table-row-level-0']//td[2]/a/button";

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
        modalWindow = new ModalWindow(page);
        button = new Button(page);
    }

    @Step("Проверить заголовок страницы")
    public String getPageTitle() {
        waitForElement(PAGE_TITLE);
        return page.locator(PAGE_TITLE).first().textContent();
    }

    @Step("Проверить видимость таблицы")
    public boolean isTableVisible() {
        return page.locator(TABLE).isVisible();
    }

    @Step("Выполнить поиск устройства: {device.serialNumber}")
    public DevicesPage searchDevice(DeviceEntity device) {
        waitForElement(SEARCH_INPUT);
        fill(SEARCH_INPUT, device.getSerialNumber());
        click(SEARCH_BUTTON);
        waitForTimeout(3000);
        takeScreenshot("search-results");
        return this;
    }

    @Step("Проверить наличие устройства: {serialNumber}")
    public boolean isDeviceFound(String serialNumber) {
        return page.locator("text=" + serialNumber).count() > 0;
    }

    @Step("Получить серийные номера из таблицы")
    public List<String> getSerialNumbersFromTable() {
        return page.locator(TABLE_ROWS + "//td[2]").all()
                .stream()
                .map(Locator::textContent)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Step("Кликнуть на первое устройство в таблице")
    public DeviceInfoPage clickOnFirstDevice() {
        page.locator(DEVICE_LINK).first().click();
        waitForLoadState();
        takeScreenshot("device-info-page");
        return BasePageFactory.createInstance(page, DeviceInfoPage.class);
    }

    @Step("Кликнуть на устройство с серийным номером: {serialNumber}")
    public DeviceInfoPage clickOnDeviceBySerial(String serialNumber) {
        // Ждем появления устройства в таблице после поиска
        waitForTimeout(2000);

        // Кликаем на первую кнопку в колонке серийного номера (там уже отфильтровано по поиску)
        page.locator(DEVICE_LINK).first().click();
        waitForLoadState();
        takeScreenshot("device-info-page");
        return BasePageFactory.createInstance(page, DeviceInfoPage.class);
    }

    @Step("Проверить наличие кнопки экспорта")
    public boolean isExportButtonVisible() {
        return page.getByText("Экспорт данных").isVisible();
    }

    @Step("Экспортировать данные")
    public DevicesPage exportData() {
        page.getByText("Экспорт данных").click();
        waitForTimeout(2000);
        takeScreenshot("after-export");
        return this;
    }

    @Step("Проверить наличие устройства в результатах поиска")
    public boolean verifyDeviceInResults(DeviceEntity device) {
        return getSerialNumbersFromTable().contains(device.getSerialNumber());
    }
}