package uspn;

import com.roman.base.UniDriver;
import com.roman.base.UniFrameworkException;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by user on 18.11.2016.
 */
public class USPNBaseTest extends TestCase {
    private UniDriver driver;

    public USPNBaseTest() throws IOException {
        driver = new UniDriver();
        driver.manage().window().maximize();
        driver.navigate().to("https://ais2t.fed.pfr.ru:4443/msk/index");
    }

    @Test
    public void test() throws UniFrameworkException {

    }
}
