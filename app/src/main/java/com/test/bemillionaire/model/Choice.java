package com.test.bemillionaire.model;

/**
 * Created by Admin on 19.04.2016.
 */
public class Choice {
    public String choice;
    public boolean isTrue;
//    public Choice(String choice,boolean isTrue){}

    public Choice(String choice, boolean isTrue) {
        this.choice = choice;
        this.isTrue = isTrue;
    }

    public String getChoice() {
        return choice;
    }



    public boolean isTrue() {
        return isTrue;
    }


}
