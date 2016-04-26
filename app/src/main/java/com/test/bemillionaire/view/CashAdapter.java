package com.test.bemillionaire.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.bemillionaire.R;

import java.util.ArrayList;


public class CashAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList objects;
    public TextView tv_cash_value,tv_cash_number;

    public CashAdapter(Context context, ArrayList products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_cash, parent, false);
        }

        String cashItem=objects.get(position).toString();
        tv_cash_number=(TextView) view.findViewById(R.id.tv_cash_number);
        tv_cash_value= (TextView) view.findViewById(R.id.tv_cash_value);

        tv_cash_number.setText(String.valueOf(objects.size()-position));


        tv_cash_value.setText("$"+cashItem);
        if(cashItem.equals("1000")||cashItem.equals("32000")||cashItem.equals("1000000")){
            tv_cash_value.setTextColor(ctx.getResources().getColor(R.color.white));
            tv_cash_number.setTextColor(Color.parseColor("#FFFFFF"));
        }


        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
