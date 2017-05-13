package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public enum Match {
    Contain, Equal, StartWith, EndWith;

    public boolean compare(String value1, String value2) {
        switch (this) {
            case Contain: return value1.contains(value2);
            case Equal: return value1.equals(value2);
            case StartWith: return value1.startsWith(value2);
            case EndWith: return value1.endsWith(value2);
        }
        return false;
    }
}
