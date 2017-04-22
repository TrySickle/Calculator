package com.nocompany.jason.calculator;

/**
 * Created by jason on 3/26/2017.
 */

public abstract class Term {
    private String display;

    public Term() {
    }

    public Term(String display) {
        this.display = display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public String toString() {
        return display;
    }
}
