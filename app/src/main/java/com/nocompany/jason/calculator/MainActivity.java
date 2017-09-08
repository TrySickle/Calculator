package com.nocompany.jason.calculator;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.Stack;
import java.util.LinkedList;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    private static LinkedList<String> expression;
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
        expression.clear();
    }

    private void scrollRight() {
        displayFragment.getScroll().post(new Runnable() {
            public void run() {
                displayFragment.getScroll().fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    private void scrollLeft() {
        displayFragment.getScroll().post(new Runnable() {
            public void run() {
                displayFragment.getScroll().fullScroll(HorizontalScrollView.FOCUS_LEFT);
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
            String last = expression.removeLast();
            if (!isOperator(last)) {
                expression.add("-" + last);
            } else {
                if (!expression.getLast().equals("-")) {
                    expression.add(last);
                    expression.add("-");
                }
            }
        } else {
            expression.add("-");
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
    }

    /**
     * Handles the pi button, but not finished yet
     */
    public void piClick() {
        String pi = "\u03C0";
        if (expression.isEmpty()) {
            expression.add(pi);
        } else { // nonempty cases, term is a number
            if (expression.getLast().lastIndexOf(".") != expression.getLast().length() - 1) {
                expression.add(pi);
            }
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
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
        scrollRight();
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
                expression.add(operator);
                if (view.getId() == R.id.squared) {
                    expression.add("2");
                }
                if (isPrefixOperator(operator)) {
                    expression.add("(");
                    parAvailable++;
                }
            } else if (expression.getLast().equals(")")) {
                expression.add(operator);
                if (view.getId() == R.id.squared) {
                    expression.add("2");
                }
                if (isPrefixOperator(operator)) {
                    expression.add("(");
                    parAvailable++;
                }
            } else { // last is an operator
                if (isPrefixOperator(operator)) {
                    expression.add(operator);
                    expression.add("(");
                    parAvailable++;
                }
            }
        } else {
            if (isPrefixOperator(operator)) {
                expression.add(operator);
                expression.add("(");
                parAvailable++;
            }
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
    }

    public void tenExponent() {
        if (!expression.isEmpty()) {
            if (!isOperator(expression.getLast())) {
                expression.add("\u00D7");
                expression.add("10");
                expression.add("^");
            } else { // last is an operator
                expression.add("10");
                expression.add("^");
            }
        } else {
            expression.add("10");
            expression.add("^");
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
    }

    public void eExponent() {
        if (!expression.isEmpty()) {
            if (!isOperator(expression.getLast())) {
                expression.add("\u00D7");
                expression.add("\u212F");
                expression.add("^");
            } else { // last is an operator
                expression.add("\u212F");
                expression.add("^");
            }
        } else {
            expression.add("\u212F");
            expression.add("^");
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
    }

    public void pointClick() {
        if (expression.isEmpty()) {
            expression.add("0.");
        } else {
            if (isOperator(expression.getLast())) { // adding a new number term
                if (expression.getLast().equals("-")) {
                    expression.add(expression.removeLast() + "0.");
                } else {
                    expression.add("0.");
                }
            } else { // increasing number
                String last = expression.removeLast();
                if (!last.contains(".")) {
                    expression.add(last + ".");
                }
            }
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
    }

    public void numberClick(View view) {
        String number = getTermString(view);
        if (expression.isEmpty()) {
            expression.add(number);
        } else { // nonempty cases, term is a number
            if (isOperator(expression.getLast())) { // adding a new number term
                if (expression.getLast().equals("-")) {
                    expression.add(expression.removeLast() + number);
                } else {
                    expression.add(number);
                }
            } else { // increasing number
                expression.add(expression.removeLast() + number);
            }
        }
        displayFragment.updateDisplay(expression);
        scrollRight();
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
        case R.id.squared:
        case R.id.exponent:
            term = "^";
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
        case R.id.factorial:
            term = "\u0021";
            break;
        case R.id.combination:
            term = "C";
            break;
        case R.id.permutation:
            term = "P";
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
            if (isPrefixOperator(s) || s.equals("\u03C0")) {
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
            scrollLeft();
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
        case "^":
            return exponent(x, y);
        case "C":
            return combination(x, y);
        case "P":
            return permutation(x, y);
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
            case "\u0021":
                return factorial(x);
            default:
                return "0";
        }
    }

    private Double getValue(String x) {
        switch (x) {
            case "\u03C0":
                return Math.PI;
            case "\u212F":
                return Math.E;
            default:
                return Double.parseDouble(x);
        }
    }

    private String factorial(String x) {
        if (x.contains(".")) {
            throw new IllegalArgumentException("Cannot have fractional");
        }
        int i = Integer.valueOf(x);
        if (i < 0) {
            throw new IllegalArgumentException("Cannot have negative factorial");
        }
        if (i < 2) {
            return "1";
        } else {
            return recFact(1, i).toString();
        }
    }

    private BigInteger recFact(long start, long n) {
        long i;
        if (n <= 16) {
            BigInteger r = BigInteger.valueOf(start);
            for (i = start + 1; i < start + n; i++) {
                r = r.multiply(BigInteger.valueOf(i));
            }
            return r;
        }
        i = n / 2;
        return recFact(start, i).multiply(recFact(start + i, n - i));
    }

    private String combination(String x, String y) {
        if (x.contains(".") || y.contains(".")) {
            throw new IllegalArgumentException("Cannot have fractional");
        }
        int n = Integer.valueOf(y);
        int r = Integer.valueOf(x);
        if (n < 0 || r < 0) {
            throw new IllegalArgumentException("Cannot have negatives");
        }
        BigInteger numerator = new BigInteger(factorial(y));
        BigInteger denominator = new BigInteger(factorial(x)).multiply(new BigInteger(factorial(Integer.toString((Integer.valueOf(y) - Integer.valueOf(x))))));
        return numerator.divide(denominator).toString();
    }

    private String permutation(String x, String y) {
        System.out.println("x=" + x + "y=" + y);
        if (x.contains(".") || y.contains(".")) {
            throw new IllegalArgumentException("Cannot have fractional");
        }
        int n = Integer.valueOf(y);
        int r = Integer.valueOf(x);
        if (n < 0 || r < 0) {
            throw new IllegalArgumentException("Cannot have negatives");
        }
        BigInteger numerator = new BigInteger(factorial(y));
        BigInteger denominator = new BigInteger(factorial(Integer.toString((Integer.valueOf(y) - Integer.valueOf(x)))));
        return numerator.divide(denominator).toString();
    }

    private String sin(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.sin(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String cos(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.cos(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String tan(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.tan(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String arcsin(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.asin(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String arccos(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.acos(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String arctan(String x) {
        Double d = getValue(x);
        if (!rad) {
            d = Math.toRadians(d);
        }
        BigDecimal k = new BigDecimal(Math.atan(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String squareRoot(String x) {
        Double d = getValue(x);
        BigDecimal k = new BigDecimal(Math.sqrt(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String cubeRoot(String x) {
        Double d = getValue(x);
        BigDecimal k = new BigDecimal(Math.cbrt(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String log(String x) {
        Double d = getValue(x);
        BigDecimal k = new BigDecimal(Math.log10(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String ln(String x) {
        Double d = getValue(x);
        BigDecimal k = new BigDecimal(Math.log(d), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String negate(String x) {
        BigDecimal i = new BigDecimal(getValue(x).toString());
        BigDecimal k = i.negate();
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String multiply(String x, String y) {
        BigDecimal i = new BigDecimal(getValue(x).toString());
        BigDecimal j = new BigDecimal(getValue(y).toString());
        BigDecimal k = i.multiply(j);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String divide(String x, String y) {
        BigDecimal i = new BigDecimal(getValue(x).toString());
        BigDecimal j = new BigDecimal(getValue(y).toString());
        BigDecimal k = j.divide(i, MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String add(String x, String y) {
        BigDecimal i = new BigDecimal(getValue(x).toString());
        BigDecimal j = new BigDecimal(getValue(y).toString());
        BigDecimal k = i.add(j);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String exponent(String x, String y) {
        double i = getValue(x);
        double j = getValue(y);
        BigDecimal k = new BigDecimal(Math.pow(j, i), MathContext.DECIMAL64);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private String subtract(String x, String y) {
        BigDecimal i = new BigDecimal(getValue(x).toString());
        BigDecimal j = new BigDecimal(getValue(y).toString());
        BigDecimal k = j.subtract(i);
        k = k.stripTrailingZeros();
        return k.toPlainString();
    }

    private boolean higherOrEqualPrecedence(String x, String y) {
        return getPrecedence(x) - getPrecedence(y) >= 0;
    }

    private int getPrecedence(String x) {
        switch (x) {
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
        case "^":
        case "\u0021":
        case "C":
        case "P":
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

    private boolean isBinary(String o) {
        switch (o) {
            case "\u00D7":
            case "\u00F7":
            case "+":
            case "\u2212":
            case "\u207F":
            case "\u00B2":
            case "^":
            case "C":
            case "P":
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
        case "^":
        case "\u0021":
        case "C":
        case "P":
            return true;
        default:
            return false;
        }
    }
}
