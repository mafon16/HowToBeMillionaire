package com.test.bemillionaire.model;

import java.util.ArrayList;


public class Question {
    public String question;
    public ArrayList<Choice> choiceArrayList;
    public ArrayList<Choice> wrongChoiceArrayList;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Choice> getChoiceArrayList() {
        return choiceArrayList;
    }

    public void setChoiceArrayList(ArrayList<Choice> choiceArrayList) {
        this.choiceArrayList = choiceArrayList;

    }

    public void setWrongChoiceArrayList(ArrayList<Choice> wrongChoiceArrayList) {
        this.wrongChoiceArrayList = wrongChoiceArrayList;
    }

    public ArrayList<Choice> getWrongChoiceArrayList() {
        return wrongChoiceArrayList;
    }

}
