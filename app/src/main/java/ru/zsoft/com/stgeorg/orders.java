package ru.zsoft.com.stgeorg;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Администратор on 30.04.2016.
 */

/* Автоподстановка реквизитов
*  lisbox услуги(руки, ноги, (руки/ноги) )
*  Добавить реквизит Фамилия и стр vk
*  записывать номер фамилию имя в 2е таблици если нет номера в Клиенте
*
*
*   важно: корректировка на часовой пояс*/

public class orders extends MainActivity {

    EditText name,cont,pay;

    private db mDbHelper;
    NumberPicker h1,h2,m1,m2;
    String times;
    String times2;
    int sh1,sm1,sh2,sm2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        Button bt3;

        name= (EditText) findViewById(R.id.editText7);
        cont= (EditText) findViewById(R.id.editText6);
        pay= (EditText) findViewById(R.id.editText5);
        bt3=(Button)findViewById(R.id.button3);

        Intent intent = getIntent();
        times = intent.getStringExtra("selectionWorld");
        times2 = intent.getStringExtra("selectionWorld2");
        final String seldats=intent.getStringExtra("selectiondates");

        name.setText(intent.getStringExtra("name"));
        cont.setText(intent.getStringExtra("num"));
        pay.setText(intent.getStringExtra("sum"));


