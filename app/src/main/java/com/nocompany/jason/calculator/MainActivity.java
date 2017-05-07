package com.nocompany.jason.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.Stack;
import java.util.LinkedList;

public class MainActivity extends Activity {

    private static LinkedList<String> expression;
    private TextView display;
    private HorizontalScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.display);
        scroll = (HorizontalScrollView) findViewById(R.id.scroll);
        scroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                scroll.fullScroll(View.FOCUS_RIGHT);
            }
        });
        expression = new LinkedList<>();
        initialize();
    }

    private void initialize() {
        expression.clear();
        updateDisplay();
    }

    public void delClick(View view) {
        if (!expression.isEmpty()) {
            String term = expression.getLast();
            if (addNew(term)) {
                expression.removeLast();
            } else {
                if (term.length() == 1) {
                    expression.removeLast();
                } else {
                    expression.add(expression.removeLast().substring(0, term.length() - 1));
                }
            }
            updateDisplay();
        }
    }

    private boolean addNew(String s) {
        return s.equals("+") || s.equals("\u00F7") || s.equals("\u00D7") || s.equals("\u2212");
    }

    public void acClick(View view) {
        initialize();
    }

    public void termClick(View view) {
        String term = getTerm(view);
        if (expression.isEmpty()) {
            expression.add(term);
        } else if (addNew(term) || addNew(expression.getLast())) {
            expression.add(getTerm(view));
        } else {
            expression.add(expression.removeLast() + getTerm(view));
        }
        updateDisplay();
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

    public void equalClick(View view) {
        Stack<String> stack = new Stack<>();
        int size = expression.size();
        for (int i = 0; i < size; i++) {
            String s = expression.remove();
            if (isOperator(s)) {
                while (!stack.isEmpty() && higherOrEqualPrecedence(stack.peek(), s)) {
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
        updateDisplay();
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
        expression.add(stack.pop());
    }

    private String operate(String x, String y, String o) {
        switch (o) {
            case "\u00D7":
                return multiply(x, y);
            case "\u00F7":
                return divide(y, x);
            case "+":
                return add(x, y);
            case "\u2212":
                return subtract(y, x);
            default:
                return "0";
        }
    }

    private String multiply(String x, String y) {
        double i = Double.parseDouble(x);
        double j = Double.parseDouble(y);
        double k = i * j;
        if (isInt(k)) {
            return String.valueOf((int) k);
        } else {
            return String.valueOf(k);
        }
    }

    private String divide(String x, String y) {
        double i = Double.parseDouble(x);
        double j = Double.parseDouble(y);
        double k = i / j;
        if (isInt(k)) {
            return String.valueOf((int) k);
        } else {
            return String.valueOf(k);
        }
    }

    private String add(String x, String y) {
        double i = Double.parseDouble(x);
        double j = Double.parseDouble(y);
        double k = i + j;
        if (isInt(k)) {
            return String.valueOf((int) k);
        } else {
            return String.valueOf(k);
        }
    }

    private String subtract(String x, String y) {
        double i = Double.parseDouble(x);
        double j = Double.parseDouble(y);
        double k = i - j;
        if (isInt(k)) {
            return String.valueOf((int) k);
        } else {
            return String.valueOf(k);
        }
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
            default:
                return false;
        }
    }

    public void updateDisplay() {
        String displayText = "";
        for (String t : expression) {
            displayText += t;
        }

        display.setText(displayText);
    }
}
