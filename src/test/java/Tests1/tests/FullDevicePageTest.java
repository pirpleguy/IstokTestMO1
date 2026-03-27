package Tests1.tests;

import Tests1.models.DeviceEntity;
import Tests1.pages.DevicesPage;
import Tests1.pages.HomePage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Полное тестирование страницы устройств ПМП")
@Feature("Страница устройств ПМП")
@TestMethodOrder(OrderAnnotation.class)
public class FullDevicePageTest extends BaseTest {

    // Тестовые данные для аккаунта TestElta
    private static final String TEST_USERNAME = "TestElta";
    private static final String TEST_PASSWORD = "4zaKfdeFc4BY42Iq";

    // Серийный номер для проверки
    private static final String TEST_SERIAL = "htkwv";
    private static final String TEST_MODEL = "Сателлит Online";

    // Объекты страниц
    private DevicesPage devicesPage;

    @BeforeEach
    void setUpTest() {
        HomePage homePage = loginPage.loginAs(TEST_USERNAME, TEST_PASSWORD);
        devicesPage = homePage.navigateToDevicesPage();
    }

    // ==================== БЛОК 1: БАЗОВЫЕ ЭЛЕМЕНТЫ СТРАНИЦЫ ====================

    @Test
    @Order(1)
    @DisplayName("TC-001: Проверка заголовка страницы")
    @Description("Проверка, что заголовок страницы 'Устройства ПМП' отображается корректно")
    @Severity(SeverityLevel.BLOCKER)
    void testPageTitle() {
        printHeader("TC-001: Проверка заголовка страницы");

        String actualTitle = devicesPage.getPageTitle();

        assertThat(actualTitle)
                .as("Заголовок страницы должен быть 'Устройства ПМП'")
                .isEqualTo("Устройства ПМП");

        devicesPage.takeScreenshot("01-page-title");
    }

    @Test
    @Order(2)
    @DisplayName("TC-002: Проверка отображения таблицы")
    @Description("Проверка, что таблица с устройствами видима на странице")
    @Severity(SeverityLevel.CRITICAL)
    void testTableVisibility() {
        printHeader("TC-002: Проверка видимости таблицы");

        assertThat(devicesPage.isTableVisible())
                .as("Таблица устройств должна быть видима")
                .isTrue();

        devicesPage.takeScreenshot("02-table-visible");
    }

    @Test
    @Order(3)
    @DisplayName("TC-003: Проверка количества колонок в таблице")
    @Description("Проверка, что таблица содержит правильное количество колонок")
    @Severity(SeverityLevel.NORMAL)
    void testColumnsCount() {
        printHeader("TC-003: Проверка количества колонок");

        int actualColumns = devicesPage.getColumnsCount();

        assertThat(actualColumns)
                .as("Таблица должна содержать 11 колонок")
                .isEqualTo(11);

        devicesPage.takeScreenshot("03-columns-count");
    }

    @Test
    @Order(4)
    @DisplayName("TC-004: Проверка наличия поля поиска")
    @Description("Проверка, что поле поиска отображается на странице")
    @Severity(SeverityLevel.NORMAL)
    void testSearchInputVisible() {
        printHeader("TC-004: Проверка наличия поля поиска");

        assertThat(devicesPage.isSearchInputVisible())
                .as("Поле поиска должно быть видимо")
                .isTrue();

        devicesPage.takeScreenshot("04-search-input");
    }

    @Test
    @Order(5)
    @DisplayName("TC-005: Проверка наличия кнопки поиска")
    @Description("Проверка, что кнопка поиска отображается на странице")
    @Severity(SeverityLevel.NORMAL)
    void testSearchButtonVisible() {
        printHeader("TC-005: Проверка наличия кнопки поиска");

        assertThat(devicesPage.isSearchButtonVisible())
                .as("Кнопка поиска должна быть видима")
                .isTrue();

        devicesPage.takeScreenshot("05-search-button");
    }

