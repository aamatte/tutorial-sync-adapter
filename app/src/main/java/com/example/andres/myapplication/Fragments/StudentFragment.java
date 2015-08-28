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

    public void setName(String name){
        if (textViewName!=null)
        textViewName.setText(name);
    }


}
