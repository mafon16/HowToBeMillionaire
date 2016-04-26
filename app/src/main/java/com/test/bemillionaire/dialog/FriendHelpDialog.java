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


public class FriendHelpDialog extends DialogFragment implements View.OnClickListener {

    private String message;
    private TextView tv_friend_tip;


    public  static FriendHelpDialog newInstance(String message) {
        FriendHelpDialog f = new FriendHelpDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_friend_help, null);
        v.findViewById(R.id.btnOk).setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        message = getArguments().getString("message");
        tv_friend_tip = (TextView)v.findViewById(R.id.tv_friend_tip);
        tv_friend_tip.setText(message);

        return v;
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }


}
