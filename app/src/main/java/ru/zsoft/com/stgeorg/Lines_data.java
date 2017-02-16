package ru.zsoft.com.stgeorg;

/**
 * Created by User on 15.01.2017.
 */

public class Lines_data {
   /* время
    * имя
    * цена
    * флаг*/
    String id;
    String time;
    String name;
    String sum;
    boolean flag;

    Lines_data(String _id, String _time, String _name, String _sum, boolean _flag){
        id=_id;
        time=_time;
        name=_name;
        sum=_sum;
        flag=_flag;

    }

}
