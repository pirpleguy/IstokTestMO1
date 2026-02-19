package tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.*;
import pages.MonitoringPage;
import java.nio.file.Paths;

public class MonitoringTests extends BaseTest {

    @Test
    @DisplayName("TC002: Monitoring Program Search")
    void testMonitoringProgram() {
        String orderId = "c134009d-99d4-4958-a7a7-7b3ddd3dbef1";

        printHeader("TC-002: Search monitoring program by ID");

        try {
            // Login first
            loginPage.loginAs("TestMO1", "AEJPx7BY7OJOBF2K");

            // Create MonitoringPage and search
            MonitoringPage monitoringPage = new MonitoringPage(page);
            monitoringPage.searchByOrderId(orderId);

            // Verify results
            if (monitoringPage.isProgramFound(orderId)) {
                System.out.println("Program found!");
                monitoringPage.printProgramDetails();
                monitoringPage.takeScreenshot("tc002-success");
                System.out.println("\nTC-002: PASSED");
            } else {
                System.out.println("Program not found!");
                monitoringPage.takeScreenshot("tc002-failed");
                Assertions.fail("Program with order ID " + orderId + " not found");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc002-error.png")));
            throw e;
        }
    }
}