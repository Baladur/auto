package kinopoisk;

import com.roman.base.BaseTest;
import com.roman.base.StepInfo;
import com.roman.base.TestInfo;
import com.roman.kinopoisk.elements.Button;
import com.roman.kinopoisk.elements.Select;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roman on 10.03.2017.
 */
@TestInfo(name = "КИНОПОИСК-6",
description = "Случайный фильм.")
public class Kinopoisk6 extends BaseTest {
    private String country = "Россия";

    public Kinopoisk6 withCountry(String country) {
        this.country = country;
        return this;
    }

    @Test
    public void test() {
        execute();
    }

    @StepInfo(name = "Переход в приложение.")
    public void step1() {
        new Kinopoisk1().execute();
        driver.click(Button.CHANCE_BTN).end();
    }

    @StepInfo(name = "Первичное нажатие на кнопку \"Случайный фильм\".")
    public void step2() {
        driver.wait(() -> driver.find(Select.AFISHA_SELECT).isDisplayed())
                .duringSeconds(5)
                .orElse(() -> {
                    driver.report().log("Селект афиша не появился.");
                }).end();
        driver.click(Button.RANDOM_MOVIE_BTN).end();
        assertThat(driver.find(Button.RANDOM_FILM_NAME).isDisplayed()).describedAs("Случайный фильм не появился.").isTrue();
    }

    @StepInfo(name = "Вторичное нажатие на кнопку \"Случайный фильм\".")
    public void step3() {
        driver.click(Button.RANDOM_MOVIE_BTN).end();
        driver.wait(() -> driver.find(Button.RANDOM_FILM_NAME).isDisplayed())
                .duringSeconds(1)
                .orElse(() -> { throw new AssertionError("Беда."); })
                .end();
    }

    @StepInfo(name = "Проверка селекта стран.")
    public void step4() {
        driver.select(Select.CHANCE_COUNTRY_SELECT).optionContains(country).duringSeconds(2)
        .orElse(() -> {
            country = "США";
            driver.select(Select.CHANCE_COUNTRY_SELECT).optionContains(country).duringSeconds(2).end();
        }).end();
        driver.click(Button.RANDOM_MOVIE_BTN).end();
        driver.wait(() -> driver.find(Button.RANDOM_FILM_NAME).isDisplayed()).duringSeconds(5).end();
        WebElement description = driver.findByCss(".filmName+div");
        driver.report().screenshot(description, "Описание фильма");
        assertThat(description.getText()).describedAs("Найден фильм, снятый в стране, не указанной в селекторе стран.").containsIgnoringCase(country);
    }
}
