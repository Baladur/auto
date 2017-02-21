package com.roman.base;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by roman on 21.02.2017.
 */
public class SelectAction implements Action {
    private UniDriver driver;
    private SelectElement element;
    private Match match;
    private String optionValue;

    public SelectAction(UniDriver driver) {
        this.driver = driver;
    }

    @Override
    public void execute() {
        driver.click(element);
        List<WebElement> options = driver.findOptions(element);
        options
                .stream()
                .filter(option -> {
                    switch (match) {
                        case Contain: return option.getText().contains(optionValue);
                        case Equal: return option.getText().equals(optionValue);
                        case StartWith: return option.getText().startsWith(optionValue);
                        case EndWith: return option.getText().endsWith(optionValue);
                        default: return false;
                    }
                })
                .findFirst()
                .ifPresent(WebElement::click);
    }

    public SelectAction element(SelectElement element) {
        this.element = element;
        return this;
    }

    public SelectAction match(Match match) {
        this.match = match;
        return this;
    }

    public SelectAction optionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }
}
