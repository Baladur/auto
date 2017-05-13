package kinopoisk;

import com.roman.base.*;
import com.roman.kinopoisk.elements.Button;
import com.roman.kinopoisk.elements.TextField;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
@TestInfo(name = "КИНОПОИСК-2", description = "Поиск существующего фильма.")
public class Kinopoisk2 extends BaseTest {

    /*****LOCAL VARIABLES*****/

    private String movie = "Большой куш";

    public Kinopoisk2 withMovie(String movie) {
        this.movie = movie;
        return this;
    }

    @Test
    public void test() {
        execute();
        driver.close();
    }

    @StepInfo(name = "Запуск теста КИНОПОИСК-1")
    public void step1() {
        new Kinopoisk1().execute();
    }

    @StepInfo(name = "Ввод фильма в главный поиск.")
    public void step2() {
        driver.fill(TextField.SEARCH).withText(movie).duringSeconds(5).end();
    }

    @StepInfo(name = "Нажатие на кнопку поиска.")
    public void step3() {
        driver.click(Button.SEARCH_BTN).duringSeconds(5).end();
    }

    @StepInfo(name = "Запуск теста КИНОПОИСК-3.")
    public void step4() {
        new Kinopoisk3()
                .withMovie(movie)
                .withShouldBeEquals(true)
                .execute();
    }
}
