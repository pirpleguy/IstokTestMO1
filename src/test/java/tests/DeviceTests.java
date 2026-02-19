package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pages.DevicesPage;
import java.nio.file.Paths;

@Epic("Управление устройствами")
@Feature("Поиск устройств")
public class DeviceTests extends BaseTest {

    @Test
    @DisplayName("TC001: Поиск устройства htkwv")
    @Description("Проверка поиска устройства по серийному номеру")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Поиск устройства в разделе ПМП")
    void testSearchDevice() {
        printHeader("TC-001: Search device 'htkwv'");

        Allure.label("testCase", "TC-001");
        Allure.link("https://test.ppma.ru/portal", "Test Portal");

        try {
            // Login and navigate to Devices page
            DevicesPage devicesPage = loginPage.loginAs("TestMO1", "AEJPx7BY7OJOBF2K");

            // Search for device
            devicesPage.searchDevice("htkwv");

            // Verify results
            if (devicesPage.isDeviceFound("htkwv")) {
                System.out.println("Device found!");
                devicesPage.printDeviceDetails();
                devicesPage.takeScreenshot("tc001-success");

                Allure.addAttachment("Test Result", "text/plain", "PASSED - Device found");
                System.out.println("\nTC-001: PASSED");
            } else {
                System.out.println("Device not found!");
                devicesPage.takeScreenshot("tc001-failed");
                Allure.addAttachment("Test Result", "text/plain", "FAILED - Device not found");
                Assertions.fail("Device htkwv not found");
            }

        } catch (Exception e) {
            handleTestError(e, "TC-001");
            throw e;
        }
    }
}