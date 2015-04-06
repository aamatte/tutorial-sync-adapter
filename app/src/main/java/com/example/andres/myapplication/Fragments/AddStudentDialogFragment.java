package com.example.andres.myapplication.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.andres.myapplication.R;


public class AddStudentDialogFragment extends DialogFragment {

    public EditText editText;
    View v;
    private NoticeDialogListener mListener;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String name, String firstLastname, String secondLastname);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_agregar_alumno, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.dialog_agregar_alumno_title);
        builder.setPositiveButton(R.string.dialog_agregar_alumno_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = ((EditText) v.findViewById(R.id.dialog_student_name)).getText().toString().toUpperCase();
                String firstLastname = ((EditText) v.findViewById(R.id.dialog_first_lastname)).getText().toString().toUpperCase();
                String secondLastname = ((EditText) v.findViewById(R.id.dialog_second_lastname)).getText().toString().toUpperCase();

                mListener.onDialogPositiveClick(AddStudentDialogFragment.this , name, firstLastname, secondLastname);
            }
        })
                .setNegativeButton(R.string.dialog_agregar_alumno_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // NADA
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
