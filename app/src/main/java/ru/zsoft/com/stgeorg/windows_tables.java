package ru.zsoft.com.stgeorg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Администратор on 29.04.2016.
 */
public class windows_tables extends MainActivity {

    private db mDbHelper;
    boolean flag = false;
    String t1;
    String t2;
    String selectedWord;
    String seldat;
    String sFt = "07:00";
    String sEn = "23:59";
    String s_id;
    String name_;
    AlertDialog.Builder ad;
    ListView lw;

    ArrayList<String> input = new ArrayList<>();
    List<String> ll = new ArrayList<>();
    ArrayList<Lines_data> liner = new ArrayList<>();
    ArrayList<Integer> cnv;

    public static final int IDM_WRITE = 101;
    public static final int IDM_DELETE = 102;
    public static final int IDM_GET_INFO = 103;

    public static final int IDM_REWRITE = 104;
    public static final int IDM_WRITE_QU = 105;

    public static final int IDM_TEMP_X = 107;
    public static final int IDM_TEMP_C = 108;
    public static final int IDM_TEMP_WR = 109;

    private final int IDD_THREE_BUTT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windows_tables);
        /*Получил дату
        * отправил ее в read2
        * из Read2 данные на обработку в лайнер
        * лайнер в адаптер
        * */
        Intent intent = getIntent();
        seldat = intent.getStringExtra("selectiondates");
        Log.i("selectiondates_wint", "******" + seldat);

        lw = (ListView) findViewById(R.id.listView);
        registerForContextMenu(lw);
        lw.setOnCreateContextMenuListener(this);
        //дату в Read2
        List<String> data = read2(this, seldat);

        //лайнер
        set_test(data);

        try {
            cnv = convert(ex_time(input));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //строчка с датой вверху
        TextView tv_sldDat = (TextView) findViewById(R.id.tv_selectedDat);
        tv_sldDat.setText(conv(seldat));

        // Adapter MyAdapter_= new Adapter_(this,liner);
        // lw.setAdapter((ListAdapter) MyAdapter_);
        Ads Adapt = new Ads(this, liner);
        lw.setAdapter(Adapt);
    }


    // получаю по дате массив
    //[397, 19:00, 20:00, лукьянчикова ирина, , true]
    List<String> read2(Context ct, String ddat) {
//ddat="2016-4-12";
        // read db
        List<String> sqldat = new ArrayList<>();
        mDbHelper = new db(ct);
        mDbHelper.getWritableDatabase();
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        //db1.execSQL("SELECT * FROM clients where date like '"+ddat+"%'");
        //Cursor c = db1.query("clients",null,null,null,null,null,null);
        // Cursor c = db1.rawQuery("SELECT * FROM clients where date like '" + ddat + "%'", null);
        Cursor c = db1.rawQuery("SELECT * FROM clients where date like '" + ddat + "' order by time1 asc", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("_id");
            int time1 = c.getColumnIndex("time1");
            int time2 = c.getColumnIndex("time2");
            int name = c.getColumnIndex("name");
            // int date = c.getColumnIndex("date");
            int pay = c.getColumnIndex("pay");
            int flag = c.getColumnIndex("visit");
           // int sf_num = c.getColumnIndex("sf_num");

            do {
                sqldat.add(c.getString(id));
                if (c.getString(time1).length() == 8) {
                    sqldat.add(c.getString(time1).substring(0, c.getString(time1).length() - 3));
                } else {
                    sqldat.add(c.getString(time1).substring(0, c.getString(time1).length() - 7));
                }
                if (c.getString(time2).length() == 8) {
                    sqldat.add(c.getString(time2).substring(0, c.getString(time2).length() - 3));
                } else {
                    sqldat.add(c.getString(time2).substring(0, c.getString(time2).length() - 7));
                }

                sqldat.add(c.getString(name));
                sqldat.add(c.getString(pay));
                sqldat.add(c.getString(flag));
               // sqldat.add(c.getString(sf_num).toString());
                // + c.getString(date).toString());
            }
            while (c.moveToNext());
            c.close();
            db1.close();
        }
        Log.i("2_read", String.valueOf(sqldat));
        return sqldat;
    }

    // создаю лайнер(для  бейсадаптера конвертирую строку с данныим)
    void set_test(List<String> data) {


        if (data.size() <= 5) {
            input.add(sFt + "-" + sEn);
            liner.add(new Lines_data("", sFt + "-" + sEn, "_", "", false));
            Log.i("set_test", input.toString());
        } else {
            int i = 0;
            for (String elt : data) {

                if (i % 6 == 0) {
                    if (i == 0) {
                        if (data.get(1).equals(sFt)) {                                                          //++++++
                            //input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 2) + "Руб");
                            liner.add(new Lines_data(data.get(i), data.get(i + 1) + "-" + data.get(i + 2), data.get(i + 3), data.get(i + 4), Boolean.parseBoolean(data.get(i + 4))));
                        } else {
                            //  input.add(sFt + "-" + data.get(i+1));
                            //  input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
//i+1
                            liner.add(new Lines_data("", sFt + "-" + data.get(i + 1), "_", "", false));
                            liner.add(new Lines_data(data.get(i), data.get(i + 1) + "-" + data.get(i + 2), data.get(i + 3), data.get(i + 4), Boolean.parseBoolean(data.get(i + 5))));
                        }
                    }
                    if (i > 0) {
                        // если дата начала ==дате конца то
                        //i-5
                        if (data.get(i + 1).equals(data.get(i - 4))) {
                            // для симпл адаптера была закоменчена
                            //  input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                            liner.add(new Lines_data(data.get(i), data.get(i + 1) + "-" + data.get(i + 2), data.get(i + 3), data.get(i + 4), Boolean.parseBoolean(data.get(i + 5))));
                        } else {
                            liner.add(new Lines_data("", data.get(i - 4) + "-" + data.get(i + 1), "_", "", false));
                            liner.add(new Lines_data(data.get(i), data.get(i + 1) + "-" + data.get(i + 2), data.get(i + 3), data.get(i + 4), Boolean.parseBoolean(data.get(i + 5))));
                            //input.add(data.get(i - 3) + "-" + data.get(i));
                            // input.add(data.get(i) + "-" + data.get(i + 1) + "  " + data.get(i + 2) + "  " + data.get(i + 3) + "Руб");
                        }
                    }
                }
                i++;

            }
            if(data.get(data.size() - 4).equals(sEn)){

            }else
            // input.add(data.get(data.size()-4) + "-"+sEn);
            liner.add(new Lines_data("", data.get(data.size() - 4) + "-" + sEn, "_", "", false));
        }
        // Log.i("set_test___",liner.get(1).time);
        /*
        Adapter MyAdapter_= new Adapter_(this,liner);
       lw.setAdapter((ListAdapter) MyAdapter_);
*/
    }

    // получаю  из клиентов номер по имени дате и времени(криво теперь есть id можно по нему)
    public String getnum(Context context, String name, String date, String time1) {
        String snum = "";
        mDbHelper = new db(context);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        Cursor c = db1.rawQuery("SELECT sf_num FROM clients where name like '" + name + "' " + "and date like '" + date + "' " + "and time1 like '" + time1 + "' ", null);
        if (c.moveToFirst()) {
            int sf_num = c.getColumnIndex("sf_num");

            do {
                snum = (c.getString(sf_num));
            }
            while (c.moveToNext());
            c.close();
        }
        Log.i("get_num", "SELECT sf_num FROM clients where name like '" + name + "' " + "and date like '" + date + "' " + "and time1 like '" + time1 + "' ");
        return snum;
    }

    // вывожу  по номеру* в тост данные по клиету
    public void getinf(Context context, String s) {
       /*вывод диалогового окна с деталями по клиенту
       * для теста номер*/
        execute red_inf = new execute();
        ArrayList<String> info = red_inf.getLine_(context, s);
        Log.i("!_info", "----******" + info.toString());
        String mesages;
// контекст из метода
        //  mDbHelper = new db(context);
        //  SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        //  Cursor c = db1.rawQuery("SELECT * FROM user where pk_num= '" +s+ "' ", null);
        // if (c.moveToFirst()) {
        //  int pk_num = c.getColumnIndex("pk_num");
        //  int name = c.getColumnIndex("name");
        //  int family = c.getColumnIndex("family");
        //  int url = c.getColumnIndex("url");
        //  int count = c.getColumnIndex("count");
        //  int last = c.getColumnIndex("last");
        //  do {
        mesages = ("Номер:  " + info.get(3) +
                "\nИмя:      " + info.get(1) +
                "\nФамилия:      " + info.get(2) +
                "\nПоследняя запись:  " + info.get(5) +
                "\nВсего записей:           " + info.get(6));
        // не this а контекст если передаю контекст из др метода
        ad = new AlertDialog.Builder(context);
        //  }
        //  while (c.moveToNext());
        //  c.close();
        ad.setTitle("Testing");
        ad.setMessage(mesages);
        ad.setCancelable(true);
        ad.show();
        // }

    }

    // удаляю строку по имени дате времени1
    public boolean deleterow(Context context, String table, String name, String date, String time1) {
        mDbHelper = new db(context);
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();
        Log.i("2_delete_row", name + "' " + "and date like'" + date + "' " + time1);
        return db1.delete(table, "name like '" + name + "' " + "and date like'" + date + "' " + time1, null) > 0;
        //return db1.delete(table, "date like'"+date+"' "+time1, null) > 0;

    }


    // разбиваю строку со временем из адаптера на t1 и t2
    void parse_time(String time) {


        if (time.length() != 0) {
            String[] parse = time.split("-");
            if (parse.length == 2) {
                // если времени 2
                t1 = parse[0];
                t2 = parse[1];

                Log.i("parse", t1 + " " + t2);

            }

        }
    }
    //  строчка с датой для лейбла на верху
    String conv(String dat) {

        String s[] = dat.split("-");
        GregorianCalendar cd = new GregorianCalendar();
        cd.set(Calendar.MONTH, Integer.parseInt(s[1]));
        String daya = cd.getTime().toString();
        String[] getday = daya.split(" ", 5);

        String ss = s[0] + "-" + getday[1] + "-" + s[2];

        return ss;
    }
    // флаг наличия записи в буфере
    boolean temp_fl(){
        boolean fl=false;
        execute read=new execute();
        if(read.read_temp(this).size()>1){
            fl=true;
        }
        Log.i("2_read",read.read_temp(this).toString());

        return fl;
    }
    // красивая обновляшка
    void refrash() {
        Intent i = new Intent(this, this.getClass());
        finish();

//колхозный запуск активити
        Intent mainUpd = new Intent(this, MainActivity.class);
        mainUpd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainUpd);

        this.startActivity(i.putExtra("selectiondates", seldat));
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        // Получаем позицию элемента в списке
        // ЕСЛИ ВРЕМЯ ПУСТОЕ то автоматом спецсимвол
        // если спецсимвол есть то записать если нет товсе остальные
        /*актуально по ид удалить
        *                 не редакт
        *                 гет инф
        *                 копировать/вырезать
        *
         *              */


        menu.setHeaderTitle(liner.get(info.position).time + "  " + liner.get(info.position).name);
        selectedWord = liner.get(info.position).time;
        // из строки получвю t1 t2 id
        parse_time(selectedWord);
        s_id = liner.get(info.position).id;

        if (liner.get(info.position).name.equals("_")) {
            if(temp_fl()==true) {
             menu.add(menu.NONE, IDM_WRITE, menu.NONE, "Записать");
             menu.add(menu.NONE, IDM_TEMP_WR, menu.NONE, "Вставить");
            }else
             menu.add(menu.NONE, IDM_WRITE, menu.NONE, "Записать");

        } else {
            //если в таблице темп пусто то могу туда записать
            if(temp_fl()==false) {
                menu.add(menu.NONE, IDM_TEMP_X, menu.NONE, "Вырезать");
                menu.add(menu.NONE, IDM_TEMP_C, menu.NONE, "Копировать");
                menu.add(menu.NONE, IDM_DELETE, menu.NONE, "Удалить Запись");
                menu.add(menu.NONE, IDM_GET_INFO, menu.NONE, "Информация");
                menu.add(menu.NONE, IDM_REWRITE, menu.NONE, "Редактор клиентов");
            }else {
                menu.add(menu.NONE, IDM_DELETE, menu.NONE, "Удалить Запись");
                menu.add(menu.NONE, IDM_GET_INFO, menu.NONE, "Информация");
                menu.add(menu.NONE, IDM_REWRITE, menu.NONE, "Редактор клиентов");
            }

        }
        // Log.i("lenght_wint", "----******" + lenghWord+"---"+String.valueOf(pos));
    }
    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            // записать в базу
            case IDM_WRITE:
                Intent intent = new Intent(windows_tables.this, orders.class);
                intent.putExtra("selectionWorld", t1);
                intent.putExtra("selectionWorld2", t2);
                intent.putExtra("selectiondates", seldat);
                //intent.putExtra("name", "");
                //intent.putExtra("num", "");
                //intent.putExtra("sum", "");
                Log.i("2_seldat", "----******" + seldat);
                Log.i("open_vint", "----******" + t1);

                startActivity(intent);
                finish();
                // че за флаг
                flag = true;
                break;

            //запись из очереди
            case IDM_WRITE_QU:

                execute get_name = new execute();
                Log.i("2_line", get_name.read_in(this, seldat).toString());
                String gts = null;
                for (int d = 0; d < 4; d++) {
                    String[] row = selectedWord.split("-");
                    gts = row[0];
                }
                hash(get_name.read_in(this, seldat + "'" + " and time1 like '" + gts + ":00"));

                Log.i("2_WR", selectedWord);
                Log.i("2_WR", seldat);
                flag = true;
                break;

            // улалить
            case IDM_DELETE:
                String[] row = selectedWord.split("-");
                String time1 = row[0];

                deleterow(this, "clients", "%", seldat, "and time1 like '" + time1 + ":00' ");
                refrash();
                break;
            // получить инфо
            case IDM_GET_INFO:
                String[] grow = selectedWord.split("-");
                time1 = grow[0];
                // гет нум по ид getRow_id(item1)
                getinf(this, getnum(this, "%", seldat, time1 + ":00"));
                flag = true;
                break;

            // редактор
            case IDM_REWRITE:
                String[] data = selectedWord.split("-");
                String t = data[0];

                Intent ired = new Intent(windows_tables.this, redact.class);
                // посылаю номер на редакт
                // гет нум по ид getRow_id(item1)
                ired.putExtra("pk_num", getnum(this, "%", seldat, t + ":00"));
                startActivity(ired);
                flag = true;
                break;

            case IDM_TEMP_C:
                /*записываю в таблицу темп
                * удаляю из клиентс
                *
                */
                execute get_row = new execute();
                get_row.getRow_id(this, "clients", s_id);
                /*запустил гетров заполнил лайн()*/
                orders wr_q = new orders();
                wr_q.write_orders(this, get_row.line.get(6), get_row.line.get(2), get_row.line.get(3),
                        get_row.line.get(5), get_row.line.get(1), get_row.line.get(4), "temp",s_id);

                Log.i("get id", s_id);
                break;

            case IDM_TEMP_WR:
                // /*
                // читаю строку из темпа
                // передаю номер
                // имя
                // сумму
                // в orders
                // удаляю строку из temp*/

                Intent int_temp = new Intent(windows_tables.this, orders.class);
                execute r_tmp = new execute();

                int_temp.putExtra("selectionWorld", t1);
                int_temp.putExtra("selectionWorld2", t2);
                int_temp.putExtra("selectiondates", seldat);

                int_temp.putExtra("name", r_tmp.read_temp(this).get(0));
                int_temp.putExtra("num",  r_tmp.read_temp(this).get(3));
                int_temp.putExtra("sum",  r_tmp.read_temp(this).get(4));

                startActivity(int_temp);
                finish();

                deleterow(this, "temp", "%", "%", "and time1 like '" + "%' ");
                break;

            case IDM_TEMP_X:
                orders wr_x = new orders();
                execute _row = new execute();
                _row.getRow_id(this, "clients", s_id);
                // запись  в темп
                wr_x.write_orders(this, _row.line.get(6), _row.line.get(2), _row.line.get(3),
                        _row.line.get(5), _row.line.get(1), _row.line.get(4), "temp",s_id);

                deleterow(this, "clients", "%", seldat, "and time1 like '" + t1 + ":00' ");
                refrash();
                break;

            default:
                return super.onContextItemSelected(item);

        }
        return true;
    }

    //
    public void list_for_render() {
        execute get_list_t = new execute();
        get_list_t.read_in(this, seldat);
        ll = get_list_t.l_time;
        Log.i("2_l_time", String.valueOf(ll));
    }
    //
    ArrayList<String> ex_time(ArrayList input) {
        //    int i = 0;
        ArrayList<String> time = new ArrayList<>();
        for (Object elt : input) {
            String[] sub_input = elt.toString().split("  ");
            time.add(sub_input[0]);
        }

        Log.i("ex_time", time.toString());
        return time;
    }

    // не используется (хеш для очереди)
    void hash(List<String> read) {
        /*
    * получение хешей
    * запись из того что дал хеш
    */
        ArrayList<String> arr = new ArrayList<String>();
        ArrayList<String> arr2 = new ArrayList<String>();

        int i = 0;
        for (String elt : read) {
            if (i % 4 == 0 || i == 0) {
                arr.add(read.get(i));
                arr2.add(read.get(i + 3));
            }
            i++;
        }

        Log.i("2_mas", String.valueOf(read.size()));
        Log.i("2_mas", String.valueOf(arr));
        Log.i("2_mas", String.valueOf(arr2));

        String[] array = new String[arr.size()];
        String[] array2 = new String[arr.size()];
        int index = 0;
        for (Object value : arr) {
            array[index] = (String) value;
            index++;

        }

        alert(array, arr2);
    }
    // не используется (были едиты из них брал время)
    String[] cut(String stt, String snd) {
        /* читаю значения из едитов
         * ограничиваю массив в соответствии со значением
         */
        String[] timeline = {"01:00:00", "02:00:00", "03:00:00", "04:00:00", "05:00:00", "06:00:00", "07:00:00", "08:00:00", "09:00:00",
                "10:00:00", "11:00:00", "12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00", "17:00:00", "18:00:00",
                "19:00:00", "20:00:00", "21:00:00", "22:00:00", "23:00:00", "00:00:00"};
        int a = 0;
        int b = 0;

        for (int i = 0; i < timeline.length; i++) {

            if (timeline[i].contains(stt)) {
                a = i;
            }
            if (timeline[i].contains(snd)) {
                b = i + 1;
            }
        }
        String mscut[] = new String[b - a];

        for (int i = 0; i < b - a; i++) {
            mscut[i] = timeline[a + i];
            //timecut[i] = timeline[a + i];
            //  Log.i("wt_elt+3", timecut[i]);
        }

        Log.i("wt_elt+2", a + "---" + b);
        return mscut;
    }
    // не используется тост с селектором если на время несколько человек
    void alert(final String[] mas, final ArrayList<String> arr2) {


        //делаю запрос кто на текуущее время и эту дату
        ad = new AlertDialog.Builder(this);
        showDialog(IDD_THREE_BUTT);
        ad.setTitle("Testing");

        ad.setItems(mas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(),
                        "В запись: " + mas[which],
                        Toast.LENGTH_SHORT).show();
                name_ = mas[which];
                Log.i("2_arr2", String.valueOf(arr2.get(which)));
                orders wr_q = new orders();
                // запись из очереди
    /*
                wr_q.write_orders(windows_tables.this, seldat, selectedWord,selectedWord, "1000", name_,arr2.get(which));
                deleterow(windows_tables.this, "queue_user", name_, seldat, "");
                refrash();
     */
            }
        });
        // ad.setMessage("---");
        ad.setCancelable(true);


        ad.show();
    }
    // не используется (конвертирую разницу time1 и time2 в минуты
    // для корреляции высоты строки)
    ArrayList<Integer> convert(ArrayList time) throws ParseException {

        /* разниу втроого и перврго
        * зарисыва*/
        SimpleDateFormat e_time = new SimpleDateFormat("HH:mm");
        // String strTime = e_time.format(new Date());

        int i = 0;
        ArrayList<Integer> delta = new ArrayList<>();
        for (Object elt : time) {
            String[] deltatimes = elt.toString().split("-");
            String t1 = deltatimes[0];
            String t2 = deltatimes[1];
            Date dt1 = e_time.parse(t1);
            Date dt2 = e_time.parse(t2);
            long minutes = dt2.getTime() - dt1.getTime();
            int deltaminutes = (int) (minutes / (60 * 1000));
            delta.add(deltaminutes);
            i++;
        }
        Log.i("convert", delta.toString());
        return delta;
    }



}
