package Tests1.components;

import com.microsoft.playwright.Page;

public class SideNavMenu extends BaseComponent {

    public SideNavMenu(Page page) {
        super(page);
    }

    public void clickOnDevices() {
        page.click("//li[@data-class='pmp-sidebar-menu-devices']");
    }

    public void clickOnTasks() {
        page.click("//li[@data-class='pmp-sidebar-menu-tasks']");
    }

    public void clickOnSubscriptions() {
        page.click("//li[@data-class='pmp-sidebar-menu-subscriptions']");
    }

    public boolean isMenuVisible() {
        return page.locator("//li[@data-class='pmp-sidebar-menu-devices']").isVisible();
    }
}