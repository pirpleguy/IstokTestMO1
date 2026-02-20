package Tests1.components;

import com.microsoft.playwright.Page;

public class Button extends BaseComponent {

    public Button(Page page) {
        super(page);
    }

    public void clickSearch() {
        page.click("//button[@data-class='pmp-button-search']");
    }

    public void clickAdvancedSearch() {
        page.click("//button[@data-class='pmp-button-search']");
    }

    public boolean isButtonEnabled(String buttonText) {
        return page.locator("button:has-text('" + buttonText + "')").isEnabled();
    }
}