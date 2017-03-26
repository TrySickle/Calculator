package com.nocompany.jason.calculator;

/**
 * Created by jason on 3/18/2017.
 */

public abstract class Operator {
    private String display; // what to display on screen

    // Overloaded operate method, performs the function of the operator, can be unary or binary
    public abstract Number operate(Number x);
}
