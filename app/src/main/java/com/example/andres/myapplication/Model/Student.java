package com.example.andres.myapplication.Model;

import java.util.Comparator;

public class Student implements Comparator<Student>{

    public static final String NAME = "name";
    public static final String FIRST_LASTNAME = "first_lastname";
    public static final String SECOND_LASTNAME = "second_lastname";
    public static final String ID = "id";

    private String mNames;
    private String mFirstLastname;
    private String mSecondLastname;
    private int mIdCloud;

    public Student() { }

    public Student(String names, String firstLastname, String secondLastname) {
        mNames = names;
        mFirstLastname = firstLastname;
        mSecondLastname = secondLastname;
    }

    public Student(String names, String firstLastname, String secondLastname, int idCloud) {
        mNames = names;
        mFirstLastname = firstLastname;
        mSecondLastname = secondLastname;
        mIdCloud = idCloud;
    }

    public String getNames() {
        return mNames;
    }

    public String getFirstLastname() {
        return mFirstLastname;
    }

    public String getSecondLastname() {
        return mSecondLastname;
    }

    public int getIdCloud() {
        return mIdCloud;
    }

    @Override
    public int compare(Student lhs, Student rhs) {
        return lhs.getFirstLastname().compareTo(rhs.getFirstLastname());
    }

    @Override
    public boolean equals(Object o) {
        Student student = (Student)o;
        if (student.getFirstLastname().equals(mFirstLastname) &&
            student.getSecondLastname().equals(mSecondLastname) &&
            student.getNames().equals(mNames))
            return true;
        return false;
    }
}
