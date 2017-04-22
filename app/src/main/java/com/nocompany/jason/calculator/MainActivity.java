package com.nocompany.jason.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends Activity {

    private static LinkedList<Term> expression;
    private TextView display;
    private HorizontalScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.display);
        scroll = (HorizontalScrollView) findViewById(R.id.scroll);
        expression = new LinkedList<>();
        initialize();
    }

    private void initialize() {
        expression.clear();
        expression.add(new Operand(0L));
        updateDisplay();
    }

    public void cClick(View view) {
        Term op = expression.peekLast();
        ((Operand) op).setValue(0L);
        updateDisplay();
    }

    public void acClick(View view) {
        initialize();
    }

    public void numberClick(View view) {
        increaseTerm(view);
        updateDisplay();
    }
    
    private void increaseTerm(View view) {
        Number increase;
        switch (view.getId()) {
            case R.id.zero:
                increase = 0L;
                break;
            case R.id.one:
                increase = 1L;
                break;
            case R.id.two:
                increase = 2L;
                break;
            case R.id.three:
                increase = 3L;
                break;
            case R.id.four:
                increase = 4L;
                break;
            case R.id.five:
                increase = 5L;
                break;
            case R.id.six:
                increase = 6L;
                break;
            case R.id.seven:
                increase = 7L;
                break;
            case R.id.eight:
                increase = 8L;
                break;
            case R.id.nine:
                increase = 9L;
                break;
            default:
                increase = 0L;
        }
        Term op = expression.peekLast();
        ((Operand) op).multiplyValue(10);
        ((Operand) op).addValue(increase);
        updateDisplay();
    }

    public void updateDisplay() {
        String displayText = "";
        for (Term t : expression) {
            displayText += t;
        }

        display.setText(displayText);
        scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
    }
}
