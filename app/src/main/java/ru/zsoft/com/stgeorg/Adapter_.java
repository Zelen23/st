package ru.zsoft.com.stgeorg;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 14.01.2017.
 */

public class Adapter_ extends ArrayAdapter<Lines_data> {
    private final Activity context;
    private  final ArrayList<Lines_data> data;
    Context ct;
    //=new ArrayList<String>();

    public Adapter_(Activity context, ArrayList<Lines_data> data) {
        super(context, R.layout.row, data);
        this.context = context;
        this.data=data;


    }
    static class ViewHolder {

        public TextView names,time;
        public CheckBox chek;
        public EditText edit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // ViewHolder буферизирует оценку различных полей шаблона элемента
       final ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row, null, true);
            holder = new ViewHolder();
           // final ViewHolder holder = new ViewHolder();

            holder.names = (TextView) rowView.findViewById(R.id.name);
            holder.time = (TextView) rowView.findViewById(R.id.time);
            holder.chek = (CheckBox) rowView.findViewById(R.id.checkbox);
            holder.edit = (EditText) rowView.findViewById(R.id.edSum);
//
            holder.names.setText(data.get(position).name);
            holder.time.setText(data.get(position).time);
            holder.edit.setText(String.valueOf(data.get(position).sum));

            if(data.get(position).name=="_"){
                holder.chek.setClickable(false);
            }else{
                holder.chek.setChecked(data.get(position).flag);
            }

            holder.chek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    final boolean isChecked = holder.chek.isChecked();
                    Log.i("chek0nadapter",String.valueOf(isChecked)+" "+data.get(position).id);
                      // execute.fag_vcisit(this,String.valueOf(isChecked),data.get(position).name,data.get(position).name);
                    // Store the boolean value somewhere durable
                       if(data.get(position).id!="") {
                           execute vis = new execute();
                           vis.flag_visit(context, String.valueOf(isChecked),
                           data.get(position).id,"visit");
                       }else{

                       }
                    }

            });
            String sss="";

            holder.edit.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //   if(holder.edit.getText().length()>2)
                    Log.i("edit0nadapter", holder.edit.getText() + "~~~" + data.get(position).id);
                    //   if(data.get(position).id!="") {
                    execute vis = new execute();
                                            //Adapter_.this
                    vis.flag_visit(context,holder.edit.getText().toString() ,
                            data.get(position).id,"pay");
                    //  }

                }

            });
            rowView.setTag(holder);
          // return rowView;

        }
        else{

           holder = (ViewHolder) convertView.getTag();
        }

//26/11
       final ViewHolder holders = (ViewHolder) rowView.getTag();

        holders.names.setText(data.get(position).name);
        holders.time.setText(data.get(position).time);
        holders.edit.setText(String.valueOf(data.get(position).sum));
        holders.chek.setChecked(data.get(position).flag);






        return rowView;
    }




}
