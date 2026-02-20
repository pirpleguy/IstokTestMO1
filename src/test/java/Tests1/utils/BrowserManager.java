package Tests1.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

import static Tests1.config.ConfigurationManager.config;

public class BrowserManager {

    public static Browser getBrowser(Playwright playwright) {
        return BrowserFactory.valueOf(config().browser().toUpperCase())
                .createInstance(playwright);
    }
}