package Tests1.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:config.properties"})
public interface Configuration extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("browser")
    String browser();

    @Key("headless")
    boolean headless();

    @Key("slow.motion")
    int slowMotion();

    @Key("timeout")
    int timeout();

    @Key("video")
    boolean video();

    @Key("video.dir")
    String videoDir();

    @Key("download.dir")
    String downloadDir();

    @Key("allure.results.directory")
    String allureResultsDir();

    @Key("test.username")
    String testUsername();

    @Key("test.password")
    String testPassword();
}