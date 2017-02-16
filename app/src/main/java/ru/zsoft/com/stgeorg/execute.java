package ru.zsoft.com.stgeorg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*вся запись  в базу через этот класс*/

public class execute extends MainActivity {

    private db mDbHelper;
    List<String> l_time = new ArrayList<>();
    ArrayList<String> line= new ArrayList<>();

    //пишу в очередь
    public   void write_in(Context ct, String table, List<String> l_time, String date, String name, String num, String comm){

        ContentValues val = new ContentValues();
        mDbHelper = new db(ct);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();

        for(int i=0;i<l_time.size();i++) {

            val.put(db.qu_name, name);
            val.put(db.qu_num, num);
            val.put(db.qu_interval_t, l_time.get(i));
            // val.put(db.qu_interval_t,":00:00");
            val.put(db.qu_interval_date, date);

            db1.insert(table, null, val);
            Log.i("execute", "write_in**"+l_time.get(i) +"__"+ date);
        }

        db1.close();
    }

    //читать из очереди пл дате*
    public List<String> read_in(Context ct, String dat){

        List<String> queue = new ArrayList<>();

        mDbHelper = new db(ct);
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
        Cursor c = db2.rawQuery("SELECT * FROM queue_user where date like'"+dat+"'  order by _id DESC ", null);

        if (c.moveToFirst()) {

            int name = c.getColumnIndex("name");
            int time = c.getColumnIndex("time1");
            int date = c.getColumnIndex("date");
            int num = c.getColumnIndex("num");

            do {
                queue.add(c.getString(name));
                queue.add(c.getString(time));
                queue.add(c.getString(date));
                queue.add(c.getString(num));

                l_time.add(c.getString(time));
            }

            while (c.moveToNext());
            c.close();

        }
        Log.i("4_",String.valueOf(queue));
        Log.i("4_",dat);
        return queue;
    }

    // поиск (приходит запрос целиком)
    public List<String> search(Context ct, String dat){

        List<String> queue = new ArrayList<String>();

        mDbHelper = new db(ct);
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
        Cursor c = db2.rawQuery(dat, null);

        if (c.moveToFirst()) {

            int name = c.getColumnIndex("name");
            int time = c.getColumnIndex("time1");
            int date = c.getColumnIndex("date");
           // int num = c.getColumnIndex("sf_num");
           // int pay = c.getColumnIndex("pay");

            do {
                queue.add(c.getString(name));
                queue.add(c.getString(time));
                queue.add(c.getString(date));
              //  queue.add(c.getString(num).toString());
              //  queue.add(c.getString(pay).toString());
            }

            while (c.moveToNext());
            c.close();
        }
        Log.i("4_",dat);
        Log.i("4_",String.valueOf(queue));
        return queue;
    }

    //поиск по номеру* в пользователях
    public ArrayList<String> getLine_(Context context, String s){
       /*вывод диалогового окна с деталями по клиенту
       * для теста номер*/
        ArrayList<String> line= new ArrayList<>();
        String mesages;
// контекст из метода
        mDbHelper = new db(context);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        Cursor c = db1.rawQuery("SELECT * FROM user where pk_num= '" +s+ "' ", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int pk_num = c.getColumnIndex("pk_num");
            int name = c.getColumnIndex("name");
            int family = c.getColumnIndex("family");
            int url = c.getColumnIndex("url");
            int count = c.getColumnIndex("count");
            int last = c.getColumnIndex("last");

            do {
                line.add(c.getString(id));
                line.add(c.getString(name));
                line.add(c.getString(family));
                line.add(c.getString(pk_num));
                line.add(c.getString(url));
                line.add(c.getString(last));
                line.add(c.getString(count));


                // не this а контекст если передаю контекст из др метода

            }
            while (c.moveToNext());
            c.close();

        }
        return line;
    }

    //поиск в таблицах* по _id*
    ArrayList<String> getRow_id (Context ct, String table,String s_id){

        String mesages;
// контекст из метода
        mDbHelper = new db(ct);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        Cursor c = db1.rawQuery("SELECT * FROM "+table+" where _id= '" +s_id+ "' ", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("_id");
            int sf_num = c.getColumnIndex("sf_num");
            int name = c.getColumnIndex("name");
            int time1 = c.getColumnIndex("time1");
            int time2 = c.getColumnIndex("time2");
            int pay = c.getColumnIndex("pay");
            int date = c.getColumnIndex("date");
            int date1 = c.getColumnIndex("date1");
            int visit = c.getColumnIndex("visit");

            do {
                line.add(c.getString(id));
                line.add(c.getString(name));
                line.add(c.getString(time1));
                line.add(c.getString(time2));
                line.add(c.getString(sf_num));
                line.add(c.getString(pay));
                line.add(c.getString(date));
                line.add(c.getString(date1));
                line.add(c.getString(visit));


                // не this а контекст если передаю контекст из др метода

            }
            while (c.moveToNext());
            c.close();

        }
      Log.i("ex_read",line.toString());
        return line;

    }

