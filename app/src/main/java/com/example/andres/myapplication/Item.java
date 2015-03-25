package com.example.andres.myapplication;

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

    public void setmTipoCelda(int mTipoCelda) {
        this.mTipoCelda = mTipoCelda;
    }

    public String getmTexto() {
        return mTexto;
    }

    public void setmTexto(String mTexto) {
        this.mTexto = mTexto;
    }
}
