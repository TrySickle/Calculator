package com.nocompany.jason.calculator;

/**
 * Created by jason on 3/26/2017.
 */

public class Term {
    private String display;
    private Number value;

    public Term() {
        this(0L);
    }

    public Term(Number n) {
        setValue(n);
        setDisplay(n);
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number n) {
        value = n;
        setDisplay(value);
    }

    public void multiplyValue(Number n) {
        if (value instanceof Long) {
            value = value.longValue() * n.longValue();
        } else if (value instanceof Double) {
            value = value.doubleValue() * n.doubleValue();
        }
        setDisplay(value);
    }

    public void addValue(Number n) {
        if (value instanceof Long) {
            value = value.longValue() + n.longValue();
        } else if (value instanceof Double) {
            value = value.doubleValue() + n.doubleValue();
        }
        setDisplay(value);
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(Number n) {
        display = n.toString();
    }
}
