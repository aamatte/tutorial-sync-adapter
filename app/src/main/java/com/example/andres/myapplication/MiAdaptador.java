package com.example.andres.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by andres on 19-03-15.
 */
public class MiAdaptador extends ArrayAdapter<Item>
{
    private LayoutInflater mLayoutInflator;

    public MiAdaptador(Context context, int resource, Item[] objects){
        super(context, resource, objects);
        mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Item contenido = getItem(position);

        if (view == null){
            // crear nueva celda
            if (contenido.getmTipoCelda() ==0){
                view = mLayoutInflator.inflate(R.layout.list_item_2, parent, false);

            }
            else{
                view = mLayoutInflator.inflate(R.layout.list_item, parent, false);

            }
        }

        TextView texto = (TextView) view.findViewById(R.id.texto);
        texto.setText(contenido.getmTexto());

        return view;
    }

    // numero de tipos de items
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // retorna tipo de item de position
    @Override
    public int getItemViewType(int position) {
        Item contenido = getItem(position);
        return contenido.getmTipoCelda();
    }
}
