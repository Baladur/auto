package kinopoisk;

import com.roman.base.*;
import com.roman.kinopoisk.elements.Button;
import com.roman.kinopoisk.elements.Select;
import com.roman.kinopoisk.elements.TextField;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by roman on 19.11.2016.
 */
@Slf4j
@TestInfo(name = "КИНОПОИСК1", description = "Базовые проверки.")
public class KinopoiskBaseTest extends BaseTest {

    @Test
    public void test() throws UniFrameworkException {
        execute();
    }

    @StepInfo(name = "Вход на кинопоиск.")
    public void step1() throws UniFrameworkException {
        driver.navigate().to("https://www.kinopoisk.ru/top");
        log.info("Заходим на сайт.");
        driver.fill(TextField.SEARCH).withText("Большой куш").duringMinutes(5).end();
        driver.click(Button.SEARCH_BTN).end();
        driver.select(Select.AFISHA_SELECT).optionStartsWith("сегодня").duringSeconds(15).end();
//        driver.select(Select.RATINGS_SELECT).option().contains("Top250").end();
//        driver.table(Table.RATING_TABLE)
//                .rowWhere().column(2).startsWith("8.7")
//                .and().column("фильм").startsWith("Бо")
//                .cellByHeader("фильм").asElement()
//                .findElement(By.tagName("a")).click();
//        Shutterbug.shootPage(driver.baseDriver())
//                .highlightWithText(driver.find(TextField.SEARCH), Color.orange, 3, "Главное поле поиска", Color.orange, new Font("SansSerif", Font.BOLD, 20))
//                .highlightWithText(driver.find(Select.AFISHA_SELECT), Color.orange, 3, "Главное поле поиска", Color.orange, new Font("SansSerif", Font.BOLD, 20))
//                .save();
    }

}

