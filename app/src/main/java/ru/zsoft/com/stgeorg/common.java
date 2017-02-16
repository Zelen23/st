package ru.zsoft.com.stgeorg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/*
* Подсчет записей в текущем месяце поденно
*         Новых клиентов
* общая сумма за месяц
* кликнув день открыть журнал
*               {4 мая------5
                 5мая-----7
                 8мая-----11 выбрали: открылась сумма и имена клиентов }
* вкладка клиенты:
{лист со списком имен и фамилий. Если в Клиенте не заполнен реквизит то .....


записывать номер фамилию имя в 2е таблици если нет номера в Клиенте
*
* */
public class common extends MainActivity {
    /*
    обновляшку окна
    gw4 поменять на выпадающтй список
    * группировка по имени
    *
    * подсветить совпадение времени на дату в win_table*/
    private db mDbHelper;
    ListView lw2;
    ListView lw3;
    EditText tv_name, tv2_num,edit_search;
    GridView gw_t,gw_search;
    ExpandableListView exp;
    NumberPicker np;
    Spinner month,sp_search;
    Button bt_qu,bt_search;
    int np_day, sp_m;
    String  head_gr,head_ch;
    List<String>resultset;
   // String q_year;

    public static final int IDM_DELETE = 102;


    final String ATTR_GROUP_NAME = "groupname";
    final String ATTR_PHONE_NAME = "phoneneame";
   public static final String[] s_month = {"январь", "февраль", "март", "апрель",
                                             "май", "июнь", "июль", "август",
                                         "сентябрь", "октябрь", "ноябрь", "декабрь"};

    Map<String, String> m;

    ArrayList<Map<String, String>> groupData;
    ArrayList<Map<String, String>> childItem;
    ArrayList<ArrayList<Map<String, String>>> childData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
/// очееоедь
        super.onCreate(savedInstanceState);

