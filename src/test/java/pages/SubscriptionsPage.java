package pages;

import com.microsoft.playwright.Page;

public class SubscriptionsPage extends BasePage {

    // Locators
    private final String SUBSCRIPTIONS_MENU = ".ant-menu-item >> nth=3";
    private final String ADVANCED_SEARCH_BUTTON = "button[data-class='pmp-button-search']";
    private final String MODAL_DIALOG = "div.ant-modal[role='dialog']";
    private final String ID_FIELD = "input#id";
    private final String OK_BUTTON = "button[type='submit']:has-text('OK')";

    public SubscriptionsPage(Page page) {
        super(page);
        waitForElement(".ant-menu-item");
        click(SUBSCRIPTIONS_MENU);
        waitForLoadState();
        waitForTimeout(3000);
    }

    private void waitForLoadState() {
        page.waitForLoadState();
    }

    public SubscriptionsPage openAdvancedSearch() {
        System.out.println("Looking for advanced search button...");
        waitForElement(ADVANCED_SEARCH_BUTTON);
        click(ADVANCED_SEARCH_BUTTON);
        System.out.println("   Clicked button with data-class='pmp-button-search'");
        waitForTimeout(2000);

        System.out.println("Waiting for modal dialog...");
        waitForElement(MODAL_DIALOG);
        waitForTimeout(1000);

        return this;
    }

    public SubscriptionsPage enterSubscriptionId(String subscriptionId) {
        System.out.println("Entering subscription ID...");
        waitForElement(ID_FIELD);
        fill(ID_FIELD, subscriptionId);
        System.out.println("   Entered ID: " + subscriptionId);
        waitForTimeout(1000);
        return this;
    }

    public SubscriptionsPage clickOkButton() {
        System.out.println("Clicking OK button...");
        waitForElement(OK_BUTTON);
        click(OK_BUTTON);
        System.out.println("   Clicked OK button");
        waitForTimeout(4000);
        return this;
    }

    public boolean isSubscriptionFound(String subscriptionId) {
        String shortId = subscriptionId.substring(0, 8);
        return page.locator("text=" + shortId).count() > 0;
    }

    public String getSubscriptionId() {
        return getCellText(2);
    }

    public String getOrganization() {
        return getCellText(3);
    }

    public String getStatus() {
        return getCellText(7);
    }

    public String getType() {
        return getCellText(9);
    }

    public void printSubscriptionDetails() {
        System.out.println("\nSubscription details:");
        System.out.println("  ID: " + getSubscriptionId());
        System.out.println("  Organization: " + getOrganization());
        System.out.println("  Status: " + getStatus());
        System.out.println("  Type: " + getType());
    }

    // Business method
    public SubscriptionsPage searchSubscription(String subscriptionId) {
        return openAdvancedSearch()
                .enterSubscriptionId(subscriptionId)
                .clickOkButton();
    }
}