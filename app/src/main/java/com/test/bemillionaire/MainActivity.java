package com.test.bemillionaire;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.test.bemillionaire.dialog.GameEmptyDialog;


public class MainActivity extends AppCompatActivity {
    private Button btn_start,btn_load;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);

        btn_start=(Button)findViewById(R.id.btn_start);
        btn_load =(Button)findViewById(R.id.btn_load);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                Cursor cursorQuestion = db.getQuestions();

                if (cursorQuestion != null && cursorQuestion.getCount() > 0) {
                    cursorQuestion.close();
                    db.close();
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("isSavedGame", true);
                    startActivity(intent);
                } else {
                    GameEmptyDialog gameEmptyDialog = new GameEmptyDialog();
                    gameEmptyDialog.show(getSupportFragmentManager(), "empty_game");
                }
            }
        });


    }
}