    @Test
    @Order(6)
    @DisplayName("TC-006: Проверка наличия кнопки экспорта")
    @Description("Проверка, что на странице присутствует кнопка 'Экспорт данных'")
    @Severity(SeverityLevel.NORMAL)
    void testExportButtonVisible() {
        printHeader("TC-006: Проверка наличия кнопки экспорта");

        assertThat(devicesPage.isExportButtonVisible())
                .as("Кнопка 'Экспорт данных' должна быть видима")
                .isTrue();

        devicesPage.takeScreenshot("06-export-button");
    }

    // ==================== БЛОК 2: РАСШИРЕННЫЙ ПОИСК ====================

    @Test
    @Order(7)
    @DisplayName("TC-007: Проверка открытия расширенного поиска")
    @Description("Проверка, что кнопка расширенного поиска открывает модальное окно")
    @Severity(SeverityLevel.NORMAL)
    void testAdvancedSearchOpens() {
        printHeader("TC-007: Проверка открытия расширенного поиска");

        devicesPage.openAdvancedSearch();

        assertThat(devicesPage.isAdvancedSearchModalVisible())
                .as("Модальное окно расширенного поиска должно открыться")
                .isTrue();

        devicesPage.takeScreenshot("07-advanced-search-modal");

        devicesPage.closeAdvancedSearch();
    }

    @Test
    @Order(8)
    @DisplayName("TC-008: Проверка поиска по модели устройства")
    @Description("Проверка поиска устройства по модели в расширенном поиске")
    @Severity(SeverityLevel.NORMAL)
    void testSearchByModel() {
        printHeader("TC-008: Проверка поиска по модели устройства");

        devicesPage.openAdvancedSearch()
                .enterDeviceModel(TEST_MODEL)
                .clickAdvancedSearchOk();

        assertThat(devicesPage.isTableVisible())
                .as("Таблица должна отображаться после поиска")
                .isTrue();

        devicesPage.takeScreenshot("08-search-by-model");

        devicesPage.resetSearch();
    }

    // ==================== БЛОК 3: ПРОВЕРКА КОЛИЧЕСТВА УСТРОЙСТВ ====================

    @Test
    @Order(9)
    @DisplayName("TC-009: Проверка количества устройств в таблице")
    @Description("Проверка, что количество устройств в таблице соответствует счетчику на странице")
    @Severity(SeverityLevel.CRITICAL)
    void testDeviceCount() {
        printHeader("TC-009: Проверка количества устройств");

        int totalDevicesFromCounter = devicesPage.getTotalDevicesCountFromCounter();
        int rowsInTable = devicesPage.getDevicesRowsCount();

        assertThat(rowsInTable)
                .as("Количество устройств в таблице должно соответствовать счетчику на странице")
                .isEqualTo(totalDevicesFromCounter);

        devicesPage.takeScreenshot("09-device-count");

        System.out.println("Количество устройств в таблице: " + rowsInTable);
        System.out.println("Счетчик на странице показывает: " + totalDevicesFromCounter);
    }

    @Test
    @Order(10)
    @DisplayName("TC-010: Проверка последнего устройства в таблице")
    @Description("Проверка, что последнее устройство в таблице отображается корректно")
    @Severity(SeverityLevel.NORMAL)
    void testLastDeviceInTable() {
        printHeader("TC-010: Проверка последнего устройства в таблице");

        String lastDeviceSerial = devicesPage.getLastDeviceSerialNumber();

        assertThat(lastDeviceSerial)
                .as("Последнее устройство в таблице должно быть не пустым")
                .isNotNull()
                .isNotEmpty();

        devicesPage.takeScreenshot("10-last-device");

        System.out.println("Последнее устройство: " + lastDeviceSerial);
    }

    // ==================== БЛОК 4: ПОИСК И ПЕРЕХОД НА СТРАНИЦУ УСТРОЙСТВА ====================

    @Test
    @Order(11)
    @DisplayName("TC-011: Поиск устройства по серийному номеру")
    @Description("Проверка поиска устройства по серийному номеру")
    @Severity(SeverityLevel.CRITICAL)
    void testSearchBySerial() {
        printHeader("TC-011: Поиск устройства по серийному номеру");

        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .build();

        devicesPage.searchDevice(testDevice);

        assertThat(devicesPage.isDeviceFound(TEST_SERIAL))
                .as("Устройство с серийным номером %s должно быть найдено", TEST_SERIAL)
                .isTrue();

        devicesPage.takeScreenshot("11-search-by-serial");
    }

