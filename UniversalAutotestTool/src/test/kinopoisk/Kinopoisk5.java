package kinopoisk;

import com.roman.base.BaseTest;
import com.roman.base.StepInfo;
import com.roman.base.TestInfo;
import com.roman.bgsha.elements.Select;
import org.junit.Test;

/**
 * Created by roman on 09.03.2017.
 */
@TestInfo(name = "КИНОПОИСК-5", description = "Переход на афишу сегодня.")
public class Kinopoisk5 extends BaseTest {

    @Test
    public void test() {
        execute();
    }

    @StepInfo(name = "Запуск теста КИНОПОИСК-1.")
    public void step1() {
        new Kinopoisk1().execute();
    }

    @StepInfo(name = "Нажатие на афишу сегодня.")
    public void step2() {
        driver.select(Select.AFISHA_SELECT).optionStartsWith("сегодня").end();
    }
}
