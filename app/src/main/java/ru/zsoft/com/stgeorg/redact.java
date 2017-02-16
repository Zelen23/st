package ru.zsoft.com.stgeorg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Home on 09.01.2017.
 */

        /*получаю номер
        * делаю запрос к прльзователям
        * получат массив
        * парсиег имени
        * вывожу в едиты
        * слушаю едиты
        *           если изменено то записываю*/

public class redact  extends MainActivity {

    EditText ename,efamily,enumber,eulr;
    Button bt;
    boolean flag=false;
    ArrayList<String> liner ;
    ArrayList<String> redline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact);

        ename=(EditText)findViewById(R.id.editName);
        efamily=(EditText)findViewById(R.id.editFamily);
        enumber=(EditText)findViewById(R.id.editNumber);
        eulr=(EditText)findViewById(R.id.editUlr);
        bt=(Button)findViewById(R.id.bRed);

        Intent intent = getIntent();
        String pk_num = intent.getStringExtra("pk_num");


        Log.i("redact_1",pk_num);
         parseName(pk_num);


        View.OnClickListener clk=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBe();

                if(flag==false){
                    Log.i("redact_flag",String.valueOf(flag)+"--"+toBe());
                    finish();
                }
                else{
     // записываю в юзера данные
                    execute wr_user =new execute();
                    wr_user.redact_user(redact.this,toBe());

                    Log.i("redact_flag",String.valueOf(flag)+"--"+toBe());
                    finish();

                }
               Log.i("redact_flag",String.valueOf(flag)+"---"+liner.toString());


            }
        };
    bt.setOnClickListener(clk);

    }

    void parseName(String number) {

        /*получил строку
         *разбил имя
         *вывел пр едитам*/


        execute getInf = new execute();
        liner = getInf.getLine_(this, number);

        Log.i("redact_liner", liner.toString());

        if (liner.size() != 0) {
            enumber.setText(liner.get(3));
            eulr.setText(liner.get(4));

            Log.i("redact", String.valueOf(liner.size()) + "---" + liner.get(1));
            if (liner.get(2) == null) {
                String[] prs_name = liner.get(1).split(" ");
                Log.i("redact_2", String.valueOf(prs_name.length) + "--" + prs_name[0]);

                if (prs_name.length > 1) {
                     ename.setText(prs_name[0]);
                     efamily.setText(prs_name[1]);
                } else {
                     ename.setText(prs_name[0]);
                   //  efamily.setText("--");

               }

            } else {
                ename.setText(liner.get(1));
                efamily.setText(liner.get(2));


            }



        }
    }

   ArrayList<String> toBe(){
        /* если без данные из лайнера соответствуют данным в эдитаз то
        по нажатию на батон закрываемся
        если нет то пишем в базу
        * */
        String name=ename.getText().toString();
        String family=efamily.getText().toString();
        String number=enumber.getText().toString();
        String ulr=eulr.getText().toString();


        /*если значение пусто то null*/

        ArrayList<String> newline=new ArrayList();
        newline.add(liner.get(0));
        newline.add(name);
        newline.add(family);
        newline.add(number);
        newline.add(ulr);
for(int i=1;i<newline.size();i++){
    //значение соответствует
    if(newline.get(i).equals("")){
        newline.set(i,null);
    }

}

     if(liner.equals(newline)){
     // если соответствует
         flag=false;
         Log.i("liner_contalins",newline.toString());
     }else{

         flag=true;
     }
       return newline;
    }



}
