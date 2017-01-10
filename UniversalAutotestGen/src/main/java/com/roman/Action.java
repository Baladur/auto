package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public enum Action {
    click("click(%s.%s);\n"),
    set("textfield(%s.%s).setText(\"%s\");\n"),
    go("navigate().to(\"%s\");\n"),
    wait("wait(Time.%s, %s);\n")
    ;

    Action(String text) {
        this.text = text;
    }

    public String getText() { return text; }

    private String text;
}
