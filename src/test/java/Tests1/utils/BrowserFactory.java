package Tests1.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import static Tests1.config.ConfigurationManager.config;

public enum BrowserFactory {
    CHROMIUM {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.chromium().launch(options());
        }
    },
    FIREFOX {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.firefox().launch(options());
        }
    },
    WEBKIT {
        @Override
        public Browser createInstance(Playwright playwright) {
            return playwright.webkit().launch(options());
        }
    };

    public BrowserType.LaunchOptions options() {
        return new BrowserType.LaunchOptions()
                .setHeadless(config().headless())
                .setSlowMo(config().slowMotion());
    }

    public abstract Browser createInstance(Playwright playwright);
}