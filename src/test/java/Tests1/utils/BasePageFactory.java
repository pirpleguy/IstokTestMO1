package Tests1.utils;

import Tests1.pages.BasePage;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BasePageFactory {

    public static <T extends BasePage> T createInstance(final Page page, final Class<T> clazz) {
        try {
            BasePage instance = clazz.getDeclaredConstructor().newInstance();
            instance.setPage(page);
            instance.initComponents();
            return clazz.cast(instance);
        } catch (Exception e) {
            log.error("Ошибка при создании экземпляра страницы {}", clazz.getSimpleName(), e);
            throw new RuntimeException("Не удалось создать экземпляр страницы: " + clazz.getSimpleName(), e);
        }
    }
}