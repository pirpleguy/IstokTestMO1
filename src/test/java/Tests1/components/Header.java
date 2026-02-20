package Tests1.components;

import com.microsoft.playwright.Page;

public class Header extends BaseComponent {

    public Header(Page page) {
        super(page);
    }

    public void clickOnBurgerMenu() {
        page.click("//button[@data-class='pmp-button-sidebar']");
    }

    public boolean isBurgerMenuVisible() {
        return page.locator("//button[@data-class='pmp-button-sidebar']").isVisible();
    }
}