        Log.i("L_data_yy", String.valueOf(yyyy));
        setContentView(R.layout.common2);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tab1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Журнал");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Клиенты");
        tabHost.addTab(tabSpec);
 /*очередь и настройки*/
        tabSpec = tabHost.newTabSpec("tab3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Очередь");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab4");
        tabSpec.setContent(R.id.tab4);
        tabSpec.setIndicator("Настойки");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        lw3 = (ListView) findViewById(R.id.users);
        lw2 = (ListView) findViewById(R.id.journals);

        //tab2(клиенты)
        ArrayAdapter<String> ad_dat3 = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, userlist());
        lw3.setAdapter(ad_dat3);

        //очеоедь
        queue();
        go_searh();
    }
    // читаю юзер all 30 lim
    List<String> userlist() {
        List<String> uslist = new ArrayList<String>();
        mDbHelper = new db(getApplicationContext());
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
        //db1.execSQL("SELECT * FROM clients where date like '"+ddat+"%'");
        //Cursor c = db1.query("clients",null,null,null,null,null,null);
        // Cursor c = db1.rawQuery("SELECT * FROM clients where date like '" + ddat + "%'", null);
        Cursor c = db2.rawQuery("SELECT * FROM user where last is not null order by count desc limit 30", null);

        if (c.moveToFirst()) {
            int pk_num = c.getColumnIndex("pk_num");
            int name = c.getColumnIndex("name");
            int family = c.getColumnIndex("family");
            int url = c.getColumnIndex("url");
            int count = c.getColumnIndex("count");
            int last = c.getColumnIndex("last");

/*обход нулей прри чтении*/
            do {
                uslist.add(c.getString(pk_num).toString() + "   "
                        + c.getString(name).toString() + "   "
                        //+ c.getString(family).toString() + " "
                        //  + c.getString(url).toString() + " "
                        + c.getString(last).toString() + "  колличество:  "
                        + c.getString(count).toString());
            }

            while (c.moveToNext());

            c.close();
        }
        return uslist;


    }

    // поиск по имени или номеру
    void go_searh(){
     /*  на onCreate  выести все ресурсы
      *  по батон клику запустить метод поиска
      */
        String[]param={"Номер","Имя"};

        bt_search = (Button) findViewById(R.id.button5);
        edit_search = (EditText) findViewById(R.id.search_user);
        gw_search = (GridView) findViewById(R.id.gv_search);
        sp_search=(Spinner)findViewById(R.id.sp_param);

        ArrayAdapter ad_search = new ArrayAdapter(this, android.R.layout.simple_list_item_1, param);
        sp_search.setAdapter(ad_search);

        View.OnClickListener clk=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int attr= (int) sp_search.getSelectedItemId();
                String coloumn = null;
                
                switch (attr) {
                    case 0:
                        coloumn="sf_num";
                break;

                    case 1:
                        coloumn="name";
                break;
                }

                String qu_searsh="select \n" +
                        "       name,    \n" +
                        "       time1,   \n" +
                        "       sf_num,  \n" +
                        "       pay,     \n" +
                        "       date     \n" +
                        "                \n" +
                        "from clients where "+coloumn+" like '%"+edit_search.getText()+"%'";

                execute search_cl=new execute();
                search_cl.search(common.this,qu_searsh);

                resultset= search_cl.search(common.this,qu_searsh);
                final ArrayAdapter res_search = new ArrayAdapter(common.this, android.R.layout.
                                                                 simple_list_item_1,resultset);
                gw_search.setAdapter(res_search);

                Log.i("attr",qu_searsh);
                // Log.i("attr",search_cl.search(common.this,qu_searsh).toString());

            }

            ;
        };
        bt_search.setOnClickListener(clk);




    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {





        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;


        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        int group =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        int child =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        // Only create a context menu for child items
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            // Array created earlier when we built the expandable list
            head_ch = ((TextView) info.targetView).getText().toString();
            menu.setHeaderTitle("-----"+head_ch);
            menu.add(menu.NONE, IDM_DELETE, menu.NONE, "Удалить Запись");

        }
        else if(type == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
            head_gr = ((TextView) info.targetView).getText().toString();
            menu.setHeaderTitle("-----"+head_gr);
            menu.add(menu.NONE, IDM_DELETE, menu.NONE, "Удалить Запись");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        windows_tables del=new windows_tables();

        switch (item.getItemId()) {



            case IDM_DELETE:
                del.deleterow(this,"queue_user",head_gr,"%","");
                queue();
                /*
                Intent i = new Intent( this , this.getClass() );
                finish();
//колхозный запуск активити
                Intent mainUpd=new Intent( this,common.class);
                mainUpd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainUpd);
                */

         break;
        }
                return super.onContextItemSelected(item);
        }

    // не используется очередь
    public void queue() {
        //   gw4 = (GridView) findViewById(R.id.gr_queue);
        gw_t = (GridView) findViewById(R.id.gr_time);
        month = (Spinner) findViewById(R.id.spinner);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        bt_qu = (Button) findViewById(R.id.button4);
        tv_name = (EditText) findViewById(R.id.editText);
        tv2_num = (EditText) findViewById(R.id.editText2);

//        registerForContextMenu(gw_t);
//       gw_t.setOnCreateContextMenuListener(this);

        np.setMinValue(1);
        np.setMaxValue(31);
        //21.12
        np.setValue(Integer.parseInt(day()[0]));
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (newVal != 0) ;
                np_day = newVal;
                Log.i("3_number", String.valueOf(newVal));
            }
        });

        List<String> list_queue = new ArrayList<String>();
        final List<Integer> list_time = new ArrayList<Integer>();
        for (int i = 0; i <= 10; i++) {
            list_queue.add(String.valueOf(i));
        }

        for (int i = 0; i <= 23; i++) {
            list_time.add(i);
        }
        adjustGridView(3, 4);

        final ArrayAdapter<String> gv_queue;
        final ArrayAdapter<Integer> gv_time;
        final ArrayAdapter<String> sp_month;
        final execute queue_real = new execute();
        List<String> list_in_adapter = queue_real.read_in(this, "%");

        //gv_queue = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list_in_adapter );

        gv_time = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_time);
        sp_month = new ArrayAdapter(this, android.R.layout.simple_list_item_1, s_month);
        int ind_spinner;
        // selected month
        final int[] ind_s = new int[1];
        month.setAdapter(sp_month);
        //21.12
        month.setSelection(Integer.parseInt(day()[1]));
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // позишен это индекс месяца, получаю макс кол дей
                GregorianCalendar cld = new GregorianCalendar();
                cld.set(Calendar.MONTH, position);
                int max = cld.getActualMaximum(Calendar.DAY_OF_MONTH);
                sp_m = position;
                np.setMaxValue(max);
                Log.i("3_spinner", String.valueOf(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
//gw4.setAdapter(gv_queue);
        expand((ArrayList<String>) list_in_adapter);

        final List<String> l_time = new ArrayList<String>();
        gw_t.setAdapter(gv_time);
        gw_t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String[] timeline = {"01:00:00", "02:00:00", "03:00:00", "04:00:00", "05:00:00", "06:00:00", "07:00:00", "08:00:00", "09:00:00",
                        "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00",
                        "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00", "00:00:00"};

                l_time.add(timeline[position - 1]);

                ((TextView) view).setTextSize(15);
                view.setBackgroundColor(0xff00ff00);
            }
        });

        View.OnClickListener clk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*снимаю имя снисаю номер
                * лист времени
                * месяц из spiner
                * дата из picker
                *
                * записываю в базу и обнуляю*/
                String s_name = tv_name.getText().toString();
                String s_num = tv2_num.getText().toString();
                if (np_day != 0d)

        /* Log.i("L_time+", String.valueOf(l_time) + "day  " + np_day + "month  " + sp_m);
         Log.i("L_data", s_name + "___" + s_num+"___"+yyyy);*/

                    Log.i("L_time+", String.valueOf(l_time) + "day  " + np_day + "month  " + sp_m);
                Log.i("L_data", s_name + "___" + s_num+"___"+yyyy);
                // write in base in queue
                gw_t.setAdapter(gv_time);
                execute ex_qu_write = new execute();
                //  MainActivity ye=new MainActivity();
                // int yy=ye.yyyy;
                ex_qu_write.write_in(common.this, "queue_user", l_time, String.valueOf(MainActivity.yyyy)+"-" + sp_m + "-" + np_day, s_name, s_num, "*****");
                //   ex_qu_write.write_in(common.this, "queue_user", l_time, String.valueOf(q_year)+"-" + sp_m + "-" + np_day, s_name, s_num, "*****");
                l_time.clear();

                List<String> list_in_adapter = queue_real.read_in(common.this, "%");
                //  gw4.setAdapter(new ArrayAdapter(common.this, android.R.layout.simple_list_item_1, queue_real.read_in(common.this,"%") ));
                //вышел за массив
                expand((ArrayList<String>) list_in_adapter);
            }
        };

        bt_qu.setOnClickListener(clk);


    }
    // не испльзуется (создаем експанд лист из обычного массива группировка по имени)
    // для очереди
    void expand(ArrayList<String> inp_mass) {
        exp = (ExpandableListView) findViewById(R.id.exp_lw);


        ArrayList<String> arr = new ArrayList<>();

        int i = 0;
        for (String elt : inp_mass) {

            if (i % 4 == 0 || i == 0) {
                arr.add(inp_mass.get(i));
            }
            i++;

        }


        groupData = new ArrayList<Map<String, String>>();
        for (Object group : new HashSet(arr)) {
            m = new HashMap<String, String>();
            m.put(ATTR_GROUP_NAME, String.valueOf(group));
            groupData.add(m);
        }

        String groupFrom[] = new String[]{ATTR_GROUP_NAME};
        int groupTo[] = new int[]{android.R.id.text1};

        childData = new ArrayList<ArrayList<Map<String, String>>>();

        ArrayList<String> hashArr = new ArrayList<String>();
        for (Object harr : new HashSet(arr)) {
            hashArr.add((String) harr);
        }

        for (int k = 0; k < hashArr.size(); k++) {
            ArrayList<String> spt = new ArrayList<String>();
            childItem = new ArrayList<Map<String, String>>();
            for (int j = 0; j < inp_mass.size(); j++) {
                if (inp_mass.get(j).equals(hashArr.get(k))) {
                    //вышел за массив
                    spt.add(inp_mass.get(j + 1) + "  " + inp_mass.get(j + 2));
                    // spt.add(mass[j+2]);
                }

            }
            Log.i("1_cont_ind", spt.toString());

            for (String elt : spt) {
                m = new HashMap<String, String>();
                m.put(ATTR_PHONE_NAME, elt);
                childItem.add(m);
            }
            childData.add(childItem);
        }


        String childFrom[] = new String[]{ATTR_PHONE_NAME};
        int childTo[] = new int[]{android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,
                groupData, android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                childData, android.R.layout.simple_list_item_1, childFrom, childTo);

        exp.setAdapter(adapter);
        registerForContextMenu(exp);
        exp.setOnCreateContextMenuListener(this);






    }
    // gridview coloumns
    private void adjustGridView(int n, int m) {
        //  gw4.setNumColumns(n);
        gw_t.setNumColumns(m);

    }
}

