package com.nocompany.jason.calculator;

import android.view.View;

/**
 * Created by jason on 5/11/2017.
 */

public interface OnFragmentInteractionListener {
    void shiftClick();
    void delClick();
    void acClick();
    void negateClick();
    void termClick(View view);
    void equalClick();
}
