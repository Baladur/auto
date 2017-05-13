package com.roman.base;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 05.03.2017.
 */
@Slf4j
public class Globals {
    private static Globals instance;
    private Map<String, Object> globalVariables = new HashMap<>();

    private static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    private static void checkExists(String variableName) throws UniFrameworkException {
        if (!getInstance().globalVariables.containsKey(variableName)) {
            throw new UniFrameworkException(String.format("Global variable '%s' is not defined!", variableName));
        }
    }

    public static void put(String variableName, Object variable) throws UniFrameworkException {
        log.info(String.format("Присваиваем глобальной переменной '%s' значение '%s'.",
                variableName, variable == null ? "null" : variable.toString()));
        getInstance().globalVariables.put(variableName, variable);
    }

    public static Object get(String variableName) throws UniFrameworkException {
        log.info(String.format("Получаем значение глобальной переменной '%s'.", variableName));
        checkExists(variableName);
        return getInstance().globalVariables.get(variableName);
    }

    public static boolean isTrue(String variableName) throws UniFrameworkException {
        log.info(String.format("Проверяем глобальный флаг '%s'.", variableName));
        if (!(getInstance().globalVariables.get(variableName) instanceof Boolean)) {
            throw new UniFrameworkException(String.format("Variable '%s' is not a boolean!", variableName));
        }
        return Boolean.class.cast(getInstance().globalVariables.getOrDefault(variableName, false));
    }

    public static void clear(String variableName) {
        log.info(String.format("Очищаем глобавльную переменную '%s'", variableName));
        getInstance().globalVariables.remove(variableName);
    }


}
