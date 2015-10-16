package com.example.andres.myapplication.Model;

public class Item {
    public final static int STUDENT_CELL = 1;
    public final static int LETTER_CELL = 0;

    private String mText;
    private int mCellType;

    public Item(String text, int cellType) {
        mText = text;
        mCellType = cellType;
    }

    public int getCellType() {
        return mCellType;
    }

    public String getText() {
        return mText;
    }
}
