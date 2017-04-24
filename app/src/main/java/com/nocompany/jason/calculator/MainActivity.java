package com.nocompany.jason.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;
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
            expression.removeLast();
            updateDisplay();
        }
    }

    public void acClick(View view) {
        initialize();
    }

    public void termClick(View view) {
        expression.add(getTerm(view));
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

    public void updateDisplay() {
        String displayText = "";
        for (String t : expression) {
            displayText += t;
        }

        display.setText(displayText);
    }
}
