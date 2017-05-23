package com.nocompany.jason.calculator;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShiftFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShiftFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public ShiftFragment() {
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
        View view = inflater.inflate(R.layout.fragment_shift, container, false);
        GridLayout gridLayout =
                (GridLayout) view.findViewById(R.id.shift_gridlayout);
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
        case R.id.del:
            mListener.delClick();
            break;
        case R.id.allClear:
            mListener.acClick();
            break;
        case R.id.leftPar:
            mListener.bracketClick(view);
            break;
        case R.id.rightPar:
            mListener.bracketClick(view);
            break;
        case R.id.ln:
        case R.id.log:
        case R.id.sin:
        case R.id.cos:
        case R.id.tan:
        case R.id.square_root:
        case R.id.arcsin:
        case R.id.arccos:
        case R.id.arctan:
        case R.id.cube_root:
            mListener.operatorClick(view);
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
