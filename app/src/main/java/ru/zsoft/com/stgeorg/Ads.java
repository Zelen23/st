package ru.zsoft.com.stgeorg;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 27.01.2017.
 */

public class Ads extends BaseAdapter {
    private final Activity context;
    private ArrayList<Lines_data> data;
    Context ct;
    LayoutInflater inflter;
    //=new ArrayList<String>();

    public Ads(Activity context, ArrayList<Lines_data> data) {
        this.context = context;
        this.data=data;
        inflter = (LayoutInflater.from(context));

    }
    public void updateResults(ArrayList<Lines_data> results) {
    /* обновляю базу
    * из базы получить массив
    * перегнать его в liner
    * data преровнять к нему
    * новый liner=data
    * */

        data = results;
        //Triggers the list update

        notifyDataSetChanged();
    }

    ArrayList<Lines_data> update(){
        windows_tables rd=new windows_tables();
        rd.set_test(rd.read2(context,MainActivity.selectedDate));
        ArrayList<Lines_data>ln_d=new ArrayList<Lines_data>();
        ln_d=rd.liner;
        return ln_d;
    }

    private class ViewHolder {

        public TextView names,time;
        public CheckBox chek;
        public EditText edit;

        public ViewHolder(View rowView ) {
            names = (TextView) rowView.findViewById(R.id.name);
            time = (TextView) rowView.findViewById(R.id.time);
            chek = (CheckBox) rowView.findViewById(R.id.checkbox);
            edit = (EditText) rowView.findViewById(R.id.edSum);

        }
    }


    @Override
    public int getCount() {
        int count=data.size();
        return count;
    }
    @Override
    public Object getItem(int i) {
        return null ;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int i, final View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflter.inflate(R.layout.row, viewGroup, false);
            holder = new ViewHolder(view);


            view.setTag(holder);
            // return view;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //значения

        holder.names.setText(data.get(i).name);
        holder.time.setText(data.get(i).time);
        holder.edit.setText(String.valueOf(data.get(i).sum));
        holder.chek.setChecked(data.get(i).flag);


        holder.chek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer)v.getTag();
                Log.i("chek0nadapter"," "+data.get(i).id+"name "+data.get(i).name+" pos:"+pos);
              // if(data.get(i).name!="_") {
                    if (holder.chek.isChecked()) {
                        execute vis = new execute();
                        vis.flag_visit(context, "true",
                                data.get(i).id, "visit");
                        updateResults(update());
                       // notifyDataSetChanged();

                    }else{
                        execute vis = new execute();
                        vis.flag_visit(context, "false",
                                data.get(i).id, "visit");
                        updateResults(update());

                       // notifyDataSetChanged();
                    }
                //


            }


        });
//notifyDataSetChanged();

        return view;
        }


}
