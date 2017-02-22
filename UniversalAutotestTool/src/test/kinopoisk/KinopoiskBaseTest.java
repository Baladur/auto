package kinopoisk;

import com.roman.base.Time;
import com.roman.base.UniDriver;
import com.roman.base.UniFrameworkException;
import com.roman.kinopoisk.elements.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created by roman on 19.11.2016.
 */
public class KinopoiskBaseTest extends TestCase {
    private UniDriver driver;
    private final String SHORT_USERNAME = "aw";
    private final String VALID_USERNAME = "awef";
    private final String PASSWORD = "1";
    private final String INVALID_EMAIL = "1";
    private final String VALID_EMAIL = "waef@gmail.com";
    private final String RANDOM_CAPTCHA = "awef";

    private final String CORRECT_FILM = "Большой куш";
    private final String INCORRECT_FILM = "aewfiuaewhfiuhiiuoawefh";

    public void setUp() {
        driver = new UniDriver(UniDriver.TYPE.CHROME);
        driver.manage().window().maximize();
    }

    @Test
    public void test1() throws UniFrameworkException {
        driver.navigate().to("https://www.kinopoisk.ru/");
        driver.select(Select.AFISHA_SELECT).option().startsWith("awefawe").withSeconds(10);
    }

    @After
    public void after() {
        driver.close();
    }
}

