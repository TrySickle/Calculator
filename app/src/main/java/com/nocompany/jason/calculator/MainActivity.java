package com.nocompany.jason.calculator;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import java.math.MathContext;
import java.util.Iterator;
import java.util.Stack;
import java.util.LinkedList;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    private static LinkedList<String> expression;
    private boolean pointed;
    private String ans;
    private int parAvailable;
    private DisplayFragment displayFragment;
    private boolean rad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.display_container, new DisplayFragment(), "Display");
        ft.add(R.id.button_container, new ButtonFragment(), "Button");
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
        displayFragment = (DisplayFragment)
                getSupportFragmentManager().findFragmentByTag("Display");
        expression = new LinkedList<>();
        ans = "0";
        rad = true;
        initialize();
    }

    /**
     * Initialization, also called when clearing the display.
     */
    private void initialize() {
        parAvailable = 0;
        pointed = false;
        expression.clear();
    }

    private void scroll() {
        displayFragment.getScroll().post(new Runnable() {
            public void run() {
                displayFragment.getScroll().fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    public void radClick() {
        rad = !rad;
    }

    /**
     * Handles the shift button which switches fragments in the display
     * Alternates between normal button fragment and shift fragment.
     */
    @Override
    public void shiftClick() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag("Button") != null) {
            ft.replace(R.id.button_container, new ShiftFragment());
        } else {
            ft.replace(R.id.button_container, new ButtonFragment(), "Button");
        }
        ft.commit();
    }

    /**
     * Handles the delete button and deletes either an entire operator or one
     * digit at a time.
     */
    @Override
    public void delClick() {
        if (!expression.isEmpty()) {
            String term = expression.getLast();
            if (isOperator(term)) {
                expression.removeLast();
                if (term.equals("(")) {
                    parAvailable--;
                } else if (term.equals(")")) {
                    parAvailable++;
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

    /**
     * Handles the AC button and initializes again, resetting display.
     */
    @Override
    public void acClick() {
        initialize();
        displayFragment.updateDisplay(expression);
    }

    /**
     * Handles the negate button which when used after an operator or in an
     * empty expression acts as a unary negative sign. When used on a number,
     * the number is negated.
     */
    @Override
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
        scroll();
    }

    /**
     * Handles the pi button, but not finished yet
     * @param view View containing the pi button
     */
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

    /**
     * Handles the parentheses and limits their usage, only allowing an equal
     * number of right and left parentheses
     * @param view View containing bracket
     */
    @Override
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
            if (!expression.isEmpty() && (expression.getLast().equals(")") ||
                    !isOperator(expression.getLast()))
                    && parAvailable > 0) {
                expression.add(par);
                parAvailable--;
            }
            break;
        default:
            break;
        }
        displayFragment.updateDisplay(expression);
        scroll();
    }

    /**
     * Handles the operators, sets rules for their placement
     * @param view View containing the operator
     */
    @Override
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
                    if (isPrefixOperator(operator)) {
                        expression.add("(");
                        parAvailable++;
                    }
                    pointed = false;
                }
            } else if (expression.getLast().equals(")")) {
                if (!operator.equals(".")) {
                    expression.add(operator);
                    pointed = false;
                    if (isPrefixOperator(operator)) {
                        expression.add("(");
                        parAvailable++;
                    }
                }
            } else { // last is an operator
                if (operator.equals(".")) {
                    expression.add("0.");
                    pointed = true;
                } else if (isPrefixOperator(operator)) {
                    expression.add(operator);
                    expression.add("(");
                    parAvailable++;
                    pointed = false;
                }
            }
        } else {
            if (operator.equals(".")) {
                expression.add("0.");
                pointed = true;
            } else if (isPrefixOperator(operator)) {
                expression.add(operator);
                expression.add("(");
                parAvailable++;
                pointed = false;
            }
        }
        displayFragment.updateDisplay(expression);
        scroll();
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
        scroll();
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
        case R.id.ln:
            term = "ln";
            break;
        case R.id.log:
            term = "log";
            break;
        case R.id.sin:
            term = "sin";
            break;
        case R.id.cos:
            term = "cos";
            break;
        case R.id.tan:
            term = "tan";
            break;
        case R.id.square_root:
            term = "\u221A";
            break;
        case R.id.arcsin:
            term = "sin\u207B\u00B9";
            break;
        case R.id.arccos:
            term = "cos\u207B\u00B9";
            break;
        case R.id.arctan:
            term = "tan\u207B\u00B9";
            break;
        case R.id.cube_root:
            term = "\u221B";
            break;
        default:
            term = "0";
        }
        return term;
    }

    private LinkedList<String> expand(LinkedList<String> exp) {
        LinkedList<String> expanded = new LinkedList<>();
        String last = "";
        for (String s : expression) {
            if (isPrefixOperator(s)) {
                if (!last.equals("") && (!isOperator(last) || last.equals(")"))) {
                    expanded.add("\u00D7");
                }
            }
            expanded.add(s);
            last = s;
        }
        return expanded;
    }

    public void equalClick() {
        for (String s : expression) {
            System.out.print(s + " ");
        }
        System.out.println();
        Stack<String> stack = new Stack<>();
        LinkedList<String> expanded = expand(expression);
        int size = expanded.size();
        for (int i = 0; i < size; i++) {
            String s = expanded.remove();
            if (isOperator(s)) {
                if (s.equals("(")) {
                    stack.push(s);
                } else if (s.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        expanded.add(stack.pop());
                    }
                    stack.pop();
                } else {
                    while (!stack.isEmpty()
                            && higherOrEqualPrecedence(stack.peek(), s)) {
                        expanded.add(stack.pop());
                    }
                    stack.push(s);
                }
            } else {
                expanded.add(s);
            }
        }

        while (!stack.isEmpty()) {
            expanded.add(stack.pop());
        }
        try {
            ans = evaluateExpression(expanded);
            expression.clear();
            expression.add(ans);
            displayFragment.updateDisplay(expression);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
    }

    private String evaluateExpression(LinkedList<String> exp) {
        for (String s : exp) {
            System.out.print(s + " ");
        }
        System.out.println();
        Stack<String> stack = new Stack<>();
        while (!exp.isEmpty()) {
            String s = exp.remove();
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
        return stack.pop();
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
                return sin(x);
            case "cos":
                return cos(x);
            case "tan":
                return tan(x);
            case "arcsin":
                return arcsin(x);
            case "arccos":
                return arccos(x);
            case "arctan":
                return arctan(x);
            case "\u221A":
                return squareRoot(x);
            case "\u221B":
                return cubeRoot(x);
            case "-":
                return negate(x);
            case "ln":
                return ln(x);
            case "log":
                return log(x);
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

    private String sin(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
            System.out.println("hi" + d);
        }
        BigDecimal k = new BigDecimal(Math.sin(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String cos(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.cos(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String tan(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.tan(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String arcsin(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.asin(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String arccos(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.acos(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String arctan(String x) {
        Double d = Double.parseDouble(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.atan(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String squareRoot(String x) {
        Double d = Double.parseDouble(x);
        BigDecimal k = new BigDecimal(Math.sqrt(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String cubeRoot(String x) {
        Double d = Double.parseDouble(x);
        BigDecimal k = new BigDecimal(Math.cbrt(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String log(String x) {
        Double d = Double.parseDouble(x);
        BigDecimal k = new BigDecimal(Math.log10(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
    }

    private String ln(String x) {
        Double d = Double.parseDouble(x);
        BigDecimal k = new BigDecimal(Math.log(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toString();
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
        BigDecimal k = j.divide(i, MathContext.DECIMAL64);
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
        case ".":
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
            return 4;
        case "-":
            return 3;
        case "\u00D7":
        case "\u00F7":
            return 2;
        case "+":
        case "\u2212":
            return 1;
        default:
            return 0;
        }
    }

    private boolean isPrefixOperator(String s) {
        switch (s) {
        case "(":
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
            return true;
        default:
            return false;
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
            return true;
        default:
            return false;
        }
    }
}
