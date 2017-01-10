package com.roman.base;

import com.roman.AutogenException;
import com.roman.JSONToEnumConverter;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;

import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 12.11.2016.
 */
public class UniDriver {
    private static final String XPATH_PREFIX = "xpath";
    private static final String CSS_PREFIX = "css";

    public enum TYPE {
        FIREFOX,
        CHROME,
        IE,
        EDGE,
        OPERA
    }

    private WebDriver driver;

    public UniDriver() {
        ProfilesIni pi = new ProfilesIni();
        FirefoxProfile profile = pi.getProfile("AutoTester");
        //profile.addExtension(new File("C:/Program Files (x86)/Mozilla Firefox/browser/extensions/{972ce4c6-7e08-4474-a285-3208198ce6fd}.xpi"));
        driver = new FirefoxDriver();
    }

    public UniDriver(TYPE type) {
        switch (type) {
            case FIREFOX: driver = new FirefoxDriver(); break;
            case CHROME:
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                driver = new ChromeDriver(); break;
            case EDGE:
                System.setProperty("webdriver.edge.driver", "MicrosoftWebDriver.exe");
                driver = new EdgeDriver(); break;
            case OPERA:
                System.setProperty("webdriver.opera.driver", "operadriver.exe");
                driver = new OperaDriver(); break;
        }
    }

    public WebElement find(BaseElement element) {
        return find(element, 1);
    }

    public WebElement find(BaseElement element, int index) {
        List<WebElement> foundElements = driver.findElements(toPath(element));
        return foundElements.get(index-1);
    }

    public WebElement findOnPage(BaseElement element) {
        return findOnPage(element, 1);
    }

    public WebElement findOnPage(BaseElement element, int index) {
        int ySize = driver.manage().window().getSize().getHeight();
        int step = 200;
        int totalPassed = 0;
        while (driver.findElements(toPath(element)).size() == 0) {
            scrollY(step);
            totalPassed += step;
            if (Math.abs(totalPassed - ySize) < step) {
                break;
            }
        }
        return find(element, index);
    }

    public UniDriver click(BaseElement element) {
        return click(element, 1);
    }

    public UniDriver click(BaseElement element, int index) {
        findOnPage(element, index).click();
        return this;
    }

    public void scrollY(int y) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(String.format("window.scrollBy(0,%d)", y), "");
    }

    public void scrollX(int x) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(String.format("window.scrollBy(%d, 0)", x), "");
    }

    public WebDriver.Navigation navigate() {
        return driver.navigate();
    }

    public WebDriver.Options manage() { return driver.manage(); }

    public TableObject table(BaseElement element) {
        return new TableObject(this, element);
    }

    public TextField textfield(BaseElement element) { return new TextField(this, element); }

    public WebDriver baseDriver() {
        return driver;
    }

    private By toPath(BaseElement element) {
        String result = "";
        try {
            String pathWithPrefix = PathReader.getInstance(element.getClass().getPackage().getName()).getPath(element.getClass().getSimpleName(), element.getName());
            if (pathWithPrefix.startsWith(XPATH_PREFIX)) {
                result = pathWithPrefix.replace(XPATH_PREFIX, "");
                return By.xpath(result);
            } else if (pathWithPrefix.startsWith(CSS_PREFIX)) {
                result = pathWithPrefix.replace(CSS_PREFIX, "");
                return By.cssSelector(result);
            } else {
                throw new UniFrameworkException("No prefix found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UniFrameworkException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void wait(Time time, int amount) {
        try {
            Thread.sleep(time.getMultiplier() * amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isElementVisible(BaseElement element) {
        List<WebElement> foundElements = driver.findElements(toPath(element));
        if (foundElements.size() > 0) {
            if (foundElements.get(0).isDisplayed()) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }


}
