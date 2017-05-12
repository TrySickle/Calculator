package com.nocompany.jason.calculator;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

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
        GridLayout gridLayout =
                (GridLayout) view.findViewById(R.id.button_gridlayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View v = gridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(this);
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
            mListener.termClick(view);
            break;
        case R.id.one:
            mListener.termClick(view);
            break;
        case R.id.two:
            mListener.termClick(view);
            break;
        case R.id.three:
            mListener.termClick(view);
            break;
        case R.id.four:
            mListener.termClick(view);
            break;
        case R.id.five:
            mListener.termClick(view);
            break;
        case R.id.six:
            mListener.termClick(view);
            break;
        case R.id.seven:
            mListener.termClick(view);
            break;
        case R.id.eight:
            mListener.termClick(view);
            break;
        case R.id.nine:
            mListener.termClick(view);
            break;
        case R.id.point:
            mListener.termClick(view);
            break;
        case R.id.ans:
            mListener.termClick(view);
            break;
        case R.id.multiply:
            mListener.termClick(view);
            break;
        case R.id.divide:
            mListener.termClick(view);
            break;
        case R.id.add:
            mListener.termClick(view);
            break;
        case R.id.subtract:
            mListener.termClick(view);
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
