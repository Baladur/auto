package rzhd;

import com.roman.base.UniDriver;
import com.roman.rzhd.elements.Button;
import com.roman.rzhd.elements.TextField;
import junit.framework.TestCase;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by roman on 29.12.2016.
 */
public class RzhdBaseTest extends TestCase {
    private UniDriver driver;

    public RzhdBaseTest() {
        driver = new UniDriver(UniDriver.TYPE.CHROME);
    }

    @Test
    public void testForTickets() {
        driver.navigate().to("http://www.rzd.ru");

        driver.textfield(TextField.FROM).setText("МОСКВА");
        driver.textfield(TextField.TO).setText("МОЖГА");
        for (int i = 0; i < 3; i++) {
            driver.click(Button.CALENDAR_RIGHT);
        }
        driver.click(Button.SUBMIT);
    }
}
