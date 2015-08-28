package com.example.andres.myapplication.Model;

/**
 * Created by andres on 19-03-15.
 */
public class Item {
    private String mTexto;
    private int mTipoCelda;

    public Item(String mTexto, int mTipoCelda) {
        this.mTexto = mTexto;
        this.mTipoCelda = mTipoCelda;
    }

    public int getmTipoCelda() {
        return mTipoCelda;
    }


    public String getmTexto() {
        return mTexto;
    }

}
