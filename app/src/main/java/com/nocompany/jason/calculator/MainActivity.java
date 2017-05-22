package com.nocompany.jason.calculator;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Stack;
import java.util.LinkedList;

import java.math.BigDecimal;

public class MainActivity extends Activity implements
        OnFragmentInteractionListener {

    private static LinkedList<String> expression;
    private static LinkedList<String> saveExpression;
    private boolean pointed;
    private String ans;
    private int parAvailable;
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
        ans = "0";
        initialize();
    }

    /**
     * Initialization, also called when clearing the display
     */
    private void initialize() {
        parAvailable = 0;
        pointed = false;
        expression.clear();
    }

    /**
     * Handles the shift button which switches fragments in the display
     * Alternates between normal button fragment and shift fragment
     */
    public void shiftClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentByTag("Button") != null) {
            ft.replace(R.id.button_container, new ShiftFragment());
        } else {
            ft.replace(R.id.button_container, new ButtonFragment(), "Button");
        }
        ft.commit();
    }

    /**
     * Handles the delete button and deletes either an entire operator or one
     * digit at a time
     */
    public void delClick() {
        if (!expression.isEmpty()) {
            String term = expression.getLast();
            if (isOperator(term)) {
                expression.removeLast();
                if (term.equals("(")) {
                    parAvailable--;
                }
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
        displayFragment.updateDisplay(expression);
    }

    public void negateClick() {
        if (!expression.isEmpty()) {
            if (!isOperator(expression.getLast())) {
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

            } else {
                if (!expression.getLast().equals("-")) {
                    expression.add("-");
                }
            }
        } else {
            expression.add("-");
        }
        displayFragment.updateDisplay(expression);
    }

    public void piClick(View view) {
        String pi = getTermString(view);
        if (expression.isEmpty()) {
            expression.add(pi);
            pointed = true;
        } else { // nonempty cases, term is a number
            if (isOperator(expression.getLast())) { // adding a new number term
                pointed = true;
                expression.add(pi);
            }
        }
        displayFragment.updateDisplay(expression);
    }

    public void bracketClick(View view) {
        String par = getTermString(view);
        switch (view.getId()) {
        case R.id.leftPar:
            if (expression.isEmpty()) {
                expression.add(par);
                parAvailable++;
            } else {
                expression.add(par);
                parAvailable++;
            }
            break;
        case R.id.rightPar:
            if (!expression.isEmpty() && !isOperator(expression.getLast())
                    && parAvailable > 0) {
                expression.add(par);
                parAvailable--;
            }
            break;
        default:
            break;
        }
        displayFragment.updateDisplay(expression);
    }

    public void operatorClick(View view) {
        String operator = getTermString(view);
        if (!expression.isEmpty()) {
            if (!isOperator(expression.getLast())) {
                if (operator.equals(".")) {
                    if (!pointed) {
                        expression.add(operator);
                        pointed = true;
                    }
                } else {
                    expression.add(operator);
                    pointed = false;
                }
            } else if (expression.getLast().equals(")")) {
                if (!operator.equals(".")) {
                    expression.add(operator);
                }
            } else {
                if (operator.equals(".")) {
                    expression.add("0.");
                    pointed = true;
                }
            }
        } else if (operator.equals(".")) {
            expression.add("0.");
            pointed = true;
        }
        displayFragment.updateDisplay(expression);
    }

    public void numberClick(View view) {
        String number = getTermString(view);
        if (expression.isEmpty()) {
            expression.add(number);
            pointed = false;
        } else { // nonempty cases, term is a number
            if (isOperator(expression.getLast())) { // adding a new number term
                if (expression.getLast().equals("-")) {
                    expression.add(expression.removeLast() + number);
                } else {
                    pointed = expression.getLast().equals(".");
                    expression.add(number);
                }
            } else { // increasing number
                expression.add(expression.removeLast() + number);
            }
        }
        displayFragment.updateDisplay(expression);
    }
    
    private String getTermString(View view) {
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
        case R.id.leftPar:
            term = "(";
            break;
        case R.id.rightPar:
            term = ")";
            break;
        case R.id.pi:
            term = "\u03C0";
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
        boolean addMultiply = false;
        saveExpression = (LinkedList<String>) expression.clone();
        Stack<String> stack = new Stack<>();
        int size = expression.size();
        String last = "";
        for (int i = 0; i < size; i++) {
            String s = expression.remove();
            if (isOperator(s)) {
                if (s.equals("(")) {
                    if (!last.equals("") && (!isOperator(last) || last.equals(")"))) {
                        addMultiply = true;
                    }
                    stack.push(s);
                } else if (s.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        expression.add(stack.pop());
                    }
                    stack.pop();
                    if (addMultiply) {
                        while (!stack.isEmpty()
                                && higherOrEqualPrecedence(stack.peek(), "\u00D7")) {
                            expression.add(stack.pop());
                        }
                        expression.add("\u00D7");
                    }
                } else {
                    while (!stack.isEmpty()
                            && higherOrEqualPrecedence(stack.peek(), s)) {
                        expression.add(stack.pop());
                    }
                    stack.push(s);
                }
            } else {
                expression.add(s);
            }
            last = s;
        }

        while (!stack.isEmpty()) {
            expression.add(stack.pop());
        }
        evaluateExpression();
        displayFragment.updateDisplay(expression);
    }

    private void evaluateExpression() {
        Stack<String> stack = new Stack<>();
        try {
            while (!expression.isEmpty()) {
                String s = expression.remove();
                if (isOperator(s)) {
                    if (isBinary(s)) {
                        String top1 = stack.pop();
                        String top2 = stack.pop();
                        stack.push(operate(top1, top2, s));
                    } else {
                        String top = stack.pop();
                        stack.push(operate(top, s));
                    }
                } else {
                    stack.add(s);
                }
            }
            ans = stack.pop();
            expression.add(ans);
        } catch (Exception e) {
            expression = (LinkedList<String>) saveExpression.clone();
            displayFragment.updateDisplay(expression);
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
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

    private String operate(String x, String o) {
        switch (o) {
            case "sin":
                return Double.toString(Math.sin(Double.parseDouble(x)));
            case "cos":
                return Double.toString(Math.cos(Double.parseDouble(x)));
            case "tan":
                return Double.toString(Math.tan(Double.parseDouble(x)));
            case "-":
                return negate(x);
            default:
                return "0";
        }
    }

    private boolean isBinary(String o) {
        switch (o) {
            case "\u00D7":
            case "\u00F7":
            case "+":
            case "\u2212":
            case ".":
            case "\u207F":
            case "\u00B2":
                return true;
            case "-":
            case "log":
            case "ln":
            case "\u221A":
            case "sin":
            case "cos":
            case "tan":
            case "sin\u207B\u00B9":
            case "cos\u207B\u00B9":
            case "tan\u207B\u00B9":
            case "\u221B":
            case "\u0021":
                return false;
            default:
                return false;
        }
    }

    private String negate(String x) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal k = i.negate();
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String multiply(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = i.multiply(j);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String divide(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = j.divide(i, java.math.MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String add(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = i.add(j);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String point(String x, String y) {
        BigDecimal i = new BigDecimal(y);
        BigDecimal j = new BigDecimal("0." + x);
        if (i.compareTo(BigDecimal.ZERO) < 0) {
            j = j.negate();
        }
        BigDecimal k = i.add(j);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String subtract(String x, String y) {
        BigDecimal i = new BigDecimal(x);
        BigDecimal j = new BigDecimal(y);
        BigDecimal k = j.subtract(i);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private boolean higherOrEqualPrecedence(String x, String y) {
        return getPrecedence(x) - getPrecedence(y) >= 0;
    }

    private int getPrecedence(String x) {
        switch (x) {
        case "-":
            return 3;
        case ".":
            return 3;
        case "\u00D7":
            return 2;
        case "\u00F7":
            return 2;
        case "+":
            return 1;
        case "\u2212":
            return 1;
        default:
            return 0;
        }
    }

    private boolean isOperator(String s) {
        switch (s) {
        case "\u00D7":
        case "\u00F7":
        case "+":
        case "\u2212":
        case ".":
        case "(":
        case ")":
        case "-":
            return true;
        default:
            return false;
        }
    }
}
