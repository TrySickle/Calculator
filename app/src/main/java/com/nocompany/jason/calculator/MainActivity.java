package com.nocompany.jason.calculator;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import java.util.Iterator;
import java.util.Stack;
import java.util.LinkedList;

import java.math.BigDecimal;

public class MainActivity extends Activity implements
        OnFragmentInteractionListener {

    private static LinkedList<String> expression;
    private boolean pointed;
    private String ans;
    private DisplayFragment displayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.display_container, new DisplayFragment(), "Display");
        ft.add(R.id.button_container, new ButtonFragment(), "Button");
        ft.commit();
        getFragmentManager().executePendingTransactions();
        displayFragment = (DisplayFragment)
                getFragmentManager().findFragmentByTag("Display");
        expression = new LinkedList<>();
        pointed = false;
        ans = "0";
    }

    private void initialize() {
        pointed = false;
        expression.clear();
        displayFragment.updateDisplay(expression);
    }

    public void shiftClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentByTag("Button") != null) {
            ft.replace(R.id.button_container, new ShiftFragment());
        } else {
            ft.replace(R.id.button_container, new ButtonFragment(), "Button");
        }
        ft.commit();
    }

    public void delClick() {
        if (!expression.isEmpty()) {
            String term = expression.getLast();
            if (isOperator(term)) {
                expression.removeLast();
            } else {
                if (term.length() == 1) {
                    expression.removeLast();
                } else {
                    expression.add(
                            expression.removeLast().
                            substring(0, term.length() - 1));
                }
            }
            displayFragment.updateDisplay(expression);
        }
    }

    public void acClick() {
        initialize();
    }

    public void negateClick() {
        if (!expression.isEmpty() && !isOperator(expression.getLast())) {
            if (pointed) {
                Iterator<String> reverseIter = expression.descendingIterator();
                reverseIter.next();
                reverseIter.next();
                String beforeDecimal = reverseIter.next();
                BigDecimal before = new BigDecimal(beforeDecimal);
                BigDecimal negated = before.negate();
                expression.set(expression.size() - 3, negated.toString());
            } else {
                BigDecimal last = new BigDecimal(expression.getLast());
                BigDecimal negated = last.negate();
                expression.set(expression.size() - 1, negated.toString());
            }
            displayFragment.updateDisplay(expression);
        }
    }

    public void termClick(View view) {
        String term = getTerm(view);
        if (expression.isEmpty()) { // empty cases
            if (!isOperator(term)) {
                expression.add(term);
                pointed = false;
            }
        } else if (!isOperator(term)) { // nonempty cases, term is a number
            if (isOperator(expression.getLast())) { // adding a new number term
                pointed = expression.getLast().equals(".");
                expression.add(term);
            } else { // increasing number
                expression.add(expression.removeLast() + getTerm(view));
            }
        } else { // nonempty, term is an operator
            // can't have two operators in a row
            if (!isOperator(expression.getLast())) {
                if (term.equals(".")) {
                    if (!pointed) {
                        expression.add(term);
                        pointed = true;
                    }
                } else {
                    expression.add(term);
                    pointed = false;
                }
            }
        }
        displayFragment.updateDisplay(expression);
    }
    
    private String getTerm(View view) {
        String term;
        switch (view.getId()) {
        case R.id.zero:
            term = "0";
            break;
        case R.id.one:
            term = "1";
            break;
        case R.id.two:
            term = "2";
            break;
        case R.id.three:
            term = "3";
            break;
        case R.id.four:
            term = "4";
            break;
        case R.id.five:
            term = "5";
            break;
        case R.id.six:
            term = "6";
            break;
        case R.id.seven:
            term = "7";
            break;
        case R.id.eight:
            term = "8";
            break;
        case R.id.nine:
            term = "9";
            break;
        case R.id.point:
            term = ".";
            break;
        case R.id.ans:
            term = ans;
            break;
        case R.id.multiply:
            term = "\u00D7";
            break;
        case R.id.divide:
            term = "\u00F7";
            break;
        case R.id.add:
            term = "+";
            break;
        case R.id.subtract:
            term = "\u2212";
            break;
        default:
            term = "0";
        }
        return term;
    }

    public void equalClick() {
        for (String s : expression) {
            System.out.print(s + " ");
        }
        System.out.println();
        Stack<String> stack = new Stack<>();
        int size = expression.size();
        for (int i = 0; i < size; i++) {
            String s = expression.remove();
            if (isOperator(s)) {
                while (!stack.isEmpty()
                        && higherOrEqualPrecedence(stack.peek(), s)) {
                    expression.add(stack.pop());
                }
                stack.push(s);
            } else {
                expression.add(s);
            }
        }

        while (!stack.isEmpty()) {
            expression.add(stack.pop());
        }
        evaluateExpression();
        displayFragment.updateDisplay(expression);
    }

    private void evaluateExpression() {
        Stack<String> stack = new Stack<>();
        while (!expression.isEmpty()) {
            String s = expression.remove();
            if (isOperator(s)) {
                String top1 = stack.pop();
                String top2 = stack.pop();
                stack.push(operate(top1, top2, s));
            } else {
                stack.add(s);
            }
        }
        ans = stack.pop();
        expression.add(ans);
    }

    private String operate(String x, String y, String o) {
        switch (o) {
        case "\u00D7":
            return multiply(x, y);
        case "\u00F7":
            return divide(x, y);
        case "+":
            return add(x, y);
        case "\u2212":
            return subtract(x, y);
        case ".":
            return point(x, y);
        default:
            return "0";
        }
    }

    private String multiply(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = i.multiply(j);
        return k.toString();
    }

    private String divide(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = j.divide(i, java.math.MathContext.DECIMAL64);
        return k.toString();
    }

    private String add(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = i.add(j);
        return k.toString();
    }

    private String point(String x, String y) {
        BigDecimal i = new BigDecimal(y);
        BigDecimal j = new BigDecimal("0." + x);
        if (i.compareTo(BigDecimal.ZERO) < 0) {
            j = j.negate();
        }
        BigDecimal k = i.add(j);
        return k.toString();
    }

    private String subtract(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = j.subtract(i);
        return k.toString();
    }

    private boolean isInt(String x) {
        double d = Double.valueOf(x);
        return d == (int) d;
    }

    private boolean isInt(double x) {
        return x == (int) x;
    }

    private boolean higherOrEqualPrecedence(String x, String y) {
        return getPrecedence(x) - getPrecedence(y) >= 0;
    }

    private int getPrecedence(String x) {
        switch (x) {
        case ".":
            return 2;
        case "\u00D7":
            return 1;
        case "\u00F7":
            return 1;
        case "+":
            return 0;
        case "\u2212":
            return 0;
        default:
            return 0;
        }
    }

    private boolean isOperator(String s) {
        switch (s) {
        case "\u00D7":
            return true;
        case "\u00F7":
            return true;
        case "+":
            return true;
        case "\u2212":
            return true;
        case ".":
            return true;
        case "(":
            return true;
        case ")":
            return true;
        default:
            return false;
        }
    }
}
