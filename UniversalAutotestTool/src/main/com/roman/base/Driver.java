package com.roman.base;

import lombok.extern.slf4j.Slf4j;
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
import org.openqa.selenium.opera.OperaDriver;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by roman on 12.11.2016.
 */
/* TODO:
1) Разделить методы на публичные и приватные
2) Ввести классы билдеров действий
Пример использования:
driver.click(TextField.USERNAME).duringSeconds(Config.getTimeout()).end();
driver.fill(
driver.table(Table.GAME_RESULTS)
.rowWhere().columnEquals("Место", "1").columnWithName("Команда")
.asTextField())
.withText("Рубин")
.end();
driver.select(Select.SORTING).optionWhere().optionContains("возрастанию").duringSeconds(5).end();
wait(() -> driver.rowAsMap(Label.INDICATOR).isDisplayed(), 5)
    .else().message(ERROR_MESSAGE).end();
assertThat(driver.table(Table.DOCUMENTS)
.rowWhere().columnWithName("Статус").equals("Обработано")
.and().columnWithName("Дата").contains(LocalDate.now().format())
.columnWithIndex(5).asString().equals("Expected"))
.describedAs("wtf");
 */
@Slf4j
public class Driver {
    private static final String XPATH_PREFIX = "xpath";
    private static final String CSS_PREFIX = "css";
    private static Driver instance;
    private WebDriver driver;
    private ReportLogger reportLogger;
    private boolean isClosed = false;

    public ReportLogger report() {
        return reportLogger;
    }

    public enum TYPE {
        FIREFOX,
        CHROME,
        IE,
        EDGE,
        OPERA
    }

    private Driver() {
        ProfilesIni pi = new ProfilesIni();
        FirefoxProfile profile = pi.getProfile("AutoTester");
        //profile.addExtension(new File("C:/Program Files (x86)/Mozilla Firefox/browser/extensions/{972ce4c6-7e08-4474-a285-3208198ce6fd}.xpi"));
        driver = new FirefoxDriver();
        reportLogger = new ExtentReportLogger(this);
    }

    private Driver(TYPE type) {
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
        reportLogger = new ExtentReportLogger(this);
    }

    public static Driver getDriver() {
        if (instance == null) {
            instance = new Driver(TYPE.CHROME);
            instance.manage().window().maximize();
        }
        return instance;
    }

    protected WebDriver baseDriver() {
        return driver;
    }

    protected List<By> toPaths(BaseElement element) {
        try {
            List<String> paths = PathReader.getInstance(element.getClass().getPackage().getName())
                    .getPaths(element.getClass().getSimpleName(), element.getName());
            return paths
                    .stream()
                    .map(path -> {
                        if (path.startsWith(XPATH_PREFIX)) {
                            return By.xpath(path.replace(XPATH_PREFIX, ""));
                        } else {
                            return By.cssSelector(path.replace(CSS_PREFIX, ""));
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UniFrameworkException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scrollY(int y) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(String.format("window.scrollBy(0,%d)", y), "");
    }

    private void scrollX(int x) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(String.format("window.scrollBy(%d, 0)", x), "");
    }

    public WebElement find(BaseElement element, int index) {
        List<WebElement> foundElements = driver.findElements(toPaths(element).get(0));
        if (foundElements.size() == 0) {
            throw new AssertionError(LogMessages.elementNotFound(element));
        }
        return foundElements.get(index-1);
    }

    public WebElement find(BaseElement element, String text, Match match) {
        List<WebElement> foundElements = driver.findElements(toPaths(element).get(0));
        if (foundElements.size() == 0) {
            throw new AssertionError(LogMessages.elementNotFound(element));
        }
        return foundElements
                .stream()
                .filter(w -> match.compare(text, w.getText()) || match.compare(text, w.getAttribute("value")))
                .findFirst()
                .orElseThrow(() -> new AssertionError(LogMessages.elementWithTextNotFound(element, text)));
    }

    public WebElement findOnPage(BaseElement element, int index) {
        int ySize = driver.manage().window().getSize().getHeight();
        int step = 200;
        int totalPassed = 0;
        List<WebElement> foundElements = new ArrayList<>();
        do {
            foundElements = driver.findElements(toPaths(element).get(0));
            scrollY(step);
            totalPassed += step;
            if (Math.abs(totalPassed - ySize) < step) {
                break;
            }
        } while (foundElements.size() == 0 || !foundElements.get(0).isDisplayed());
        return foundElements.get(index-1);
    }

    public WebElement find(BaseElement element) {
        return find(element, 1);
    }

    public WebElement findOnPage(BaseElement element) {
        return findOnPage(element, 1);
    }

    public List<WebElement> findOptions(BaseElement element) {
        return driver.findElements(toPaths(element).get(1));
    }

    public WebDriver.Navigation navigate() {
        return driver.navigate();
    }

    public WebDriver.Options manage() { return driver.manage(); }

    public IRowWhere table(BaseElement element) {
        return new TableAccessor(this, element);
    }

    public void close() {
        if (!isClosed) {
            driver.close();
            reportLogger.close();
            isClosed = true;
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isElementVisible(BaseElement element) {
        List<WebElement> foundElements = driver.findElements(toPaths(element).get(0));
        if (foundElements.size() > 0) {
            return foundElements.get(0).isDisplayed();
        }
        return false;
    }

    public ISelectMatch select(BaseElement selectElement) {
        return new SelectAction(this, selectElement);
    }

    public IWithText fill(BaseElement fillElement) {
        return new FillAction(this, fillElement);
    }

    public IWithTime click(BaseElement clickElement) {
        return new ClickAction(this, clickElement);
    }

    public ISelectMatch select(BaseElement selectElement, int elementIndex) {
        return new SelectAction(this, selectElement, elementIndex);
    }

    public IWithText fill(BaseElement fillElement, int elementIndex) {
        return new FillAction(this, fillElement, elementIndex);
    }

    public IWithTime click(BaseElement clickElement, int elementIndex) {
        return new ClickAction(this, clickElement, elementIndex);
    }

    public IWithTime wait(Supplier<Boolean> condition) { return new WaitAction(this, condition); }

    public void go(String url) {
        new GoAction(this, url).end();
    }

    public WebElement findByCss(String cssSelector) {
        try {
            return driver.findElement(By.cssSelector(cssSelector));
        } catch (Throwable t) {
            throw new AssertionError(LogMessages.failFindByCss(cssSelector));
        }
    }

    public WebElement findByXPath(String xpath) {
        try {
            return driver.findElement(By.xpath(xpath));
        } catch (Throwable t) {
            throw new AssertionError(LogMessages.failFindByXPath(xpath));
        }
    }
}
