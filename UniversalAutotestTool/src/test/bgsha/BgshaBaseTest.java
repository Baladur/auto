package bgsha;


import com.roman.base.*;
import com.roman.bgsha.elements.Table;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by roman on 19.11.2016.
 */
@TestInfo(name = "БГША-БАЗОВЫЙ", description = "Базовые проверки.")
public class BgshaBaseTest extends BaseTest {

    @Test
    public void test() throws Throwable {
        execute();
    }

    @StepInfo(name = "Проверка содержимого таблицы.")
    public void step1() throws UniFrameworkException {
        driver.navigate().to("http://www.bgsha.ru/struktura/2012-02-10-07-15-25/%D0%B5%D0%B6%D0%B5%D0%B3%D0%BE%D0%B4%D0%BD%D1%8B%D0%B5-%D1%81%D0%BF%D0%BE%D1%80%D1%82%D0%B8%D0%B2%D0%BD%D1%8B%D0%B5-%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F/%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D1%8B-%D1%80%D0%B5%D0%B7%D1%83%D0%BB%D1%8C%D1%82%D0%B0%D1%82%D0%BE%D0%B2.html");
        assertThat(driver.table(Table.TURNIR).rowWhere().column("Команда").contains("ФВМ").rowAsMap())
                .describedAs("Неожиданные шашки и место.")
                .containsEntry("Шашки", "4")
                .containsEntry("Место", "6");
        String points = driver.table(Table.TURNIR).rowWhere().column("Команда").contains("Агроном")
                .cellByHeader("Шашки").asString();
        if (!Stream.of("I", "II", "III").anyMatch(s -> points.equals(s))) {
            assertThat(Integer.valueOf(points)).describedAs("Неправильный формат очков").isGreaterThan(3);
        }
    }
}

