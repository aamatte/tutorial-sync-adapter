package com.example.andres.myapplication.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andres.myapplication.Activities.MainActivity;
import com.example.andres.myapplication.R;


public class StudentFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView textViewName;

    public StudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student, container, false);
        Bundle args = getArguments();
        String name;
        if (args != null){
            name = args.getString(MainActivity.CODE_NAME);
        }
        else{
            name = "Selecciona un alumno";
        }

        textViewName = (TextView) v.findViewById(R.id.texto);
        textViewName.setText(name);

        return v;
    }

    public void setNombre(String name){
        if (textViewName!=null)
        textViewName.setText(name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteractionStudent();
    }

}
