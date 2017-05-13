package kinopoisk;

import com.relevantcodes.extentreports.LogStatus;
import com.roman.base.*;
import com.roman.kinopoisk.elements.Button;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roman on 05.03.2017.
 */
@TestInfo(name = "КИНОПОИСК-3", description = "Проверка результата поиска.")
public class Kinopoisk3 extends BaseTest {

    private String movie = "";
    private boolean shouldBeEquals = true;

    public Kinopoisk3 withMovie(String movie) {
        this.movie = movie;
        return this;
    }

    public Kinopoisk3 withShouldBeEquals(boolean shouldBeEquals) {
        this.shouldBeEquals = shouldBeEquals;
        return this;
    }

    @Test
    public void test() throws Throwable {
        execute();
    }

    @StepInfo(name = "Проверка результата поиска.")
    public void step1() {
        assertThat(shouldBeEquals && driver.find(Button.MOST_WANTED).getText().equals(movie)).describedAs("Отрицательная проверка.").isTrue();
        driver.report().log("Проверка результата поиска пройдена.");
    }

    @StepInfo(name = "Запуск теста КИНОПОИСК-4.")
    public void step2() {
        new Kinopoisk4().execute();
    }
}
