package com.example.andres.myapplication.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andres.myapplication.Activities.MainActivity;
import com.example.andres.myapplication.R;

/**
 * Fragment que solo muestra el nombre del estudiante seleccionado.
 */
public class StudentFragment extends Fragment {

    private TextView mNameView;

    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student, container, false);
        Bundle args = getArguments();
        mNameView = (TextView) v.findViewById(R.id.text_view);
        mNameView.setText(R.string.select_a_student);
        if (args != null){
            mNameView.setText(args.getString(MainActivity.CODE_NAME));
        }
        return v;
    }

    public void setName(String name){
        if (mNameView !=null)
        mNameView.setText(name);
    }
}
