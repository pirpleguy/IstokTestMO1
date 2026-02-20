package Tests1.pages;

import Tests1.components.*;
import Tests1.models.TaskEntity;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;

public class TaskPage extends BasePage {

    private Header header;
    private SideNavMenu sideNavMenu;
    private ModalWindow modalWindow;
    private Button button;

    private final String PAGE_TITLE = "//span[text()='Программы дистанционного мониторинга']";
    private final String TABLE = "//div[@class='ant-table-content']";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";
    private final String FIRST_ROW_ID = "//tr[@class='ant-table-row ant-table-row-level-0']//td[4]";

    @Override
    public void initComponents() {
        header = new Header(page);
        sideNavMenu = new SideNavMenu(page);
        modalWindow = new ModalWindow(page);
        button = new Button(page);
    }

    @Step("Проверить заголовок страницы")
    public String getPageTitle() {
        return page.locator(PAGE_TITLE).first().textContent();
    }

    @Step("Проверить видимость таблицы")
    public boolean isTableVisible() {
        return page.locator(TABLE).isVisible();
    }

    @Step("Получить количество колонок")
    public int getColumnsCount() {
        return page.locator("//div//table//tr//th").count();
    }

    @Step("Выполнить поиск по ID заказа")
    public TaskPage searchByOrderId(TaskEntity task) {
        waitForElement(SEARCH_INPUT);
        fill(SEARCH_INPUT, task.getId());
        click(SEARCH_BUTTON);
        waitForTimeout(3000);
        takeScreenshot("task-search-results");
        return this;
    }

    @Step("Проверить наличие программы")
    public boolean isTaskFound(String taskId) {
        return page.locator("text=" + taskId).count() > 0;
    }

    @Step("Получить ID из первой строки")
    public String getFirstTaskId() {
        return page.locator(FIRST_ROW_ID).first().textContent().trim();
    }

    @Step("Проверить наличие кнопки экспорта")
    public boolean isExportButtonVisible() {
        return page.getByText("Экспорт данных").isVisible();
    }
}