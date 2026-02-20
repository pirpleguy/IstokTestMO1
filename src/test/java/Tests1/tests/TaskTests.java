package Tests1.tests;

import Tests1.models.TaskEntity;
import Tests1.pages.TaskPage;
import Tests1.pages.HomePage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static Tests1.config.ConfigurationManager.config;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Управление программами мониторинга")
@Feature("Страница программ дистанционного мониторинга")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskTests extends BaseTest {

    private static final String TEST_ORDER_ID = "c134009d-99d4-4958-a7a7-7b3ddd3dbef1";

    @Test
    @Order(1)
    @DisplayName("Проверка заголовка страницы")
    @Description("Проверка, что заголовок страницы отображается корректно")
    @Severity(SeverityLevel.BLOCKER)
    void testPageTitle() {
        printHeader("TC-002.1: Проверка заголовка страницы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        TaskPage taskPage = homePage.navigateToTaskPage();
        String actualTitle = taskPage.getPageTitle();

        assertThat(actualTitle)
                .as("Заголовок страницы должен быть 'Программы дистанционного мониторинга'")
                .isEqualTo("Программы дистанционного мониторинга");

        taskPage.takeScreenshot("tc002-1-title-checked");
    }

    @Test
    @Order(2)
    @DisplayName("Проверка отображения таблицы")
    @Description("Проверка, что таблица с программами видима на странице")
    @Severity(SeverityLevel.CRITICAL)
    void testTableVisibility() {
        printHeader("TC-002.2: Проверка видимости таблицы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        TaskPage taskPage = homePage.navigateToTaskPage();
        boolean isVisible = taskPage.isTableVisible();

        assertThat(isVisible)
                .as("Таблица программ должна быть видима")
                .isTrue();

        taskPage.takeScreenshot("tc002-2-table-visible");
    }

    @Test
    @Order(3)
    @DisplayName("Поиск программы по ID заказа")
    @Description("Проверка поиска программы мониторинга по идентификатору заказа")
    @Severity(SeverityLevel.CRITICAL)
    void testSearchByOrderId() {
        printHeader("TC-002.3: Поиск программы по ID заказа");

        TaskEntity testTask = TaskEntity.builder()
                .id(TEST_ORDER_ID)
                .build();

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        TaskPage taskPage = homePage.navigateToTaskPage();
        taskPage.searchByOrderId(testTask);

        boolean isFound = taskPage.isTaskFound(TEST_ORDER_ID);

        assertThat(isFound)
                .as("Программа с ID заказа %s должна быть найдена", TEST_ORDER_ID)
                .isTrue();

        taskPage.takeScreenshot("tc002-3-task-found");
    }

    @Test
    @Order(4)
    @DisplayName("Проверка наличия кнопки экспорта")
    @Description("Проверка, что на странице присутствует кнопка 'Экспорт данных'")
    @Severity(SeverityLevel.NORMAL)
    void testExportButton() {
        printHeader("TC-002.4: Проверка наличия кнопки экспорта");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        TaskPage taskPage = homePage.navigateToTaskPage();
        boolean isExportButtonVisible = taskPage.isExportButtonVisible();

        assertThat(isExportButtonVisible)
                .as("Кнопка 'Экспорт данных' должна быть видима")
                .isTrue();

        taskPage.takeScreenshot("tc002-4-export-button-visible");
    }
}