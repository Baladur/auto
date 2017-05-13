package com.roman.base;

import lombok.Getter;

/**
 * Created by roman on 10.03.2017.
 */
@Getter
public class ElementAction extends AbstractAction {
    protected BaseElement element;
    protected int elementIndex;

    public ElementAction(Driver driver, BaseElement element) {
        super(driver);
        this.element = element;
        this.elementIndex = 1;
    }

    public ElementAction(Driver driver, BaseElement element, int elementIndex) {
        super(driver);
        this.element = element;
        this.elementIndex = elementIndex;
    }
}
