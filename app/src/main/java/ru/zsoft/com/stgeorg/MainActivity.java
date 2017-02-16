package ru.zsoft.com.stgeorg;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // месяц писать в 2х значном формате т.е с "0"
    // если ячейка пустая то не выводить пустой грид
    GregorianCalendar cld = new GregorianCalendar();
    ArrayList<Integer> i_ar=new ArrayList<Integer>();
    GridView gv, gv2;
            //gv3;
    Button bt, bt2;
    ListView lv_f;
    TextView t;
    Spinner sp_mns, sp_year;

    int month;
    int d;
    int max;

    public ArrayAdapter<String> ad_c;
    ArrayAdapter<String> adapter3;
    String inf_ord_o;
    String time_;
    public static String selectedDate;
    private db mDbHelper;
    public static final int IDM_GET_INFO = 106;
    public static final int IDM_CALL = 107;
    // public static int I_YEAR;
    public static int yyyy;
    Intent intent;
    boolean ff = false;
    public static final String[] s_year = {"2016", "2017", "2018"};
    ArrayList<Lines_data> ln_d = new ArrayList<Lines_data>();
    GregorianCalendar cl = new GregorianCalendar();

    List<String> mn_ = new ArrayList<String>();
    List<Integer> cl_ = new ArrayList<Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        /*месяц,
        получаю день по счету
        получаю дней в месяце
        запролняю временный массив прибавив смещение дня по счету*/

        /*выводим в текст текуший месяц и выставляем его в календаре
        * счетчики-батоны
        * ими плюсуем месяцы и отнимаем на них же и слушатели передающие в календарь*/

        t = (TextView) findViewById(R.id.textView);
        gv = (GridView) findViewById(R.id.gridView);
        gv2 = (GridView) findViewById(R.id.gridView2);
       // gv3 = (GridView) findViewById(R.id.gridView3);
        bt = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        sp_mns = (Spinner) findViewById(R.id.spinner2);
        sp_year = (Spinner) findViewById(R.id.spinner3);
        lv_f = (ListView) findViewById(R.id.lv_front);
        registerForContextMenu(lv_f);
        lv_f.setOnCreateContextMenuListener(this);

        // Set_year();
        // index();
        //  yyyy=cld.get(Calendar.YEAR);
        //gv3


        //month-day(для  рендера даты)


        //month in render (and year)
        day();
        final common mnth = new common();
        // кнопки
        View.OnClickListener clk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month + 1;
                if (month <= 11)
                    t.setText(mnth.s_month[month]);
                else {
                    month = cld.get(Calendar.MONTH);
                }
                // grv(month,I_YEAR);
                sp_mns.setSelection(month);
                grv(month, yyyy);
            }
        };

        View.OnClickListener clk2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = month - 1;
                if (month >= 0)
                    t.setText(mnth.s_month[month]);
                else {
                    month = cld.get(Calendar.MONTH);
                }
                // grv(month,I_YEAR);
                sp_mns.setSelection(month);
                grv(month, yyyy);
            }
        };

        bt.setOnClickListener(clk);
        bt2.setOnClickListener(clk2);

        rr();
        vv();
        // че то мутное
        i_ar.add(0);
        i_ar.add(2);

        // grv(cld.get(Calendar.MONTH),cld.get(Calendar.YEAR));
        // sp_mns.setSelection(cld.get(Calendar.MONTH));

    }

    // создаю календарь и крашу его(вынести работу с базой)
    // grv лютый его нужно разбить как минимум вывксти из грв запросы базы
    public void grv(final int month, int y) {

        final String[] name = {"пн", "вт", "ср", "чт", "пт", "сб", "вс"};
        //cld.getInstance().setFirstDayOfWeek(Calendar.MONDAY);
        /*
        cld.set(Calendar.YEAR,I_YEAR);
        cld.set(Calendar.MONTH,month);
        cld.set(Calendar.DAY_OF_MONTH, 1);
        */
        cld.set(y, month, 1);
        d = cld.get(Calendar.DAY_OF_WEEK);
        max = cld.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] ms = new String[50];
        final List<String> list_d = new ArrayList<>();