        try {
            set_time_to_spiner(times,times2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Button write in db all data
        View.OnClickListener clk3= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         // String seldats="2016-5-3";

                String nameI=name.getText().toString();
                String contI=cont.getText().toString();
                String payI=pay.getText().toString();
                final SimpleDateFormat s_time=new SimpleDateFormat("HH:mm:ss");
                Date d1= null;
                Date d2= null;
                try {
                    d1 = s_time.parse(sh1+":"+sm1+":00");
                    d2 = s_time.parse(sh2+":"+sm2+":00");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String strTime = s_time.format(d1);
                String strTime2 = s_time.format(d2);

                Log.i("time_for",strTime+"__"+strTime2);
                Log.i("selectionWorld_ord","*********"+ times);
                Log.i("selectiondates_ord", "*********" + seldats);


                if(strTime.equals(strTime2)){

                    Toast.makeText(getApplicationContext(),
                            "косяк: " + "Время начала равно\n времени окончала",
                            Toast.LENGTH_LONG).show();
                }else
                write_orders(orders.this,seldats,strTime,strTime2,payI,nameI,contI,"clients","");

                Intent intent_m = new Intent(orders.this, MainActivity.class);
                intent_m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_m);

                Intent intent2 = new Intent(orders.this, windows_tables.class);
                intent2.putExtra("selectiondates", seldats);

                finish();
                startActivity(intent2);

            }
        };
        bt3.setOnClickListener(clk3);
    }
    // пишу в базу(дата/время1/время2/сумма/имя/Номер/таблица/ид)
    public  void  write_orders(Context context, String dats, String times, String times2,String pays,String names, String conts,String table,String id){

        Date now =Calendar.getInstance().getTime();
        String nowDate;
        nowDate = String.valueOf(now);

        mDbHelper = new db(context);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        ContentValues val = new ContentValues();

        switch(table){
            case "clients":
                val.put(db.DATE1_COLUMN, nowDate);
                val.put(db.CONTACT_COLUMN, conts);
                val.put(db.NAME_COLUMN, names);
                val.put(db.PAY_COLUMN,pays);
                val.put(db.TIME1_COLUMN, times);
                val.put(db.TIME2_COLUMN, times2);
                val.put(db.DATE_COLUMN, dats);
                break;
            case "temp":

                val.put(db.DATE1_TEMP, nowDate);
                val.put(db.CONTACT_TEMP, conts);
                val.put(db.NAME_TEMP, names);
                val.put(db.PAY_TEMP,pays);
                val.put(db.TIME1_TEMP, times);
                val.put(db.TIME2_TEMP, times2);
                val.put(db.DATE_TEMP, dats);
                val.put(db.ID_TEMP, id);
                break;
        }
        db1.insert(table, null, val);
        Log.i("insert", "selectedDate_ord" + table);
        Log.i("time_ord", "*********" + nowDate);
        db1.close();
    }
    //convert time to spinner
    void set_time_to_spiner(String time,String time2)throws ParseException {

        h1=(NumberPicker) findViewById(R.id.hourse1);
        h2=(NumberPicker) findViewById(R.id.hourse2);

        m1=(NumberPicker) findViewById(R.id.minutes1);
        m2=(NumberPicker) findViewById(R.id.minute2);

        // Log.i("time_to_spinner",String.valueOf());
        final SimpleDateFormat e_time=new SimpleDateFormat("HH:mm");
        final SimpleDateFormat s_time=new SimpleDateFormat("HH:mm");
     //   String strTime = e_time.format(new Date());
        Date dt1= e_time.parse(time);
        final Date dt2= e_time.parse(time2);
        final int th1=dt1.getHours();
        final int th2=dt2.getHours();
        final int tm1=dt1.getMinutes();
        final int tm2=dt2.getMinutes();
        Log.i("time_to_spinner",String.valueOf(th1)+"   "+String.valueOf(th2));
      //  long minutes = dt2.getTime() - dt1.getTime();
     //   int deltaminutes = (int) (minutes / (60 * 1000));

     //   final String s1,s2;
    // установить минимальное значение равное часу t1 t2
// api23 выебывается
        h1.setMinValue(th1);
        h1.setMaxValue(th2);

        m1.setMinValue(tm1);
        m1.setMaxValue(59);

            sm1=m1.getValue();
            sh1=h1.getValue();

        h2.setMinValue(th1);
        h2.setMaxValue(th2);
        h2.setValue(th1+1);

        //если значение часа окончания = максимальному то от нуля до минуты окончания
           if(h2.getValue()<th2){
               m2.setMinValue(0);
               m2.setMaxValue(59);
           }else{
               m2.setMinValue(0);
               m2.setMaxValue(tm2);
           }


        sm2=m2.getValue();
        sh2=h2.getValue();


     //   stime1=e_time.parse(String.valueOf(h1.getValue()+":"+m1.getValue()));
      //  stime2=e_time.parse(String.valueOf(h2.getValue()+":"+m2.getValue()));

        h1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
               @Override
               public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
// если час окончания меньше или равен выбранному часу начала
                   h2.setMinValue(h1.getValue());
                   h2.setMaxValue(th2);
                   h2.setValue(h1.getValue()+1);


                   if(h1.getValue()==th1) {
                       m1.setMinValue(tm1);
                       m1.setMaxValue(59);

                   }


                   if(h1.getValue()==th2){
                       m2.setMinValue(0);
                       m2.setMaxValue(tm2);

                       m1.setMinValue(0);
                       m1.setMaxValue(tm2);

                   }else{
                       m1.setMinValue(0);
                       m1.setMaxValue(59);
                   }
                       if(h1.getValue()==th2-1) {
                           m1.setMinValue(0);
                           m1.setMaxValue(59);

                           m2.setMinValue(0);
                           m2.setMaxValue(tm2);
                       }

                   sh1=h1.getValue();
                   sh2=h2.getValue();

               }


           });

        m1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //не проверенно
                if(h1.getValue()==h2.getValue()&&h1.getValue()!=th2){
                    m2.setMinValue(m1.getValue());
                    m2.setMaxValue(59);
                }
                sm1=m1.getValue();
            }
        });

        h2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
// если час последний то от нуля до последней минуты если нет от от0 до 50
                if(h2.getValue()==th2){
                    m2.setMinValue(0);
                    m2.setMaxValue(tm2);
                }else{
                    m2.setMinValue(0);
                    m2.setMaxValue(59);
                }

                if(h1.getValue()==h2.getValue()&&h1.getValue()!=th2){
                    m2.setMinValue(m1.getValue());
                    m2.setMaxValue(59);
                }
                sh2=h2.getValue();
            }

        });

        m2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                sm2=m2.getValue();
            }

        });




    }



}
