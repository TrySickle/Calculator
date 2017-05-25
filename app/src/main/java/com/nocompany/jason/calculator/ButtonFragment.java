package com.nocompany.jason.calculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public ButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_button, container, false);
        LinearLayout gridLayout =
                (LinearLayout) view.findViewById(R.id.button_gridlayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View v = gridLayout.getChildAt(i);
            if (v instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    View b = ((LinearLayout) v).getChildAt(j);
                    if (b instanceof Button) {
                        b.setOnClickListener(this);
                    }
                }
            }
        }
        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.shift:
            mListener.shiftClick();
            break;
        case R.id.zero:
            mListener.numberClick(view);
            break;
        case R.id.one:
            mListener.numberClick(view);
            break;
        case R.id.two:
            mListener.numberClick(view);
            break;
        case R.id.three:
            mListener.numberClick(view);
            break;
        case R.id.four:
            mListener.numberClick(view);
            break;
        case R.id.five:
            mListener.numberClick(view);
            break;
        case R.id.six:
            mListener.numberClick(view);
            break;
        case R.id.seven:
            mListener.numberClick(view);
            break;
        case R.id.eight:
            mListener.numberClick(view);
            break;
        case R.id.nine:
            mListener.numberClick(view);
            break;
        case R.id.ans:
            mListener.numberClick(view);
            break;
        case R.id.point:
            mListener.operatorClick(view);
            break;
        case R.id.multiply:
            mListener.operatorClick(view);
            break;
        case R.id.divide:
            mListener.operatorClick(view);
            break;
        case R.id.add:
            mListener.operatorClick(view);
            break;
        case R.id.subtract:
            mListener.operatorClick(view);
            break;
        case R.id.equals:
            mListener.equalClick();
            break;
        case R.id.negate:
            mListener.negateClick();
            break;
        case R.id.allClear:
            mListener.acClick();
            break;
        case R.id.del:
            mListener.delClick();
            break;
        case R.id.leftPar:
            mListener.bracketClick(view);
            break;
        case R.id.rightPar:
            mListener.bracketClick(view);
            break;
        default:
            break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
