package com.test.bemillionaire.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import com.test.bemillionaire.model.Choice;
import com.test.bemillionaire.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HttpRequest {
    private static final String TAG = "WES/HttpRequest";

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private HttpResponseListener httpResponseListener;
    private Connection connection;
    private HashMap<String,String> mapResult;
    private JSONArray jsonArrayResult=null;


    public HttpRequest(Context context, HttpResponseListener httpResponseListener){
        this.httpResponseListener =httpResponseListener;
        this.connection = new Connection(context);
    }



    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public ArrayList getQuestionList() {
        String url = "http://ec2-52-37-184-161.us-west-2.compute.amazonaws.com:80/api/game/";

        jsonArrayResult  = connection.makeRequest( GET, url, null);
        ArrayList<Question> questionsList = new ArrayList<>();
        ArrayList<Choice>  choiceList;
        ArrayList<Choice>  wrongChoiceList;
        Question question;
        for (int i = 0; i < jsonArrayResult.length(); i++) {
             question=new Question();
            try {
                JSONObject jsonObject=jsonArrayResult.getJSONObject(i);
                question.setQuestion(jsonObject.optString("body"));

                JSONArray answersJsonArray=jsonObject.getJSONArray("answers");
                choiceList=new ArrayList<>();
                wrongChoiceList=new ArrayList<>();
                for(int u=0;u<answersJsonArray.length();u++){
                    JSONObject answerJsonObject=answersJsonArray.getJSONObject(u);
                    choiceList.add(new Choice(answerJsonObject.optString("body"), answerJsonObject.optBoolean("is_correct")));
                    if(!answerJsonObject.optBoolean("is_correct")){
                         wrongChoiceList.add(new Choice(answerJsonObject.optString("body"), answerJsonObject.optBoolean("is_correct")));
                    }
                }
                question.setChoiceArrayList(choiceList);
                question.setWrongChoiceArrayList(wrongChoiceList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            questionsList.add(question);
        }
        return  questionsList;
    }




}


