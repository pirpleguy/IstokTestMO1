package pages;

import com.microsoft.playwright.Page;

public class MonitoringPage extends BasePage {

    // Locators
    private final String MONITORING_MENU = ".ant-menu-item >> nth=2";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";

    public MonitoringPage(Page page) {
        super(page);
        waitForElement(".ant-menu-item");
        click(MONITORING_MENU);
        waitForLoadState();
        waitForTimeout(3000);
    }

    private void waitForLoadState() {
        page.waitForLoadState();
    }

    public MonitoringPage searchByOrderId(String orderId) {
        System.out.println("Waiting for search input...");
        waitForElement(SEARCH_INPUT);
        waitForTimeout(1000);

        System.out.println("Searching by order ID: " + orderId);
        fill(SEARCH_INPUT, orderId);
        waitForTimeout(1000);

        System.out.println("Clicking search button...");
        click(SEARCH_BUTTON);
        waitForTimeout(4000);

        return this;
    }

    public boolean isProgramFound(String orderId) {
        String shortId = orderId.substring(0, 8);
        return page.locator("text=" + shortId).count() > 0;
    }

    public String getOrderId() {
        return getCellText(4);
    }

    public String getOrderStatus() {
        return getCellText(5);
    }

    public String getDeviceSerial() {
        return getCellText(8);
    }

    public void printProgramDetails() {
        System.out.println("\nProgram details:");
        System.out.println("  Order ID: " + getOrderId());
        System.out.println("  Status: " + getOrderStatus());
        System.out.println("  Device serial: " + getDeviceSerial());
    }
}