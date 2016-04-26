package com.test.bemillionaire.dialog;

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


public class GameEmptyDialog extends DialogFragment implements View.OnClickListener {

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_game_empty, null);
        v.findViewById(R.id.btnOk).setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }


    @Override
    public void onClick(View v) {

        dismiss();

    }

}
