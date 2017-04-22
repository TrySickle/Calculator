package com.nocompany.jason.calculator;

/**
 * Created by jason on 4/21/2017.
 */

public class Operand extends Term {
    private Number value;

    public Operand() {
        this(0L);
    }

    public Operand(Number n) {
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

    public void setDisplay(Number n) {
        setDisplay(n.toString());
    }
}
