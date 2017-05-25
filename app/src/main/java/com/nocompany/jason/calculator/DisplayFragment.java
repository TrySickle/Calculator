package com.nocompany.jason.calculator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;


public class DisplayFragment extends Fragment {

    private TextView display;
    private HorizontalScrollView scroll;
    private OnFragmentInteractionListener mListener;
    private boolean rad;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rad = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        display = (TextView) view.findViewById(R.id.display);
        //System.out.println(display == null);
        scroll = (HorizontalScrollView) view.findViewById(R.id.scroll);
//        scroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                scroll.fullScroll(View.FOCUS_RIGHT);
//            }
//        });
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        return view;
    }

    public HorizontalScrollView getScroll() {
        return scroll;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_rad:
                mListener.radClick();
                if (rad) {
                    item.setTitle("DEG");
                    rad = false;
                } else {
                    item.setTitle("RAD");
                    rad = true;
                }
                break;
            default:
                break;
        }

        return true;
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

    public void updateDisplay(LinkedList<String> expression) {
        String displayText = "";
        for (String t : expression) {
            displayText += t;
        }
        display.setText(displayText);
    }
}
