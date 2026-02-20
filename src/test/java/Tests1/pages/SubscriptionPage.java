package Tests1.pages;

import Tests1.components.*;
import Tests1.models.SubscriptionEntity;
import io.qameta.allure.Step;

public class SubscriptionPage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;
    private ModalWindow modalWindow;
    private Button button;

    private final String PAGE_TITLE = "//span[text()='Подписки']";
    private final String TABLE = "//div[@class='ant-table-content']";
    private final String ADVANCED_SEARCH_BUTTON = "//button[@data-class='pmp-button-search']";
    private final String MODAL_DIALOG = "div.ant-modal[role='dialog']";
    private final String ID_FIELD = "input#id";
    private final String OK_BUTTON = "button[type='submit']:has-text('OK')";
    private final String FIRST_ROW_ID = "//tr[@class='ant-table-row ant-table-row-level-0']//td[2]";

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
        modalWindow = new ModalWindow(page);
        button = new Button(page);
    }

    @Step("Проверить заголовок страницы")
    public String getPageTitle() {
        return page.locator(PAGE_TITLE).nth(1).textContent();
    }

    @Step("Проверить видимость таблицы")
    public boolean isTableVisible() {
        return page.locator(TABLE).isVisible();
    }

    @Step("Получить количество колонок")
    public int getColumnsCount() {
        return page.locator("//div//table//tr//th").count();
    }

    @Step("Открыть расширенный поиск")
    public SubscriptionPage openAdvancedSearch() {
        waitForElement(ADVANCED_SEARCH_BUTTON);
        click(ADVANCED_SEARCH_BUTTON);
        waitForElement(MODAL_DIALOG);
        takeScreenshot("advanced-search-modal");
        return this;
    }

    @Step("Ввести ID подписки")
    public SubscriptionPage enterSubscriptionId(SubscriptionEntity subscription) {
        waitForElement(ID_FIELD);
        fill(ID_FIELD, subscription.getId());
        return this;
    }

    @Step("Нажать OK")
    public SubscriptionPage clickOk() {
        waitForElement(OK_BUTTON);
        click(OK_BUTTON);
        waitForTimeout(3000);
        takeScreenshot("subscription-search-results");
        return this;
    }

    @Step("Выполнить поиск подписки")
    public SubscriptionPage searchSubscription(SubscriptionEntity subscription) {
        return openAdvancedSearch()
                .enterSubscriptionId(subscription)
                .clickOk();
    }

    @Step("Проверить наличие подписки")
    public boolean isSubscriptionFound(String subscriptionId) {
        return page.locator("text=" + subscriptionId).count() > 0;
    }

    @Step("Получить ID из первой строки")
    public String getFirstSubscriptionId() {
        return page.locator(FIRST_ROW_ID).first().textContent().trim();
    }
}