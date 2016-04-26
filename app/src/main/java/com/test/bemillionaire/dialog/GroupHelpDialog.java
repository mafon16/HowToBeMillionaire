package com.test.bemillionaire.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.test.bemillionaire.R;

import java.util.Random;


public class GroupHelpDialog extends DialogFragment implements View.OnClickListener {

    private TextView tv_tip_a,tv_tip_b,tv_tip_c,tv_tip_d;
    private View graf_tip_a,graf_tip_b,graf_tip_c,graf_tip_d;



    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_group_help, null);
        v.findViewById(R.id.btnOk).setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        tv_tip_a = (TextView)v.findViewById(R.id.tv_tip_a);
        tv_tip_b = (TextView)v.findViewById(R.id.tv_tip_b);
        tv_tip_c = (TextView)v.findViewById(R.id.tv_tip_c);
        tv_tip_d = (TextView)v.findViewById(R.id.tv_tip_d);

        graf_tip_a=(View)v.findViewById(R.id.graf_tip_a);
        graf_tip_b=(View)v.findViewById(R.id.graf_tip_b);
        graf_tip_c=(View)v.findViewById(R.id.graf_tip_c);
        graf_tip_d=(View)v.findViewById(R.id.graf_tip_d);

         Random random = new Random();
        //create four random number ,that have sum -100):
        int firstRandNum=random.nextInt(101);
        int secondRandNum=random.nextInt(100-firstRandNum);
        int thirdRandNum=random.nextInt(100-firstRandNum-secondRandNum);
        int fourthRandNum=100-firstRandNum-secondRandNum-thirdRandNum;

        tv_tip_a.setText(firstRandNum + "%");
        tv_tip_b.setText(secondRandNum + "%");
        tv_tip_c.setText(thirdRandNum+"%");
        tv_tip_d.setText(fourthRandNum+"%");

        LinearLayout.LayoutParams paramsA = (LinearLayout.LayoutParams) graf_tip_a.getLayoutParams();
        paramsA.height = firstRandNum*2;
        graf_tip_a.setLayoutParams(paramsA);

        LinearLayout.LayoutParams paramsB = (LinearLayout.LayoutParams) graf_tip_b.getLayoutParams();
        paramsB.height = secondRandNum*2;
        graf_tip_b.setLayoutParams(paramsB);

        LinearLayout.LayoutParams paramsC = (LinearLayout.LayoutParams) graf_tip_c.getLayoutParams();
        paramsC.height = thirdRandNum*2;
        graf_tip_c.setLayoutParams(paramsC);

        LinearLayout.LayoutParams paramsD = (LinearLayout.LayoutParams) graf_tip_d.getLayoutParams();
        paramsD.height = fourthRandNum*2;
        graf_tip_d.setLayoutParams(paramsD);

        return v;
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }


}
