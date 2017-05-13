package com.roman.bgsha.elements;

import com.roman.base.SelectElement;

/**
 * Created by roman on 22.02.2017.
 */
public enum Select implements SelectElement {
    AFISHA_SELECT, RATINGS_SELECT;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getParentPath() {
        return null;
    }

    @Override
    public String getChildrenPath() {
        return null;
    }
}
