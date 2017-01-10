package kinopoisk;

import com.roman.base.Time;
import com.roman.base.UniDriver;
import com.roman.kinopoisk.elements.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created by roman on 19.11.2016.
 */
public class KinopoiskBaseTest extends TestCase {
    private UniDriver driver;
    private final String SHORT_USERNAME = "aw";
    private final String VALID_USERNAME = "awef";
    private final String PASSWORD = "1";
    private final String INVALID_EMAIL = "1";
    private final String VALID_EMAIL = "waef@gmail.com";
    private final String RANDOM_CAPTCHA = "awef";

    private final String CORRECT_FILM = "Большой куш";
    private final String INCORRECT_FILM = "aewfiuaewhfiuhiiuoawefh";

    public void setUp() {
        driver = new UniDriver(UniDriver.TYPE.CHROME);
        driver.manage().window().maximize();

    }

//    @Test
//    public void testRegistration() {
//        driver.navigate().to("https://www.kinopoisk.ru");
//        //Нажимаем на вкладку "общение"
//        driver.click(Button.COMMUNICATION_TAB);
//
//        //Нажимаем на ссылку "Форум"
//        driver.click(Link.FORUM);
//
//        //Нажимаем на ссылку "Регистрация"
//        driver.click(Link.REGISTER);
//
//        //Соглашаемся с условиями и нажимаем "Регистрация"
//        driver.click(CheckBox.AGREE_WITH_RULES);
//        driver.click(Button.REGISTRATION);
//
//        //Вводим имя пользователя менее 3 символов
//        driver.textfield(TextField.REG_USERNAME).setText(SHORT_USERNAME);
//        driver.wait(Time.SECONDS, 2);
//        String errorMsg = driver.find(Label.VERIFY_MESSAGE).getText();
//        Assert.assertEquals(errorMsg, "Ваше имя пользователя не может быть короче 3 символов.");
//
//        //Вводим корректное имя пользователя
//        driver.textfield(TextField.REG_USERNAME).setText(VALID_USERNAME);
//        driver.wait(Time.SECONDS, 2);
//        String msg = driver.find(Label.VERIFY_MESSAGE).getText();
//        Assert.assertEquals(true,
//                msg.equals("Имя пользователя корректно и не занято.") || msg.startsWith("Это имя пользователя уже кем-то занято"));
//
//        //Вводим пароль, некорректный email, нажимаем "Регистрация"
//        driver.textfield(TextField.PASSWORD).setText(PASSWORD);
//        driver.textfield(TextField.PASSWORD_CONFIRM).setText(PASSWORD);
//        driver.textfield(TextField.EMAIL).setText(INVALID_EMAIL);
//        driver.textfield(TextField.EMAIL_CONFIRM).setText(INVALID_EMAIL);
//
//        driver.textfield(TextField.CAPTCHA).setText(RANDOM_CAPTCHA);
//        driver.click(Button.SUBMIT_REGISTRATION);
//
//        String firstRegFailure = driver.find(ListItem.REG_FAILURE, 1).getText();
//        String secondRegFailure = driver.find(ListItem.REG_FAILURE, 2).getText();
//        Assert.assertEquals(firstRegFailure, "Вы ввели неправильный адрес электронной почты.");
//        Assert.assertEquals(secondRegFailure, "Введённая вами строка не совпадает с той, которая была указана на проверочном изображении.");
//
//        //Вводим пароль, корректный email, нажимаем "Регистрация"
//        driver.textfield(TextField.PASSWORD).setText(PASSWORD);
//        driver.textfield(TextField.PASSWORD_CONFIRM).setText(PASSWORD);
//        driver.textfield(TextField.EMAIL).setText(VALID_EMAIL);
//        driver.textfield(TextField.EMAIL_CONFIRM).setText(VALID_EMAIL);
//
//        driver.textfield(TextField.CAPTCHA).setText(RANDOM_CAPTCHA);
//        driver.click(Button.SUBMIT_REGISTRATION);
//
//        String regFailure = driver.find(ListItem.REG_FAILURE, 1).getText();
//        Assert.assertEquals(regFailure, "Введённая вами строка не совпадает с той, которая была указана на проверочном изображении.");
//    }
//
//    @Test
//    public void testSearch() {
//        driver.navigate().to("https://www.kinopoisk.ru");
//        //Вводим в поле поиска корректное название фильма
//        driver.textfield(TextField.SEARCH_INPUT).setText(CORRECT_FILM);
//
//        //Переходим на результат поиска
//        driver.click(Button.SEARCH_BTN);
//
//        //Сравниваем введенное название с фактическим
//        String actualName = driver.find(Link.MOST_WANTED_RESULT).getText();
//        Assert.assertEquals(actualName, CORRECT_FILM);
//
//        //Вводим в поле поиска некорректное название фильма
//        driver.textfield(TextField.SEARCH_INPUT).setText(INCORRECT_FILM);
//
//
//        //Проверяем что результатов нет
//        driver.click(Button.SEARCH_BTN);
//        driver.wait(Time.SECONDS, 2);
//        Assert.assertEquals(true, driver.isElementVisible(Label.NO_RESULTS));
//    }

    @Test
    public void testVoting() {
        driver.navigate().to("http://www.poll.ru");

    }

    public void tearDown() {
        driver.baseDriver().quit();
    }
}
