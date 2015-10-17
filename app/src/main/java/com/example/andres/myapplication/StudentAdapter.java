package com.example.andres.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andres.myapplication.Model.Item;

import java.util.ArrayList;

/**
 * Genera la vista de cada celda de la lista de estudiantes.
 */
public class StudentAdapter extends ArrayAdapter<Item>
{
    private LayoutInflater mLayoutInflater;

    public StudentAdapter(Context context, int resource, ArrayList<Item> objects){
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Item item = getItem(position);
        if (view == null){
            // Crear nueva celda
            // Si es una celda de letra entonces tiene layout diferente.
            if (item.getCellType() == Item.LETTER_CELL) {
                view = mLayoutInflater.inflate(R.layout.list_item_2, parent, false);
            }
            else {
                view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            }
        }
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(item.getText());
        return view;
    }

    /**
     * Numero de tipos de items.
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Retorna tipo de item de position
     */
    @Override
    public int getItemViewType(int position) {
        Item item = getItem(position);
        return item.getCellType();
    }
}
