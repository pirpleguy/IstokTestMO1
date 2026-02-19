package tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.*;
import pages.SubscriptionsPage;
import java.nio.file.Paths;

public class SubscriptionTests extends BaseTest {

    @Test
    @DisplayName("TC003: Subscription Search")
    void testSubscription() {
        String subscriptionId = "a4947e2d-7865-4cb9-9d03-3e0649426ef1";

        printHeader("TC-003: Search subscription by ID");

        try {
            // Login first
            loginPage.loginAs("TestMO1", "AEJPx7BY7OJOBF2K");

            // Create SubscriptionsPage and search
            SubscriptionsPage subscriptionsPage = new SubscriptionsPage(page);
            subscriptionsPage.searchSubscription(subscriptionId);

            // Verify results
            if (subscriptionsPage.isSubscriptionFound(subscriptionId)) {
                System.out.println("Subscription found!");
                subscriptionsPage.printSubscriptionDetails();
                subscriptionsPage.takeScreenshot("tc003-success");
                System.out.println("\nTC-003: PASSED");
            } else {
                System.out.println("Subscription not found!");
                subscriptionsPage.takeScreenshot("tc003-failed");
                Assertions.fail("Subscription with ID " + subscriptionId + " not found");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc003-error.png")));
            throw e;
        }
    }
}