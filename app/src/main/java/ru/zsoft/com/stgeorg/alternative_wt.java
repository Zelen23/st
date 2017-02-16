package ru.zsoft.com.stgeorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11.01.2017.
 */

public class alternative_wt extends AppCompatActivity {
    /* имею нач знач и кон значения
    * 7:00 и 23:00
    * если массив данных пуст то вывожу широкую строку с интервалом 7 00-23 00
    * если есть запись то  сравниваю время с нач знач если== то вывожу первым если нет то
    * добавляю пустую строку  с интервалом нач знач до первого времени
    *
    * вывел первую строку для вывода второй проверяю конеуц первой и начало второй
    * если ==то вывожу второй если нет то вывожу строку с интервалом конец второй начало первой */
    int weight=320;
    ListView lw;


    String sFt="07:00";
    String sEn="23:00";
    ArrayList<String> input =new ArrayList<>();
    // ArrayList<String> data =new ArrayList<>();


    void set_test(List<String> data)  {
        if (data.size()<=3) {
            input.add(sFt+"-"+sEn);
            Log.i("set_test",input.toString());
        }else {
            int i = 0;
            for (String elt : data) {

                if (i % 4 == 0) {
                    if (i == 0) {
                        if (sFt == data.get(0)) {
                            input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                        } else {
                            input.add(sFt + "-" + data.get(i) +"---------------");
                            input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                        }
                    } else {
                    }
                    if (i > 0) {
                        if (data.get(i) != (data.get(i - 3))) {
                            input.add(data.get(i - 3) + "-" + data.get(i) +"---------------");
                            input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                        } else
                            input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                    }
                }
                i++;

            }
            input.add(data.get(data.size()-3) + "-"+sEn+"---------------");
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, input) {
            // адаптер устанавливает ширину строк
                /*  есть индекс к высоты
                * если запись в инпуте одна то шо высота строки рова индексу
                * если сток 10 и они ровны между собой то все рорвны по 1/10
                * высота устанавливается в соответствии с весом строки*/
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // здесь не дата а массив весов строки
                try {
                    for (int i = 0; i < convert(ex_time(input)).size(); i++) {
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        if (position == i) {
                            // если позиция ==номеру строки то ставлю нн высоту
                            // если разница во времени<=20мин то всеравно ставлю 20
                            if (convert(ex_time(input)).get(i) <= 20) {
                                ((TextView) view).setHeight(20);
                                ((TextView) view).setMinimumHeight(20);
                            } else
                                ((TextView) view).setHeight(convert(ex_time(input)).get(i));
                            ((TextView) view).setMinimumHeight(convert(ex_time(input)).get(i));
                            // ((TextView) view).setText(String.valueOf(set_weight(input)[i]));
                            return view;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((TextView) view).setHeight(20);
                ((TextView) view).setMinimumHeight(20);
                return view;
            }
        };
        Log.i("set_test",input.toString());
        lw.setAdapter(adapter);
    }

    ArrayList<String> ex_time(ArrayList input){
        int i=0;
        ArrayList<String>time=new ArrayList<>();
        for (Object elt:input){
            String[] sub_input=elt.toString().split("  ");
            time.add(sub_input[0]);
        }


        Log.i("ex_time",time.toString());

        return time;
    }

    ArrayList<Integer> convert(ArrayList time) throws ParseException {

        /* разниу втроого и перврго
        * зарисыва*/

        SimpleDateFormat e_time=new SimpleDateFormat("HH:mm");
        String strTime = e_time.format(new Date());

        int i=0;
        ArrayList<Integer> delta=new ArrayList<>();
        for(Object elt:time){
            String [] deltatimes=elt.toString().split("-");
            String t1=deltatimes[0];
            String t2=deltatimes[1];
            Date dt1= e_time.parse(t1);
            Date dt2= e_time.parse(t2);
            long minutes = dt2.getTime() - dt1.getTime();
            int deltaminutes = (int) (minutes / (60 * 1000));
            delta.add(deltaminutes);
            i++;

        }
        Log.i("convert",delta.toString());
        return delta;
    }


}
