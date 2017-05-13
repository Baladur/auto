package kinopoisk;

import com.roman.base.BaseTest;
import com.roman.base.StepInfo;
import com.roman.base.TestInfo;
import com.roman.kinopoisk.elements.Button;
import com.roman.kinopoisk.elements.TextField;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roman on 06.03.2017.
 */
@TestInfo(name = "КИНОПОИСК-4", description = "Переход по результату запроса.")
public class Kinopoisk4 extends BaseTest {

    @Test
    public void test() {
        execute();
    }

    @StepInfo(name = "Нажатие на ссылку.")
    public void step1() {
        driver.click(Button.MOST_WANTED).duringSeconds(5).end();
    }

    @StepInfo(name = "Проверка пустоты поля поиска.")
    public void step2() {
        driver.fill(TextField.SEARCH).withText("awefawe").duringSeconds(2).end();
        assertThat(driver.find(TextField.SEARCH).getAttribute("value")).describedAs("Резльутат запроса не очищен.").hasSize(0);
        driver.report().pass("Поле поиска пусто согласно ожиданиям.");
    }
}
