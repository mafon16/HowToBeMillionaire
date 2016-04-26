package com.test.bemillionaire.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.test.bemillionaire.R;


public class GameOverDialog extends DialogFragment  {

    public SaveGameListener listener;

    public interface SaveGameListener{
        void save_and_exit();
        void exit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SaveGameListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_game_over, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.save_and_exit();
                dismiss();

            }
        });
        v.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.exit();
                dismiss();
            }
        });


        return v;
    }

}


