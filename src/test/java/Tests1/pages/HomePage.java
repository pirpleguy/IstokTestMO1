package Tests1.pages;

import Tests1.components.Header;
import Tests1.components.SideNavMenu;
import Tests1.components.ModalWindow;
import Tests1.utils.BasePageFactory;
import io.qameta.allure.Step;

public class HomePage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;
    private ModalWindow modalWindow;

    private final String PAGE_TITLE = "//span[text()='Текущая статистика']";

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
        modalWindow = new ModalWindow(page);
    }

    @Step("Проверить заголовок главной страницы")
    public String getPageTitle() {
        waitForElement(PAGE_TITLE);
        return page.locator(PAGE_TITLE).textContent();
    }

    @Step("Проверить видимость бургер-меню")
    public boolean isBurgerMenuVisible() {
        return header.isBurgerMenuVisible();
    }

    @Step("Открыть бургер-меню")
    public HomePage openBurgerMenu() {
        header.clickOnBurgerMenu();
        return this;
    }

    @Step("Перейти на страницу устройств")
    public DevicesPage navigateToDevicesPage() {
        openBurgerMenu();
        sideNavMenu.clickOnDevices();
        waitForLoadState();
        return BasePageFactory.createInstance(page, DevicesPage.class);
    }

    @Step("Перейти на страницу программ мониторинга")
    public TaskPage navigateToTaskPage() {
        openBurgerMenu();
        sideNavMenu.clickOnTasks();
        waitForLoadState();
        return BasePageFactory.createInstance(page, TaskPage.class);
    }

    @Step("Перейти на страницу подписок")
    public SubscriptionPage navigateToSubscriptionPage() {
        openBurgerMenu();
        sideNavMenu.clickOnSubscriptions();
        waitForLoadState();
        takeScreenshot("navigate-to-subscriptions");
        return BasePageFactory.createInstance(page, SubscriptionPage.class);
    }

    @Step("Проверить, что главная страница загружена")
    public boolean isHomePageLoaded() {
        return isBurgerMenuVisible() && page.locator(PAGE_TITLE).isVisible();
    }
}