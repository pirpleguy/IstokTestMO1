package Tests1.tests;

import Tests1.models.SubscriptionEntity;
import Tests1.pages.HomePage;
import Tests1.pages.SubscriptionPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static Tests1.config.ConfigurationManager.config;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Управление подписками")
@Feature("Страница подписок")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionTests extends BaseTest {

    private static final String TEST_SUBSCRIPTION_ID = "a4947e2d-7865-4cb9-9d03-3e0649426ef1";

    @Test
    @Order(1)
    @DisplayName("Проверка заголовка страницы подписок")
    @Description("Проверка, что заголовок страницы 'Подписки' отображается корректно")
    @Severity(SeverityLevel.BLOCKER)
    void testPageTitle() {
        printHeader("TC-003.1: Проверка заголовка страницы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        SubscriptionPage subscriptionPage = homePage.navigateToSubscriptionPage();
        String actualTitle = subscriptionPage.getPageTitle();

        assertThat(actualTitle)
                .as("Заголовок страницы должен быть 'Подписки'")
                .isEqualTo("Подписки");

        subscriptionPage.takeScreenshot("tc003-1-title-checked");
    }

    @Test
    @Order(2)
    @DisplayName("Проверка отображения таблицы подписок")
    @Description("Проверка, что таблица с подписками видима на странице")
    @Severity(SeverityLevel.CRITICAL)
    void testTableVisibility() {
        printHeader("TC-003.2: Проверка видимости таблицы");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        SubscriptionPage subscriptionPage = homePage.navigateToSubscriptionPage();
        boolean isVisible = subscriptionPage.isTableVisible();

        assertThat(isVisible)
                .as("Таблица подписок должна быть видима")
                .isTrue();

        subscriptionPage.takeScreenshot("tc003-2-table-visible");
    }

    @Test
    @Order(3)
    @DisplayName("Поиск подписки по идентификатору")
    @Description("Проверка расширенного поиска подписки по ID")
    @Severity(SeverityLevel.CRITICAL)
    void testSearchBySubscriptionId() {
        printHeader("TC-003.3: Поиск подписки по идентификатору");

        SubscriptionEntity testSubscription = SubscriptionEntity.builder()
                .id(TEST_SUBSCRIPTION_ID)
                .build();

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        SubscriptionPage subscriptionPage = homePage.navigateToSubscriptionPage();
        subscriptionPage.searchSubscription(testSubscription);

        boolean isFound = subscriptionPage.isSubscriptionFound(TEST_SUBSCRIPTION_ID);

        assertThat(isFound)
                .as("Подписка с ID %s должна быть найдена", TEST_SUBSCRIPTION_ID)
                .isTrue();

        subscriptionPage.takeScreenshot("tc003-3-subscription-found");
    }

    @Test
    @Order(4)
    @DisplayName("Проверка работы модального окна расширенного поиска")
    @Description("Проверка, что модальное окно расширенного поиска открывается")
    @Severity(SeverityLevel.NORMAL)
    void testAdvancedSearchModal() {
        printHeader("TC-003.4: Проверка открытия модального окна");

        HomePage homePage = loginPage.loginAs(
                config().testUsername(),
                config().testPassword()
        );

        SubscriptionPage subscriptionPage = homePage.navigateToSubscriptionPage();
        subscriptionPage.openAdvancedSearch();

        SubscriptionEntity testSubscription = SubscriptionEntity.builder()
                .id(TEST_SUBSCRIPTION_ID)
                .build();

        subscriptionPage.enterSubscriptionId(testSubscription);
        subscriptionPage.clickOk();

        boolean isFound = subscriptionPage.isSubscriptionFound(TEST_SUBSCRIPTION_ID);

        assertThat(isFound)
                .as("После поиска подписка должна быть найдена")
                .isTrue();

        subscriptionPage.takeScreenshot("tc003-4-advanced-search-result");
    }
}