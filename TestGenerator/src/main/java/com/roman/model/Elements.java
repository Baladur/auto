package com.roman.model;

import java.util.List;

public class Elements {
        public List<Element> Button;
        public List<Element> TextField;
        public List<Element> Select;
        public List<Element> Table;
        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("Elements:\n");
            result.append(Button.toString());
            result.append(TextField.toString());
            result.append(Select.toString());
            result.append(Table.toString());
            return result.toString();
        }
    }