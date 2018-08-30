package com.celeber.habitappandroid;

public class Ban {

    private int id;
    private String title;
    private String submit;
    private String resist;

    public Ban() {

    }

    public Ban(int id, String title, String submit, String resist) {
        this.id = id;
        this.title = title;
        this.submit = submit;
        this.resist = resist;

    }

    public Integer getID(){
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getSubmit() {
        return submit;
    }

    public String getResist() {
        return resist;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public void setResist(String resist){
        this.resist = resist;
    }

}
