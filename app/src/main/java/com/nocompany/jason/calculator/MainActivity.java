package com.nocompany.jason.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static ArrayList<Term> terms;
    private static ArrayList<Operator> operators;
    private int numOperators;
    private int numTerms;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.display);
        operators = new ArrayList<>();
        terms = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        terms.add(new Term(0L));
        numTerms = terms.size();
        updateDisplay();
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
        Term term = terms.get(numOperators);
        term.multiplyValue(10);
        term.addValue(increase);
        updateDisplay();
    }

    public void updateDisplay() {
        String displayText = "";
        for (int i = 0; i < numTerms; i++) {
            displayText += terms.get(i).getDisplay();
            if (i > 0) {
                displayText += operators.get(i - 1);
            }
        }

        display.setText(displayText);
    }
}