    @Test
    @Order(12)
    @DisplayName("TC-012: Переход на страницу информации об устройстве")
    @Description("Проверка перехода на страницу детальной информации по клику на серийный номер в таблице")
    @Severity(SeverityLevel.CRITICAL)
    void testNavigateToDeviceInfo() {
        printHeader("TC-012: Переход на страницу информации об устройстве");

        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .build();
        devicesPage.searchDevice(testDevice);

        devicesPage.clickOnDeviceSerialLink(TEST_SERIAL);

        page.waitForLoadState();
        devicesPage.waitForTimeout(3000);

        String currentUrl = page.url();
        assertThat(currentUrl)
                .as("URL должен содержать информацию об устройстве")
                .contains("device");

        boolean hasTitle = page.locator("//span[text()='Информация об устройстве']").count() > 0;
        assertThat(hasTitle)
                .as("Должна открыться страница информации об устройстве")
                .isTrue();

        devicesPage.takeScreenshot("12-device-info-page");
    }

    // ==================== БЛОК 5: ПРОВЕРКА ВКЛАДОК ====================

    @Test
    @Order(13)
    @DisplayName("TC-013: Проверка вкладки 'Измерения'")
    @Description("Проверка, что вкладка 'Измерения' открывается и отображает данные")
    @Severity(SeverityLevel.NORMAL)
    void testMeasurementsTab() {
        printHeader("TC-013: Проверка вкладки 'Измерения'");

        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .build();
        devicesPage.searchDevice(testDevice);
        devicesPage.clickOnDeviceSerialLink(TEST_SERIAL);

        devicesPage.waitForTimeout(2000);

        boolean hasMeasurementsTab = page.locator("//div[contains(text(), 'Измерения')]").count() > 0;
        assertThat(hasMeasurementsTab)
                .as("Вкладка 'Измерения' должна отображаться")
                .isTrue();

        devicesPage.takeScreenshot("13-measurements-tab");
    }

    @Test
    @Order(14)
    @DisplayName("TC-014: Проверка вкладки 'Основная информация'")
    @Description("Проверка, что вкладка 'Основная информация' открывается и отображает данные")
    @Severity(SeverityLevel.NORMAL)
    void testInfoTab() {
        printHeader("TC-014: Проверка вкладки 'Основная информация'");

        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .build();
        devicesPage.searchDevice(testDevice);
        devicesPage.clickOnDeviceSerialLink(TEST_SERIAL);

        devicesPage.waitForTimeout(2000);

        boolean hasInfoTab = page.locator("//div[contains(text(), 'Основная информация')]").count() > 0;
        assertThat(hasInfoTab)
                .as("Вкладка 'Основная информация' должна отображаться")
                .isTrue();

        devicesPage.takeScreenshot("14-info-tab");
    }

    // ==================== БЛОК 6: ЭКСПОРТ ====================

    @Test
    @Order(15)
    @DisplayName("TC-015: Проверка экспорта данных со страницы устройств")
    @Description("Проверка, что кнопка экспорта инициирует скачивание файла")
    @Severity(SeverityLevel.NORMAL)
    void testExportData() {
        printHeader("TC-015: Проверка экспорта данных");

        devicesPage.clickExportButton();

        assertThat(devicesPage.isExportStarted())
                .as("Экспорт должен начаться")
                .isTrue();

        devicesPage.takeScreenshot("15-export-started");
    }

    // ==================== БЛОК 7: БОКОВОЕ МЕНЮ ====================

    @Test
    @Order(16)
    @DisplayName("TC-016: Проверка бокового меню")
    @Description("Проверка, что боковое меню открывается и отображается")
    @Severity(SeverityLevel.NORMAL)
    void testSideMenu() {
        printHeader("TC-016: Проверка бокового меню");

        devicesPage.openBurgerMenu();

        assertThat(devicesPage.isSideMenuVisible())
                .as("Боковое меню должно быть видимо после открытия")
                .isTrue();

        devicesPage.takeScreenshot("16-side-menu");
    }
}