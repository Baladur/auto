package com.roman.base;

import java.util.concurrent.TimeUnit;

/**
 * Created by roman on 01.03.2017.
 */
public class LogMessages {
    /**
     * Transforms time unit to string
     *
     * @param timeUnit
     * @return
     */
    public static String timeUnitToString(TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS: return "сек";
            case MINUTES: return "мин";
            case HOURS: return "ч";
        }
        throw new IllegalArgumentException("Unexpected time unit!");
    }
    /**
     * Select
     *
     * @param action
     * @return
     */
    public static String selectAction(SelectAction action) {
        String message = String.format("Выбираем из селекта '%s' опцию", action.getElement().getName());
        if (action.getOptionIndex() < 0) {
            String matching = "";
            switch (action.getOptionMatch()) {
                case Equal: matching = ", равную строке "; break;
                case Contain: matching = ", содержащую строку "; break;
                case StartWith: matching = ", начинающуюся со строки "; break;
                case EndWith: matching = ", заканчивающуюся строкой "; break;
            }
            message += matching + "'%s'.";
            message = String.format(message, action.getOptionValue());
        } else {
            message += String.format("с индексом %d.", action.getOptionIndex());
        }
        return message;
    }

    /**
     * Fill
     *
     * @param action
     * @return
     */
    public static String fillAction(FillAction action) {
        String message = String.format("Заполняем текстовое поле '%s' значением '%s'.",
                action.getElement().getName(), action.getText(), action.getTime(), timeUnitToString(action.getTimeUnit()));
        return message;
    }

    /**
     * Click
     *
     * @param action
     * @return
     */
    public static String clickAction(ClickAction action) {
        String message = String.format("Нажимаем на элемент '%s'.",
                action.getElement().getName());
        return message;
    }

    /**
     * Fail select with index
     *
     * @param action
     * @return
     */
    public static String failSelectActionWithIndex(SelectAction action) {
        return String.format("Не удалось найти %d-ую опцию выпадающего списка '%s' в течение %d %s.",
                action.getOptionIndex() + 1, action.getElement().getName(),
                action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * Fail select with option value
     *
     * @param action
     * @return
     */
    public static String failSelectActionWithOptionValue(SelectAction action) {
        return String.format("Не удалось найти опцию с текстом '%s' выпадающего списка '%s' в течение %d %s.",
                action.getOptionValue(), action.getElement().getName(),
                action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * When select element can't be clicked
     *
     * @param action
     * @return
     */
    public static String failSelectActionMain(SelectAction action) {
        return String.format("Не удалось нажать на выпадающий список '%s' в течение %d %s.",
                action.getElement().getName(), action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * When select options can't be found
     *
     * @param action
     * @return
     */
    public static String failSelectActionOptions(SelectAction action) {
        return String.format("Не удалось найти опции выпадающего списка '%s' в течение %d %s.",
                action.getElement().getName(), action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * Fail fill action
     *
     * @param action
     * @return
     */
    public static String failFillAction(FillAction action) {
        return String.format("Не удалось заполнить текстовое поле '%s' текстом '%s' в течение %d %s.",
                action.getElement().getName(), action.getText(),
                action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * Fail click action
     *
     * @param action
     * @return
     */
    public static String failClickAction(ClickAction action) {
        return String.format("Не удалось нажать на элемент '%s' в течение %d %s.",
                action.getElement().getName(), action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * When element is not found
     *
     * @param element
     * @return
     */
    public static String elementNotFound(BaseElement element) {
        return String.format("Элемент '%s' не найден.", element.getName());
    }

    /**
     * When element with text is not found
     *
     * @param element
     * @param text
     * @return
     */
    public static String elementWithTextNotFound(BaseElement element, String text) {
        return String.format("Элемент '%s' с текстом '%s' не найден.", element.getName(), text);
    }

    /**
     * Default message for action failure.
     *
     * @param action
     * @return
     */
    public static String failAbstractAction(AbstractAction action) {
        return String.format("Не удалось выполнить действие за %d %s.", action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * When condition does not become true after time defined in {@code WaitAction}.
     *
     * @return
     */
    public static String failWaitAction(WaitAction action) {
        return String.format("Не удалось дождаться выполнения условия за %d %s.", action.getTime(), timeUnitToString(action.getTimeUnit()));
    }

    /**
     * When we navigate to web page url.
     *
     * @param action
     * @return
     */
    public static String goAction(GoAction action) {
        return String.format("Переходим по адресу '%s'.", action.getUrl());
    }

    /**
     * When element is not found by css selector.
     *
     * @param cssSelector
     * @return
     */
    public static String failFindByCss(String cssSelector) {
        return String.format("Не удалось найти элемент по css-селектору '%s'.", cssSelector);
    }

    /**
     * When element is not found by xpath.
     *
     * @param xpath
     * @return
     */
    public static String failFindByXPath(String xpath) {
        return String.format("Не удалось найти элемент по xpath '%s'.", xpath);
    }
}
