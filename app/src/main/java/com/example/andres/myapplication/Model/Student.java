package com.example.andres.myapplication.Model;

/**
 * Created by andres on 11-04-15.
 */
public class Student {

    private String names;
    private String firstLastname;
    private String secondLastname;
    private int idCloud;

    public Student(){ }


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

    public Student(String names, String firstLastname){
        this.names = names;
        this.firstLastname = firstLastname;
        this.secondLastname = "";
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getFirstLastname() {
        return firstLastname;
    }

    public void setFirstLastname(String firstLastname) {
        this.firstLastname = firstLastname;
    }

    public String getSecondLastname() {
        return secondLastname;
    }

    public void setSecondLastname(String secondLastname) {
        this.secondLastname = secondLastname;
    }

    public int getIdCloud() {
        return idCloud;
    }

    public void setIdCloud(int idCloud) {
        this.idCloud = idCloud;
    }


}
