package Tests1.pages;

import Tests1.components.*;
import Tests1.models.DeviceEntity;
import Tests1.utils.BasePageFactory;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

public class DevicesPage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;
    private ModalWindow modalWindow;
    private Button button;

    // ==================== ЛОКАТОРЫ ====================
    private final String PAGE_TITLE = "//span[text()='Устройства ПМП']";
    private final String TABLE = "//div[@class='ant-table-content']";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";
    private final String TABLE_ROWS = "//tr[@class='ant-table-row ant-table-row-level-0']";
    private final String DEVICE_LINK = "//tr[@class='ant-table-row ant-table-row-level-0']//td[2]/a/button";
    private final String DEVICE_LINK_BY_SERIAL = "//tr[@class='ant-table-row ant-table-row-level-0']//td[2]/a/button[contains(text(), '%s')]";
    private final String EXPORT_BUTTON = "//button[contains(@class, 'ant-btn')]//span[text()='Экспорт данных']";
    private final String ADVANCED_SEARCH_BUTTON = "button[data-class='pmp-button-search']";
    private final String ADVANCED_SEARCH_MODAL = "div.ant-modal[role='dialog']";
    private final String MODAL_CLOSE = "div.ant-modal-close";
    private final String MODEL_FIELD = "//input[@id='model']";
    private final String OK_BUTTON = "//button[contains(@class, 'ant-btn-primary')]//span[text()='OK']";
    private final String RESET_BUTTON = "//button[text()='Сбросить']";
    private final String TOTAL_COUNTER = "//div[contains(@class, 'ant-pagination-total-text')]";
    private final String EXPORT_MESSAGE = ".ant-message-notice, .ant-notification-notice, .ant-spin";
    private final String LAST_DEVICE_CELL = "(//tr[@class='ant-table-row ant-table-row-level-0'])[last()]/td[2]";

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
        modalWindow = new ModalWindow(page);
        button = new Button(page);
    }

    // ==================== БАЗОВЫЕ ПРОВЕРКИ ====================

    @Step("Проверить заголовок страницы")
    public String getPageTitle() {
        waitForElement(PAGE_TITLE);
        return page.locator(PAGE_TITLE).first().textContent();
    }

    @Step("Проверить видимость таблицы")
    public boolean isTableVisible() {
        return page.locator(TABLE).isVisible();
    }

    @Step("Получить количество колонок в таблице")
    public int getColumnsCount() {
        waitForTimeout(1000);
        return page.locator("//div//table//tr//th").count();
    }

    @Step("Проверить наличие поля поиска")
    public boolean isSearchInputVisible() {
        return page.locator(SEARCH_INPUT).isVisible();
    }

    @Step("Проверить наличие кнопки поиска")
    public boolean isSearchButtonVisible() {
        return page.locator(SEARCH_BUTTON).isVisible();
    }

    @Step("Проверить наличие кнопки экспорта")
    public boolean isExportButtonVisible() {
        return page.locator(EXPORT_BUTTON).isVisible();
    }

    // ==================== РАСШИРЕННЫЙ ПОИСК ====================

    @Step("Открыть расширенный поиск")
    public DevicesPage openAdvancedSearch() {
        waitForTimeout(1000);

        if (page.locator(ADVANCED_SEARCH_BUTTON).count() > 0) {
            page.locator(ADVANCED_SEARCH_BUTTON).first().click();
            System.out.println("Нажата кнопка расширенного поиска");
        } else {
            System.out.println("Кнопка расширенного поиска не найдена");
            takeScreenshot("advanced-search-not-found");
        }

        waitForTimeout(1000);
        return this;
    }

    @Step("Проверить видимость модального окна расширенного поиска")
    public boolean isAdvancedSearchModalVisible() {
        return page.locator(ADVANCED_SEARCH_MODAL).isVisible();
    }

    @Step("Закрыть расширенный поиск")
    public DevicesPage closeAdvancedSearch() {
        if (page.locator(MODAL_CLOSE).count() > 0) {
            page.locator(MODAL_CLOSE).click();
        }
        waitForTimeout(1000);
        return this;
    }

    @Step("Ввести модель устройства в расширенном поиске")
    public DevicesPage enterDeviceModel(String model) {
        waitForElement(MODEL_FIELD);
        fill(MODEL_FIELD, model);
        return this;
    }

    @Step("Нажать OK в расширенном поиске")
    public DevicesPage clickAdvancedSearchOk() {
        if (page.locator(OK_BUTTON).count() > 0) {
            page.locator(OK_BUTTON).first().click();
        } else {
            page.locator("button:has-text('OK')").first().click();
        }
        waitForTimeout(2000);
        return this;
    }

    @Step("Сбросить поиск")
    public DevicesPage resetSearch() {
        if (page.locator(RESET_BUTTON).count() > 0) {
            page.locator(RESET_BUTTON).click();
        }
        waitForTimeout(1000);
        return this;
    }

    // ==================== ПОИСК ПО СЕРИЙНОМУ НОМЕРУ ====================

    @Step("Выполнить поиск устройства")
    public DevicesPage searchDevice(DeviceEntity device) {
        waitForElement(SEARCH_INPUT);
        fill(SEARCH_INPUT, device.getSerialNumber());
        click(SEARCH_BUTTON);
        waitForTimeout(3000);
        takeScreenshot("search-results");
        return this;
    }

    @Step("Проверить наличие устройства в таблице")
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

    @Step("Кликнуть по ссылке с серийным номером: {serialNumber}")
    public void clickOnDeviceSerialLink(String serialNumber) {
        waitForTimeout(2000);
        waitForElement(TABLE_ROWS);

        String selector = String.format("//tr[@class='ant-table-row ant-table-row-level-0']//td[2]/a/button[contains(text(), '%s')]", serialNumber);

        if (page.locator(selector).count() > 0) {
            page.locator(selector).first().click();
            System.out.println("Кликнули по серийному номеру: " + serialNumber);
        } else {
            page.locator(DEVICE_LINK).first().click();
            System.out.println("Кликнули по первому устройству");
        }

        waitForLoadState();
    }

    @Step("Кликнуть на устройство по серийному номеру")
    public DeviceInfoPage clickOnDeviceBySerial(String serialNumber) {
        String selector = String.format(DEVICE_LINK_BY_SERIAL, serialNumber);
        if (page.locator(selector).count() > 0) {
            page.locator(selector).first().click();
        } else {
            page.locator(DEVICE_LINK).first().click();
        }
        waitForLoadState();
        takeScreenshot("device-info-page");
        return BasePageFactory.createInstance(page, DeviceInfoPage.class);
    }

    @Step("Кликнуть на первое устройство в таблице")
    public DeviceInfoPage clickOnFirstDevice() {
        waitForElement(DEVICE_LINK);
        page.locator(DEVICE_LINK).first().click();
        waitForLoadState();
        return BasePageFactory.createInstance(page, DeviceInfoPage.class);
    }

    // ==================== ПРОВЕРКА КОЛИЧЕСТВА УСТРОЙСТВ ====================

    @Step("Получить общее количество устройств из счетчика")
    public int getTotalDevicesCountFromCounter() {
        try {
            String countText = "";
            for (int i = 0; i < 3; i++) {
                try {
                    page.waitForSelector(TOTAL_COUNTER, new Page.WaitForSelectorOptions().setTimeout(5000));
                    countText = page.locator(TOTAL_COUNTER).textContent();
                    if (countText != null && !countText.isEmpty()) {
                        break;
                    }
                } catch (Exception e) {
                    // Пропускаем и пробуем еще раз
                }
            }

            if (countText.isEmpty()) {
                return getDevicesRowsCount();
            }

            String numbers = countText.replaceAll("[^0-9]", "");
            if (numbers.isEmpty()) return 0;
            return Integer.parseInt(numbers);
        } catch (Exception e) {
            return getDevicesRowsCount();
        }
    }

    @Step("Получить количество строк в таблице")
    public int getDevicesRowsCount() {
        return page.locator(TABLE_ROWS).count();
    }

    @Step("Получить последний серийный номер в таблице")
    public String getLastDeviceSerialNumber() {
        waitForTimeout(2000);
        try {
            return page.locator(LAST_DEVICE_CELL).textContent().trim();
        } catch (Exception e) {
            return "N/A";
        }
    }

    // ==================== ЭКСПОРТ ====================

    @Step("Нажать кнопку экспорта")
    public DevicesPage clickExportButton() {
        waitForElement(EXPORT_BUTTON);
        page.locator(EXPORT_BUTTON).click();
        waitForTimeout(3000);
        return this;
    }

    @Step("Проверить, что экспорт начался")
    public boolean isExportStarted() {
        try {
            page.waitForSelector(EXPORT_MESSAGE, new Page.WaitForSelectorOptions().setTimeout(10000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== НАВИГАЦИЯ ====================

    @Step("Открыть бургер-меню")
    public DevicesPage openBurgerMenu() {
        header.clickOnBurgerMenu();
        waitForTimeout(500);
        return this;
    }

    @Step("Проверить видимость бокового меню")
    public boolean isSideMenuVisible() {
        return sideNavMenu.isMenuVisible();
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    @Step("Получить текст из ячейки таблицы")
    public String getCellText(int row, int column) {
        String selector = String.format("//tr[@class='ant-table-row ant-table-row-level-0'][%d]//td[%d]", row, column);
        return page.locator(selector).textContent().trim();
    }

    @Step("Получить все строки таблицы")
    public List<String> getAllRowsText() {
        return page.locator(TABLE_ROWS).all()
                .stream()
                .map(Locator::textContent)
                .collect(Collectors.toList());
    }
}