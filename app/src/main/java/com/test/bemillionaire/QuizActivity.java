package com.test.bemillionaire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.bemillionaire.dialog.FriendHelpDialog;
import com.test.bemillionaire.dialog.GameOverDialog;
import com.test.bemillionaire.dialog.GameWinDialog;
import com.test.bemillionaire.dialog.GroupHelpDialog;
import com.test.bemillionaire.dialog.NetworkErrorDialog;
import com.test.bemillionaire.model.Choice;
import com.test.bemillionaire.model.Question;
import com.test.bemillionaire.network.HttpRequest;
import com.test.bemillionaire.network.HttpResponseListener;
import com.test.bemillionaire.view.CashAdapter;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener,HttpResponseListener,GameOverDialog.SaveGameListener {


    private static final int HANDLE_CHECKED_ANSWER = 0; //key for handler
    private static final int FINISHED_QUESTION = 14; //last question

    private ImageButton btn_help_fifty_fifty, btn_help_friend, btn_help_group;
    private Button btn_exit, btn_next;
    private TextView tv_question, tv_choice_a, tv_choice_b, tv_choice_c, tv_choice_d, tv_info_current;
    private ListView lv_cash;
    private LinearLayout ll_a, ll_b, ll_c, ll_d;

    private CashAdapter cashAdapter;

    private String choiceA, choiceB, choiceC, choiceD;
    boolean isCorrectA, isCorrectB, isCorrectC, isCorrectD;

    public ArrayList<Question> questionArrayList;
    private Question question;
    public ArrayList<Choice> wrongChoiceArray;
    private String currentQuestion;
    private int numOfQuestion;

    private Handler handler;
    public Random random;
    private DB db;
    private HttpRequest request;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mSettingsEditor;
    private boolean isSavedGame;
    private boolean isFriendHelpOpen = true;
    private boolean isGroupHelpOpen = true;
    private boolean isFiftyFiftyHelpOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new DB(this);
        db.open();

        btn_help_fifty_fifty = (ImageButton) findViewById(R.id.btn_help_fifty_fifty);
        btn_help_friend = (ImageButton) findViewById(R.id.btn_help_friend);
        btn_help_group = (ImageButton) findViewById(R.id.btn_help_group);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        ll_a = (LinearLayout) findViewById(R.id.ll_a);
        ll_b = (LinearLayout) findViewById(R.id.ll_b);
        ll_c = (LinearLayout) findViewById(R.id.ll_c);
        ll_d = (LinearLayout) findViewById(R.id.ll_d);

        tv_question = (TextView) findViewById(R.id.tv_question);
        tv_choice_a = (TextView) findViewById(R.id.tv_choice_a);
        tv_choice_b = (TextView) findViewById(R.id.tv_choice_b);
        tv_choice_c = (TextView) findViewById(R.id.tv_choice_c);
        tv_choice_d = (TextView) findViewById(R.id.tv_choice_d);

        tv_info_current = (TextView) findViewById(R.id.tv_info_current);
        lv_cash = (ListView) findViewById(R.id.lv_cash);
        lv_cash.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setupCashListView();

        Intent intent = getIntent();
        isSavedGame = intent.getBooleanExtra("isSavedGame", false);
        if (isSavedGame) {
            loadSavedGame();
        } else {
            startNewGame();
        }
    }




    private void startNewGame() {
        request = new HttpRequest(this, this);//I decide to use a native network connections methods, but last time in my projects I used Volley lib.
        if (!request.isOnline(this)) {
            NetworkErrorDialog networkErrorDialog = new NetworkErrorDialog();
            networkErrorDialog.show(getSupportFragmentManager(), "net_error");
        } else {
            questionArrayList = request.getQuestionList();
            numOfQuestion = 0;    //initialize first question
            setQuestion(numOfQuestion);
            highlightCash(cashAdapter.getCount());
        }
    }

    private void setQuestion(int order) {
        btn_next.setVisibility(View.GONE);
        tv_info_current.setVisibility(View.GONE);
        question = questionArrayList.get(order);

        currentQuestion = question.getQuestion();
        wrongChoiceArray = question.getWrongChoiceArrayList();

        choiceA = question.getChoiceArrayList().get(0).getChoice();
        isCorrectA = question.getChoiceArrayList().get(0).isTrue();

        choiceB = question.getChoiceArrayList().get(1).getChoice();
        isCorrectB = question.getChoiceArrayList().get(1).isTrue();

        choiceC = question.getChoiceArrayList().get(2).getChoice();
        isCorrectC = question.getChoiceArrayList().get(2).isTrue();

        choiceD = question.getChoiceArrayList().get(3).getChoice();
        isCorrectD = question.getChoiceArrayList().get(3).isTrue();

        tv_question.setText(currentQuestion);
        tv_choice_a.setText(choiceA);
        tv_choice_b.setText(choiceB);
        tv_choice_c.setText(choiceC);
        tv_choice_d.setText(choiceD);
    }

    private void loadSavedGame() {
        Cursor cursorQuestion = db.getQuestions();
        if (cursorQuestion != null && cursorQuestion.getCount() > 0) {
            questionArrayList = new ArrayList<>();
            ArrayList<Choice> choiceList;
            Question question;
            while (cursorQuestion.moveToNext()) {
                question = new Question();
                int questionColIndex = cursorQuestion.getColumnIndex(DB.QUESTION_TITLE);
                String questionTitle = cursorQuestion.getString(questionColIndex);
                question.setQuestion(questionTitle);
                Cursor cursorAnswer = db.getAnswersSortedbyQuestion(questionTitle);
                choiceList = new ArrayList<>();
                while (cursorAnswer.moveToNext()) {
                    choiceList.add(new Choice(cursorAnswer.getString(cursorAnswer.getColumnIndex(DB.ANSWER_TITLE)),
                            cursorAnswer.getInt(cursorAnswer.getColumnIndex(DB.ANSWER_ISTRUE)) > 0));
                }
                question.setChoiceArrayList(choiceList);
                questionArrayList.add(question);
            }
            cursorQuestion.close();
            db.close();


            mSettings = getPreferences(MODE_PRIVATE);
            numOfQuestion = mSettings.getInt("last_question", 0);

            setQuestion(numOfQuestion);
            highlightCash(cashAdapter.getCount() - numOfQuestion);
            loadHelpStates();
        } else {
            startNewGame();
        }
    }

    private void  loadHelpStates(){
        isFriendHelpOpen = mSettings.getBoolean("friend_help_state", false);
        isGroupHelpOpen =  mSettings.getBoolean("group_help_state", false);
        isFiftyFiftyHelpOpen = mSettings.getBoolean("fifty_fifty_help_state", false);


        btn_help_group.setSelected(!isGroupHelpOpen);
        btn_help_group.setClickable(isGroupHelpOpen);

        btn_help_friend.setSelected(!isFriendHelpOpen);
        btn_help_friend.setClickable(isFriendHelpOpen);

        btn_help_fifty_fifty.setSelected(!isFiftyFiftyHelpOpen);
        btn_help_fifty_fifty.setClickable(isFiftyFiftyHelpOpen);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_help_fifty_fifty:
                showFiftyFiftyHelp();
                break;
            case R.id.btn_help_friend:
                showFriendHelp();
                break;
            case R.id.btn_help_group:
                showGroupHelp();
                break;
            case R.id.btn_exit:
                GameOverDialog gameOverDialog = new GameOverDialog();
                gameOverDialog.show(getSupportFragmentManager(), "exit");
                break;
            case R.id.ll_a:
                checkAnswer(isCorrectA, v);
                break;
            case R.id.ll_b:
                checkAnswer(isCorrectB, v);
                break;
            case R.id.ll_c:
                checkAnswer(isCorrectC, v);
                break;
            case R.id.ll_d:
                checkAnswer(isCorrectD, v);
                break;
            case R.id.btn_next:
                setNextQuestion();
                break;
        }
    }





    private void showFiftyFiftyHelp() {
        isFiftyFiftyHelpOpen=false;
        btn_help_fifty_fifty.setSelected(true);
        btn_help_fifty_fifty.setClickable(false);

        random = new Random();

        int firstRandWrongNum = random.nextInt(wrongChoiceArray.size());
        String firstRandWrongChoice = wrongChoiceArray.get(firstRandWrongNum).getChoice();
        wrongChoiceArray.remove(firstRandWrongNum);

        int secRandWrongNum = random.nextInt(wrongChoiceArray.size());
        String secRandWrongChoice = wrongChoiceArray.get(secRandWrongNum).getChoice();


        if (tv_choice_a.getText().equals(firstRandWrongChoice) || tv_choice_a.getText().equals(secRandWrongChoice)) {
            tv_choice_a.setText("");
            ll_a.setClickable(false);
        }
        if (tv_choice_b.getText().equals(firstRandWrongChoice) || tv_choice_b.getText().equals(secRandWrongChoice)) {
            tv_choice_b.setText("");
            ll_b.setClickable(false);
        }
        if (tv_choice_c.getText().equals(firstRandWrongChoice) || tv_choice_c.getText().equals(secRandWrongChoice)) {
            tv_choice_c.setText("");
            ll_c.setClickable(false);
        }
        if (tv_choice_d.getText().equals(firstRandWrongChoice) || tv_choice_d.getText().equals(secRandWrongChoice)) {
            tv_choice_d.setText("");
            ll_d.setClickable(false);
        }
    }

    private void showFriendHelp() {
        isFriendHelpOpen=false;
        btn_help_friend.setSelected(true);
        btn_help_friend.setClickable(false);

        random = new Random();
        int randChoiceNum = random.nextInt(question.getChoiceArrayList().size());
        String randChoice = question.getChoiceArrayList().get(randChoiceNum).getChoice();
        FriendHelpDialog friendHelpDialog = FriendHelpDialog.newInstance(randChoice);
        friendHelpDialog.show(getSupportFragmentManager(), "FriendHelp");

    }

    private void showGroupHelp() {
        isGroupHelpOpen=false;
        btn_help_group.setSelected(true);
        btn_help_group.setClickable(false);

        GroupHelpDialog groupHelpDialog = new GroupHelpDialog();
        groupHelpDialog.show(getSupportFragmentManager(), "GroupHelp");

    }

    private void checkAnswer(boolean choice, View v) {
        tv_info_current.setVisibility(View.VISIBLE);
        if (choice) {
            tv_info_current.setText(R.string.correct);
            v.setBackgroundResource((R.drawable.btn_choice_true));
            if (numOfQuestion == FINISHED_QUESTION) {
                GameWinDialog gameWinDialog = new GameWinDialog();
                gameWinDialog.show(getSupportFragmentManager(), "game_win");
            } else {
                btn_next.setVisibility(View.VISIBLE);
            }
        } else {
            v.setBackgroundResource((R.drawable.btn_choice_false));
            showCorrectAnswer();
            tv_info_current.setText(R.string.wrong);
            btn_next.setVisibility(View.GONE);
        }
        ll_a.setClickable(false);
        ll_b.setClickable(false);
        ll_c.setClickable(false);
        ll_d.setClickable(false);
    }

    private void setNextQuestion() {
        ll_a.setClickable(true);
        ll_b.setClickable(true);
        ll_c.setClickable(true);
        ll_d.setClickable(true);
        ll_a.setBackgroundResource(R.drawable.btn_choice_selector);
        ll_b.setBackgroundResource(R.drawable.btn_choice_selector);
        ll_c.setBackgroundResource(R.drawable.btn_choice_selector);
        ll_d.setBackgroundResource(R.drawable.btn_choice_selector);

        numOfQuestion++;
        setQuestion(numOfQuestion);
        highlightCash(cashAdapter.getCount() - numOfQuestion);
    }

    private void highlightCash(int pos) {
        lv_cash.setItemChecked(pos - 1, true);//-1 is correct current position
    }


    private void showCorrectAnswer() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == HANDLE_CHECKED_ANSWER) {
                    if (isCorrectA) {
                        ll_a.setBackgroundResource(R.drawable.btn_choice_true);
                    } else if (isCorrectB) {
                        ll_b.setBackgroundResource(R.drawable.btn_choice_true);
                    } else if (isCorrectC) {
                        ll_c.setBackgroundResource(R.drawable.btn_choice_true);
                    } else if (isCorrectD) {
                        ll_d.setBackgroundResource(R.drawable.btn_choice_true);
                    }
                }
                return true;
            }
        });

        Thread t = new Thread(new Runnable() {
            public void run() {

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(HANDLE_CHECKED_ANSWER);

            }
        });
        t.start();
    }



    private void setupCashListView() {
        ArrayList cashList = new ArrayList();
        cashList.add(1000000);
        cashList.add(500000);
        cashList.add(250000);
        cashList.add(125000);
        cashList.add(64000);
        cashList.add(32000);
        cashList.add(16000);
        cashList.add(8000);
        cashList.add(4000);
        cashList.add(2000);
        cashList.add(1000);
        cashList.add(500);
        cashList.add(300);
        cashList.add(200);
        cashList.add(100);

        cashAdapter = new CashAdapter(this, cashList);
        lv_cash.setAdapter(cashAdapter);
        lv_cash.setClickable(false);
        highlightCash(cashAdapter.getCount());
    }


    private void saveGame() {
        mSettingsEditor = getPreferences(MODE_PRIVATE).edit();
        mSettingsEditor.putInt("last_question", numOfQuestion);
        mSettingsEditor.putBoolean("friend_help_state", isFriendHelpOpen);
        mSettingsEditor.putBoolean("group_help_state",isGroupHelpOpen);
        mSettingsEditor.putBoolean("fifty_fifty_help_state",isFiftyFiftyHelpOpen);
        mSettingsEditor.apply();

        db.clearTables();
        for (Question question : questionArrayList) {
            db.addQuestion(question.getQuestion());
            for (Choice choice : question.getChoiceArrayList())
                db.addAnswer(choice.getChoice(), choice.isTrue(), question.getQuestion());
        }
    }

    @Override
    public void handleError() {

    }

    @Override
    public void save_and_exit() {
        saveGame();
        Intent intent = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void exit() {
        Intent intent = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(intent);
    }
}