//из нетбина календарь
        int in = 1;
        for (int ir = 1; ir < 49; ir++) {

            if (in <= max) {
                if (d == 1) {
                    Log.i("d_grv_if=1", String.valueOf(d));
            /*то в массив первые 6 позиций пишем null*/
                    if (ir >= d + 6) {
                        ms[ir] = String.valueOf(in++);
                    }

                } else if (ir >= d - 1) {
                    Log.i("d_grv_if!=1", String.valueOf(d));
                    ms[ir] = String.valueOf(in++);
                }
            }
            if (ms[ir] == null) {
                list_d.add(" ");
            } else {
                list_d.add(String.valueOf(ms[ir]));
            }
        }
        Log.i("grv_data", String.valueOf(cld.get(Calendar.MONTH)));
        Log.i("1_debug", String.valueOf(cld.get(Calendar.MONTH) + "--" + String.valueOf(max) + "---")
                + String.valueOf(d) + "---" + String.valueOf(cld.get(Calendar.YEAR)));
        Log.i("1_debug_2", list_d.toString());
//Раскрасил ячейки!!!
// распарсить не все а на выбранный месяц

        init_mass(month,y);


//renderer cells

        ad_c = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_d) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                View view = super.getView(position, convertView, parent);

               /*лист позиция
               закрасить позицию с номером из лмсиа
               position
               *     if(position==list_d.indexOf(" ")){

                view.setBackgroundColor(Color.GRAY);
                 return view;
}
*/
//Text Size

                ((TextView) view).setTextSize(12);
                int n = 0;

//Show_toDay
// год
                if (day()[2].contains(String.valueOf(yyyy)))
                    if (position == list_d.indexOf(day()[0]) && month == Integer.parseInt(day()[1])) {

                        ((TextView) view).setTextSize(12);
                        ((TextView) view).setTextColor(Color.BLUE);
                    }
                {
                    int color = 0x00FFFFFF; // Transparent

                    for (int i = 0; i < mn_.size(); i++) {

                        n = list_d.indexOf(mn_.get(i));

                        if (position == n) {

                            switch (cl_.get(i)){
                                case 1:
                                    color = Color.rgb(222,241,232);
                                    break;
                                case 2:
                                    color =Color.rgb(222,241,232);
                                    break;
                                case 3:
                                    color =Color.rgb(222,241,232);
                                    break;

                                case 4:
                                    color =Color.rgb(181,223,201);
                                    break;
                                case 5:
                                    color =Color.rgb(181,223,201);
                                    break;
                                case 6:
                                    color =Color.rgb(181,223,201);
                                    break;

                                case 7:
                                    color =Color.rgb(113,193,151);
                                    break;
                                case 8:
                                    color =Color.rgb(113,193,151);
                                    break;
                                case 9:
                                    color =Color.rgb(113,193,151);
                                    break;

                                case 10:
                                    color =Color.rgb(78,163,148);
                                    break;
                                case 11:
                                    color =Color.rgb(78,163,148);
                                    break;
                                case 12:
                                    color =Color.rgb(78,163,148);
                                    break;
                                // подправиить
                                case 13:
                                    color =Color.rgb(73,158,122);
                                    break;
                                case 14:
                                    color =Color.rgb(73,158,122);
                                    break;

                                case 15:
                                    color =Color.rgb(233,84,88);
                                    break;
                                case 16:
                                    color =Color.rgb(233,84,88);
                                    break;
                                case 18:
                                    color =Color.rgb(233,84,88);
                                    break;


                                case 20:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 21:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 22:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 23:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 24:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 25:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 26:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 27:
                                    color =Color.rgb(250,149,105);
                                    break;
                                case 28:
                                    color =Color.rgb(250,149,105);
                                    break;
                            }

                            view.setBackgroundColor(color);
                            return view;
                        }

                    }
                    /*
                    for (int j = 0; j <list_d.size(); j++)
                        if (position ==j) {
                            view.setBackgroundColor(Color.GRAY);
                            return view;
                        } else {
                        */
                            view.setBackgroundColor(color);
                  //      }


                    return view;
                }
            }


        };
        gv.setAdapter(ad_c);


        //Long clicker get Windows_table
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ad_c.getItem(position);
                selectedDate = String.valueOf(cld.get(Calendar.YEAR)) + "-" + month + "-" + s;
                Log.i("1_am a longClick", selectedDate);
                Intent intent2 = new Intent(MainActivity.this, windows_tables.class);
                intent2.putExtra("selectiondates", selectedDate);
                startActivity(intent2);

                return false;
            }
        });

