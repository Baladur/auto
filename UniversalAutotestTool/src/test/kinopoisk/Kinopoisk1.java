package kinopoisk;

import com.roman.base.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roman on 05.03.2017.
 */
@TestInfo(name = "КИНОПОИСК-1", description = "Вход на кинопоиск.")
public class Kinopoisk1 extends BaseTest {

    @Test
    public void test() {
        execute();
    }

    @StepInfo(name = "Переход по главной ссылке.")
    public void step1() {
        driver.go("https://www.kinopoisk.ru");
        driver.wait(() -> driver.getTitle().contains("КиноПоиск"))
                .orElse(() -> { throw new AssertionError("Ошибка перехода на главную страницу."); }).end();
        driver.report().pass("Успешно перешли на страницу.");
    }
}
