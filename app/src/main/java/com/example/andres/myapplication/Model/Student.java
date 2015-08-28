package com.example.andres.myapplication.Model;

/**
 * Created by andres on 11-04-15.
 */
public class Student {

    private String names;
    private String firstLastname;
    private String secondLastname;
    private int idCloud;

    public Student(String names, String firstLastname, String secondLastname){
        this.names = names;
        this.firstLastname = firstLastname;
        this.secondLastname = secondLastname;
    }

    public Student(String names, String firstLastname, String secondLastname, int idCloud){
        this.names = names;
        this.firstLastname = firstLastname;
        this.secondLastname = secondLastname;
        this.idCloud = idCloud;
    }


    public String getNames() {
        return names;
    }

    public String getFirstLastname() {
        return firstLastname;
    }

    public String getSecondLastname() {
        return secondLastname;
    }

    public int getIdCloud() {
        return idCloud;
    }

    @Override
    public boolean equals(Object o){
        Student s = (Student)o;
        if (s.getFirstLastname().equals(firstLastname) &&
            s.getSecondLastname().equals(secondLastname) &&
            s.getNames().equals(names))
            return true;
        return false;
    }


}