//Click_clic get list day
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = ad_c.getItem(position);
                selectedDate = String.valueOf(cld.get(Calendar.YEAR)) + "-" + month + "-" + s;
                //2101  day_ord(selectedDate);
                windows_tables rd = new windows_tables();
                rd.set_test(rd.read2(MainActivity.this, selectedDate));

                //Adapter MyAdapter_= new Adapter_(MainActivity.this,rd.liner);
                // lv_f.setAdapter((ListAdapter) MyAdapter_);
                Ads Adapt = new Ads(MainActivity.this, rd.liner);
                lv_f.setAdapter(Adapt);
                ln_d = rd.liner;

                Log.i("1_SelectedDate", selectedDate + "  id" + id + "pos");
            }


        });
//шапка день недели
        final ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, name);
        gv2.setAdapter(adapter2);
    }


    // Разбор текущей даты для проверок
    String[] day() {
//month-day
        GregorianCalendar cld_m = new GregorianCalendar();
        String day = cld_m.getTime().toString();
        String[] getday = day.split(" ", 5);
        int mn = cld_m.get(Calendar.MONTH);
        String[] show_day = new String[3];
        show_day[0] = String.valueOf(Integer.parseInt(getday[2]));
        show_day[1] = String.valueOf(mn);
        show_day[2] = String.valueOf(getday[4]);

        // Log.i ("1_day****", String.valueOf(cld_m.get(Calendar.YEAR)));

        Log.i("1_day**", getday[2]);
        Log.i("1_day-----mns", getday[1]);
        Log.i("1_day*", String.valueOf(mn));
        Log.i("1_day***", String.valueOf(month));
        Log.i("1_day~~", String.valueOf(Integer.parseInt(getday[2])));


        return show_day;
    }
    //индекс текущего года в массиве
    public int index() {
        Log.i("im_year", String.valueOf(cld.get(Calendar.YEAR)));
        int k = 0;
        for (int i = 0; i < s_year.length; i++) {
            if (s_year[i].equals(String.valueOf(cld.get(Calendar.YEAR)))) {
                Log.i("im_year", s_year[i]);
                k = i;
            }
        }
        return k;
    }
    // месяц в спиннер
    void rr() {
        Context ct = this;
        sp_mns.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, common.s_month));
        sp_year.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, s_year));
        sp_mns.setSelection(0, true);
        View view = (View) sp_mns.getChildAt(0);
        long id = sp_mns.getAdapter().getItemId(0);
        sp_mns.performItemClick(view, 0, id);
        sp_mns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                grv(Integer.parseInt(String.valueOf(sp_mns.getSelectedItemId())),
                        Integer.parseInt(sp_year.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // GregorianCalendar cl= new GregorianCalendar();
        // mns.setText(String.valueOf(cl.get(Calendar.MONTH)));
        // year.setText(String.valueOf(cl.get(Calendar.YEAR)));

    }
    // год в спиннер
    void vv() {
        Context ct = this;
        //  sp_mns.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,common.s_month ));
        sp_year.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, s_year));
        sp_year.setSelection(0, true);
        View view = (View) sp_year.getChildAt(0);
        long id = sp_year.getAdapter().getItemId(0);
        sp_year.performItemClick(view, 0, id);
        sp_year.setSelection(index());


        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ya = cl.get(Calendar.YEAR);
                int y1 = Integer.parseInt(sp_year.getSelectedItem().toString());

                Log.i("vv_focus1", String.valueOf(ya) + "====" + y1);

                //если  выбранный год==текущему то текущий месяц
                if (y1 == ya) {
                    sp_mns.setSelection(Integer.parseInt(day()[1]));
                } else {
                    sp_mns.setSelection(0);
                }


                yyyy = Integer.parseInt(sp_year.getSelectedItem().toString());
                grv(0,
                        Integer.parseInt(sp_year.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        GregorianCalendar cl = new GregorianCalendar();
        // mns.setText(String.valueOf(cl.get(Calendar.MONTH)));
        // year.setText(String.valueOf(cl.get(Calendar.YEAR)));

    }


    // массивы для раскраски
    void init_mass(int month,int y){
        mn_  = new ArrayList<String>();
        cl_ = new ArrayList<Integer>();
        mDbHelper = new db(getApplicationContext());
        //Application did not close the cursor or database object that was opened here
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
// показывать только на выбранный месяц!!
//*создать два массива записи в день и цвет дня */
///*Запрос из истории*/


        Cursor c = db1.rawQuery("SELECT * FROM history where date " +
                "like '" + y + "-" + month + "-%'", null);

        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int date = c.getColumnIndex("date");
            int d_count = c.getColumnIndex("d_count");
// Привести в проядок
            do {
//распарсить дату до числа 2016-6-17
                String d = c.getString(date).toString();
                String[] pd = d.split("-", 3);
                //mn_ day in history_for_fender
                mn_.add(pd[2]);
                //cl_ weight of gay(color_weight)
                cl_.add(c.getInt(d_count));
            }
            while (c.moveToNext());
            c.close();

        }
        Log.i("1_mn_", String.valueOf(mn_.size()));
        Log.i("1_mn", String.valueOf(mn_));
        Log.i("1_cl_", String.valueOf(cl_));

    }

    // settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent = new Intent(MainActivity.this, common.class);
            intent.putExtra("year_inSettigs", yyyy);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // context_menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        windows_tables wt_line = new windows_tables();

        if (ln_d.get(info.position).name.equals("_")) {

        } else {
            // String  selectedWord = wt_line.liner.get(info.position).time;
            menu.setHeaderTitle(ln_d.get(info.position).time + "  " + ln_d.get(info.position).name);
            menu.add(menu.NONE, IDM_GET_INFO, menu.NONE, "Информация");
            menu.add(menu.NONE, IDM_CALL, menu.NONE, "Позвонить");

            inf_ord_o = ln_d.get(info.position).name;
            time_ = ln_d.get(info.position).time.substring(0, 5) + ":00";
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case IDM_GET_INFO:
                windows_tables wt = new windows_tables();
                windows_tables wt2 = new windows_tables();
                // гет нуи по ид getRow_id(item1)
                wt.getinf(this, wt2.getnum(this, inf_ord_o, selectedDate, time_));

                Log.i("2_get_n", time_ + "---" + inf_ord_o);
                break;
            case IDM_CALL:
                windows_tables wt3 = new windows_tables();
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse
                        ("tel:" + wt3.getnum(this, inf_ord_o, selectedDate, time_)));
                startActivity(dialIntent);
        }

        return super.onContextItemSelected(item);
    }

    // не используется брал все записи и выводил их на фронт
    List<String> day_ord(String selectedDate) {
        List<String> day_o = new ArrayList<String>();
        if (selectedDate != null) {

            mDbHelper = new db(getApplicationContext());
            mDbHelper.getWritableDatabase();
            SQLiteDatabase db2 = mDbHelper.getWritableDatabase();


            Cursor c = db2.rawQuery("SELECT * FROM clients where date like '" + selectedDate + "'" + "order by time1 asc", null);

            if (c.moveToFirst()) {
                int time1 = c.getColumnIndex("time1");
                int name = c.getColumnIndex("name");
                int pay = c.getColumnIndex("pay");

                do {
                    day_o.add(c.getString(time1).toString());
                    day_o.add(c.getString(name).toString());
                    day_o.add(c.getString(pay).toString());
                }

                while (c.moveToNext());
                c.close();

                Log.i("1_day_o", String.valueOf(day_o));
                adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, day_o);
            } else {
                List<String> nl = new ArrayList<String>();
                adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nl);
            }
            //gv3.setAdapter(adapter3);
        }
        return day_o;
    }
    //
    void silent(){

        for(int j=0;j<ad_c.getCount();j++){
            if(ad_c.getItem(j)==" "){
                i_ar.add(j);
            }

        }
        //  Log.i("silent",i_ar.toString());
    }
    //
    public void Spin_m(int mn) {
        grv(cld.get(Calendar.MONTH), Calendar.YEAR);

        ArrayAdapter ad_mns = new ArrayAdapter(this, android.R.layout.simple_list_item_1, common.s_month);
        sp_mns.setAdapter(ad_mns);
        sp_mns.setSelection(0, true);
        View view = (View) sp_mns.getChildAt(0);
        long id = sp_mns.getAdapter().getItemId(0);
        sp_mns.performItemClick(view, 0, id);
        sp_mns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t.setText(common.s_month[position]);
                cld.set(Calendar.MONTH, position);
                //sp_year.setSelection(getindex());
                //  sp_mns.setSelection(position);
                grv(position, yyyy);
                Log.i("1_list", String.valueOf(position) + "---" + String.valueOf(id) + "--");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }





}