    // пишу из редактора  в пользователи
    public void redact_user(Context ct,ArrayList<String> newline){

        ContentValues val = new ContentValues();
        mDbHelper = new db(ct);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();

        val.put(db.name_us,newline.get(1));
        val.put(db.family_us, newline.get(2));
        val.put(db.pk_num_us,newline.get(3));
        val.put(db.url_us, newline.get(4));

        db1.update("user",val,"id = '"+newline.get(0)+"'",null);

        Log.i("execute_redact","--");
        db1.close();
    }

    // обновляю из адаптера редакт клиентов (сумма/галка)
    public  void flag_visit(Context ct, String flag, String id,String coloumn){


        ContentValues val = new ContentValues();
        mDbHelper = new db(ct);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        switch(coloumn){
            case "visit":
                val.put(db.VISIT_COLUMN,flag);
                break;
            case "pay":
                val.put(db.PAY_COLUMN,flag);
                break;
        }

        db1.update("clients",val,"_id = '"+id+"'",null);

        Log.i("execute_visit",val.toString());

        db1.close();
    }

    // читать из вреиенной таблицы
    public List<String> read_temp(Context ct) {
//ddat="2016-4-12";
        // read db
        List<String> sqldat = new ArrayList<>();
        mDbHelper = new db(ct);
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        //db1.execSQL("SELECT * FROM clients where date like '"+ddat+"%'");
        //Cursor c = db1.query("clients",null,null,null,null,null,null);
        // Cursor c = db1.rawQuery("SELECT * FROM clients where date like '" + ddat + "%'", null);
        Cursor c = db1.rawQuery("SELECT * FROM temp ", null);
        if (c.moveToFirst()) {
           // int id = c.getColumnIndex("_id");
            int name = c.getColumnIndex("name");
            int time1 = c.getColumnIndex("time1");
            int time2 = c.getColumnIndex("time2");
            int sf_num = c.getColumnIndex("sf_num");
            int pay = c.getColumnIndex("pay");
            int date = c.getColumnIndex("date");
            int date1 = c.getColumnIndex("date1");
            int visit = c.getColumnIndex("visit");

            do {
              //  sqldat.add(  c.getString(id).toString() );
                sqldat.add( c.getString(name));
                sqldat.add( c.getString(time1));
                sqldat.add( c.getString(time2));
                sqldat.add( c.getString(sf_num));
                sqldat.add( c.getString(pay) );
                sqldat.add( c.getString(date));
                sqldat.add(c.getString(date1) );
                sqldat.add(c.getString(visit));
                // + c.getString(date).toString());
            }

            while (c.moveToNext());
            c.close();
           db1.close();
        }
        Log.i("temp", String.valueOf(sqldat));
        return sqldat;
    }

    // читаю из истории
    public List<String> read() {
        if(MainActivity.yyyy==0){
            this.yyyy=cld.get(Calendar.YEAR);
        }
        //ddat="2016-4-12";
        // read db

        List<String> mn = new ArrayList<String>();
        List<String> cl = new ArrayList<String>();
        List<String> sqldat = new ArrayList<String>();
        mDbHelper = new db(getApplicationContext());
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();

        Cursor c = db1.rawQuery("SELECT * FROM history where date like '"+String.valueOf(MainActivity.yyyy)+"-%' ", null);

        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int date = c.getColumnIndex("date");
            int d_count = c.getColumnIndex("d_count");

            do {
                sqldat.add(c.getString(id)+ "  дата: "
                        + c.getString(date) + "  записей на дату: "
                        + c.getString(d_count));

//распарсить дату до числа 2016-6-17
                String d = c.getString(date);
                String[] pd = d.split("-", 3);

                mn.add(pd[2]);
                cl.add(c.getString(d_count));

            }
            while (c.moveToNext());
            c.close();
            Log.i("comm_cl", String.valueOf(cl));
            Log.i("comm_mn", String.valueOf(mn));

        }

        return sqldat;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}



