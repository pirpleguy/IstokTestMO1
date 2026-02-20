package Tests1.components;

import com.microsoft.playwright.Page;

public class ModalWindow extends BaseComponent {

    public ModalWindow(Page page) {
        super(page);
    }

    public void clickOk() {
        page.click("//span[text()='OK']");
    }

    public void clickCancel() {
        page.click("//span[text()='Отмена']");
    }

    public void clickExport() {
        page.getByText("Экспорт данных").click();
    }

    public void selectFromDropdown(String selector, String optionText) {
        if (optionText != null && !optionText.isEmpty()) {
            page.locator(selector).click();
            page.locator("div.ant-select-item-option-content:has-text('" + optionText + "')").click();
        }
    }

    public boolean isModalVisible() {
        return page.locator("div.ant-modal[role='dialog']").isVisible();
    }
}