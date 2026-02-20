package Tests1.tests;

import Tests1.models.DeviceEntity;
import Tests1.pages.DeviceInfoPage;
import Tests1.pages.DevicesPage;
import Tests1.pages.HomePage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static Tests1.config.ConfigurationManager.config;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Управление устройствами")
@Feature("Страница устройств ПМП")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeviceTests extends BaseTest {

    private static final String TEST_SERIAL = "htkwv";
    private static final String TEST_MODEL = "Сателлит Online";

    @Test
    @Order(1)
    @DisplayName("Проверка заголовка страницы устройств")
    @Description("Проверка, что заголовок страницы 'Устройства ПМП' отображается корректно")
    @Severity(SeverityLevel.BLOCKER)
    void testPageTitle() {
        printHeader("TC-001.1: Проверка заголовка страницы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        DevicesPage devicesPage = homePage.navigateToDevicesPage();
        String actualTitle = devicesPage.getPageTitle();

        assertThat(actualTitle)
                .as("Заголовок страницы должен быть 'Устройства ПМП'")
                .isEqualTo("Устройства ПМП");

        // Скриншот в конце теста
        devicesPage.takeScreenshot("tc001-1-page-title-checked");
        Allure.addAttachment("Результат", "Заголовок страницы корректно отображается");
    }

    @Test
    @Order(2)
    @DisplayName("Проверка отображения таблицы")
    @Description("Проверка, что таблица с устройствами видима на странице")
    @Severity(SeverityLevel.CRITICAL)
    void testTableVisibility() {
        printHeader("TC-001.2: Проверка видимости таблицы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        DevicesPage devicesPage = homePage.navigateToDevicesPage();
        boolean isVisible = devicesPage.isTableVisible();

        assertThat(isVisible)
                .as("Таблица устройств должна быть видима")
                .isTrue();

        // Скриншот в конце теста
        devicesPage.takeScreenshot("tc001-2-table-visible");
        Allure.addAttachment("Результат", "Таблица устройств видима на странице");
    }

    @Test
    @Order(3)
    @DisplayName("Поиск устройства по серийному номеру")
    @Description("Проверка функционала поиска устройства по серийному номеру")
    @Severity(SeverityLevel.CRITICAL)
    void testDeviceSearch() {
        printHeader("TC-001.3: Поиск устройства по серийному номеру");

        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .model(TEST_MODEL)
                .build();

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        DevicesPage devicesPage = homePage.navigateToDevicesPage();
        devicesPage.searchDevice(testDevice);

        boolean isFound = devicesPage.isDeviceFound(TEST_SERIAL);

        assertThat(isFound)
                .as("Устройство с серийным номером %s должно быть найдено", TEST_SERIAL)
                .isTrue();

        // Скриншот в конце теста
        devicesPage.takeScreenshot("tc001-3-device-found");
        Allure.addAttachment("Результат", String.format("Устройство с серийным номером %s успешно найдено", TEST_SERIAL));
    }

    @Test
    @Order(4)
    @DisplayName("Проверка наличия кнопки экспорта")
    @Description("Проверка, что на странице присутствует кнопка 'Экспорт данных'")
    @Severity(SeverityLevel.NORMAL)
    void testExportButton() {
        printHeader("TC-001.4: Проверка наличия кнопки экспорта");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        DevicesPage devicesPage = homePage.navigateToDevicesPage();
        boolean isExportButtonVisible = devicesPage.isExportButtonVisible();

        assertThat(isExportButtonVisible)
                .as("Кнопка 'Экспорт данных' должна быть видима")
                .isTrue();

        // Скриншот в конце теста
        devicesPage.takeScreenshot("tc001-4-export-button-visible");
        Allure.addAttachment("Результат", "Кнопка 'Экспорт данных' видима на странице");
    }

    @Test
    @Order(5)
    @DisplayName("Переход на страницу информации об устройстве")
    @Description("Проверка перехода на страницу детальной информации об устройстве по клику на серийный номер")
    @Severity(SeverityLevel.CRITICAL)
    void testDeviceInfoNavigation() {
        printHeader("TC-001.5: Переход на страницу информации об устройстве");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        DevicesPage devicesPage = homePage.navigateToDevicesPage();

        // Сначала находим нужное устройство
        DeviceEntity testDevice = DeviceEntity.builder()
                .serialNumber(TEST_SERIAL)
                .build();
        devicesPage.searchDevice(testDevice);

        // Убеждаемся, что устройство найдено
        assertThat(devicesPage.isDeviceFound(TEST_SERIAL))
                .as("Устройство с серийным номером %s должно быть найдено перед переходом", TEST_SERIAL)
                .isTrue();

        // Кликаем по серийному номеру в таблице (первый в результатах поиска)
        DeviceInfoPage infoPage = devicesPage.clickOnDeviceBySerial(TEST_SERIAL);

        // Проверяем, что перешли на страницу информации
        assertThat(infoPage.isDeviceInfoPageLoaded())
                .as("Должна открыться страница информации об устройстве")
                .isTrue();

        infoPage.takeScreenshot("tc001-5-device-info-page");
    }
}