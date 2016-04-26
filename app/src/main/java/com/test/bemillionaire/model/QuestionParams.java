package com.test.bemillionaire.model;

import java.util.Map;




public class QuestionParams {
    String question;
    Map choiceA,choiceB,choiceC,choiceD;

    public QuestionParams(String question, Map choiceA, Map choiceB, Map choiceC, Map choiceD) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
    }

    //    public QuestionParams(String ){
//
//
//    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(Map choiceA) {
        this.choiceA = choiceA;
    }

    public Map getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(Map choiceB) {
        this.choiceB = choiceB;
    }

    public Map getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(Map choiceC) {
        this.choiceC = choiceC;
    }

    public Map getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(Map choiceD) {
        this.choiceD = choiceD;
    }
}
