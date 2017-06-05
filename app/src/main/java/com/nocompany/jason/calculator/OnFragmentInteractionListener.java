package com.nocompany.jason.calculator;

import android.view.View;

public interface OnFragmentInteractionListener {
    void shiftClick();
    void delClick();
    void acClick();
    void pointClick();
    void negateClick();
    void numberClick(View view);
    void operatorClick(View view);
    void equalClick();
    void bracketClick(View view);
    void radClick();
}
