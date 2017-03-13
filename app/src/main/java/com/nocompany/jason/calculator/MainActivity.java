package com.nocompany.jason.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static long[] terms;
    private static String[] operators;
    private int numOperators;
    private int numTerms;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.display);
        display.setSelected(true);
        operators = new String[10];
        terms = new long[10];
        terms[0] = 0;
        numTerms = 1;
        setDisplay(terms);
    }

    public void numberClick(View view) {
        TextView display = (TextView) findViewById(R.id.display);
        String displayString;
        switch (view.getId()) {
            case R.id.zero:
                updateDisplay(0);
                break;
            case R.id.one:
                updateDisplay(1);
                break;
            case R.id.two:
                updateDisplay(2);
                break;
            case R.id.three:
                updateDisplay(3);
                break;
            case R.id.four:
                updateDisplay(4);
                break;
            case R.id.five:
                updateDisplay(5);
                break;
            case R.id.six:
                updateDisplay(6);
                break;
            case R.id.seven:
                updateDisplay(7);
                break;
            case R.id.eight:
                updateDisplay(8);
                break;
            case R.id.nine:
                updateDisplay(9);
                break;
        }
    }

    public void updateDisplay(long input) {
        long term = terms[numOperators];
        terms[numOperators] = term * 10 + input;

        setDisplay(terms);
    }

    public void setDisplay(long[] terms) {
        String displayString = "";
        for (int i = 0; i < numTerms; i++) {
            displayString += terms[i];
            if (i > 0) {
                displayString += operators[i - 1];
            }
        }

        display.setText(displayString);
    }
}
