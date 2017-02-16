package ru.zsoft.com.stgeorg;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class calendar_c extends AppCompatActivity {
//это старый метод календаря
    /*
     + по-нормальному закрывать Activity
     - долгое касание на переходъ
     + текущая дата в date1
     +  Удаляшка
     +  Убрать индекс ввыводных стоках
     *  прикрутить прибавление часа для запроса
     *  органичение рабочего дня
     *  редактирование записи
     +  запрет записи на одно время
     * Обновление версии базы

     *  новая вкладка статистика
     */

    CalendarView cv;
    //Button bt,bt2;
    private db mDbHelper;
    private SQLiteDatabase mSqLiteDatabase;
    TextView tv,tv2;
    public String mdate;


    /*
    * 1. Долгим касанием открываю день
    * 2. Перехожу на Windows_tables
    * 3. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv=(CalendarView)findViewById(R.id.calendarView1);






        // cv.setOnDateChangeListener(new onDateChangeListener(){});
        /*cv.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Log.i("**********","imLongClick");
                return false;
            }
        });
        */
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {



            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mYear)
                        .append("-").append(mMonth + 1).append("-").append(mDay)
                        .toString();
                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
                Log.i("ddat","******"+ selectedDate);
                mdate=selectedDate;
                Intent  intent2= new Intent(calendar_c.this, windows_tables.class);
                intent2.putExtra("selectiondates",selectedDate);
                startActivity(intent2);

                //Intent intent1 = new Intent(MainActivity.this, orders.class);
                // intent1.putExtra("selectiondate", selectedDate);

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent  intent= new Intent(calendar_c.this, common.class);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

