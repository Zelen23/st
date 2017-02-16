package ru.zsoft.com.stgeorg;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 /* триггеры на служебное* /

 */


public class db extends SQLiteOpenHelper implements BaseColumns {

             private static final String DATABASE_NAME="st.db";
             private static final int DATABASE_VERSION=1;

    private static final String DATABASE_TABLE ="clients";
    public static final String ID_COLUMN="id";
    public static final String NAME_COLUMN="name";
    public static final String TIME1_COLUMN="time1";
    public static final String TIME2_COLUMN="time2";
    public static final String CONTACT_COLUMN="sf_num";
    public static final String PAY_COLUMN="pay";
    public static final String DATE_COLUMN="date";
    public static final String DATE1_COLUMN="date1";
    public static final String VISIT_COLUMN="visit" ;
    public static final String FILE_DIR="/sdcard/";



    private static final String DATABASE_TABLE2 ="user";
    public static final String id_us="id";
    public static final String pk_num_us="pk_num";
    public static final String name_us="name";
    public static final String family_us="family";
    public static final String url_us="url";
    public static final String count_us="count";
    public static final String last_us="last";

    private static final String DATABASE_TABLE3 ="history";
    public static final String id_his="id";
    public static final String date_his="date";
    public static final String count_his="d_count";

    private static final String DATABASE_TABLE4="user_ord";
    public static final String id_ord="id";
    public static final String us_num="us_num";
    public static final String us_count="us_count";

    private static final String DATABASE_TABLE5="queue_user";
    public static final String qu_id="id";
    public static final String qu_num="num";
    public static final String qu_name="name";
    public static final String qu_interval_t="time1";
    public static final String qu_interval_date="date";
    public static final String qu_comment="comment";

    private static final String DATABASE_TABLE6 ="temp";
    public static final String ID_TEMP="_id";
    public static final String NAME_TEMP="name";
    public static final String TIME1_TEMP="time1";
    public static final String TIME2_TEMP="time2";
    public static final String CONTACT_TEMP="sf_num";
    public static final String PAY_TEMP="pay";
    public static final String DATE_TEMP="date";
    public static final String DATE1_TEMP="date1";
    public static final String VISIT_TEMP="visit" ;



    /*
CREATE TABLE [queue_user] (
  [id] INT NOT NULL,
  [num] int NOT NULL,
  [name] VARCHAR,
  [interval_t] VARCHAR,
  [interval_date] VARCHAR,
  [comment] VARCHAR);


CREATE TABLE [settings] (
  [id] INT NOT NULL,
  [interval] VARCHAR);


CREATE TABLE [temp_history] (
  [id] INT NOT NULL,
  [date] VARCHAR NOT NULL,
  [d_count] VARCHAR NOT NULL);


CREATE TABLE [temp_user] (
  [id] INT NOT NULL,
  [pk_num] INT NOT NULL,
  [name] VARCHAR,
  [family] VARCHAR,
  [url] VARCHAR,
  [count] int,
  [last] VARCHAR);*/

/*[id] INT,
  [us_num] INT,
  [us_count] INT);*/

    /*CREATE TABLE [temp] (
  [_id] INTEGER,
  [Name] TEXT NOT NULL,
  [time] TIME,
  [time2] TIME,
  [sf_num] INT(20) NOT NULL,
  [pay] NVARCHAR(50),
  [date] NVARCHAR(50),
  [date1] NVARCHAR(50),
  [visit] VARCHAR NOT NULL ON CONFLICT REPLACE DEFAULT false);
  */

    private static final String DATABASE_CREATE_SCRIPT2 = "create table "
		+ DATABASE_TABLE + " (" + BaseColumns._ID
		+ " integer primary key autoincrement, "
		+ NAME_COLUMN + " text not null, "
		+ TIME1_COLUMN + " time, "
		+ TIME2_COLUMN + " time, "
		+ CONTACT_COLUMN + " INT(20) NOT NULL CONSTRAINT [fk_num] REFERENCES [user]([pk_num]) ON DELETE NO ACTION ON UPDATE NO ACTION MATCH SIMPLE ,"
		+ PAY_COLUMN + " nvarchar(50), "
		+ DATE_COLUMN + " nvarchar(50), "
        + DATE1_COLUMN + " nvarchar(50),"
        + VISIT_COLUMN  + " VARCHAR NOT NULL ON CONFLICT REPLACE DEFAULT false );"
        ;

    private static final String DATABASE_CREATE_SCRIPT = "create table "
    +DATABASE_TABLE2+"("
            +id_us+" INT NOT NULL ,"
            +pk_num_us+" INT NOT NULL ,"
            +name_us+" VARCHAR ,"
            +family_us+ "  VARCHAR ,"
            +url_us+ " VARCHAR ,"
            +count_us+" INT ,"
            +last_us+" VARCHAR ,"
            +"CONSTRAINT [sqlite_autoindex_user_1] PRIMARY KEY ([pk_num]));"
            ;
		//+ DATE1_COLUMN + " datetime NOT NULL DEFAULT '1900-01-01');";

    private static final String DATABASE_CREATE_SCRIPT3="create table "
            +DATABASE_TABLE3+"("
            +id_his+ " INT ,"
            +date_his+ " VARCHAR ,"
            +count_his+ " VARCHAR );"
            ;
    private static final String DATABASE_CREATE_SCRIPT4="create table "
            +DATABASE_TABLE4+"("
            +id_ord+ " INT ,"
            +us_num+ " INT ,"
            +us_count+ " INT );"
            ;
    private static final String DATABASE_CREATE_SCRIPT5="create table "
            +DATABASE_TABLE5+ " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            +qu_id+" INT ,"
            +qu_num+" INT ,"
            +qu_name+" VARCHAR ,"
            +qu_interval_t+" VARCHAR ,"
            +qu_interval_date+" VARCHAR ,"
            +qu_comment+" VARCHAR );"
            ;

    private static final String DATABASE_CREATE_SCRIPT6 = "create table "
		    + DATABASE_TABLE6 + " (" + BaseColumns._ID
		    + " integer primary key autoincrement, "
		    + NAME_TEMP + " text not null, "
		    + TIME1_TEMP+ " time, "
		    + TIME2_TEMP + " time, "
		    + CONTACT_TEMP + " INT(20) NOT NULL, "
		    + PAY_TEMP + " nvarchar(50), "
		    + DATE_TEMP + " nvarchar(50), "
            + DATE1_TEMP + " nvarchar(50),"
            + VISIT_TEMP  + " VARCHAR NOT NULL ON CONFLICT REPLACE DEFAULT false );"
            ;


    /*db(View.OnClickListener onClickListener, String s, Context context, int i){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
*/
    Context mContext;

    public db(Context context)
    {

    /* super( context, DATABASE_NAME , null, DATABASE_VERSION);
        mContext = context;
    */

      super(context, Environment.getExternalStorageDirectory()

                + File.separator + FILE_DIR
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);


    }

    /*  public DatabaseHelper(final Context context) {
    super(context, Environment.getExternalStorageDirectory()
            + File.separator + FILE_DIR
            + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
}*/

    public db (Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
        db.execSQL(DATABASE_CREATE_SCRIPT2);
        db.execSQL(DATABASE_CREATE_SCRIPT3);
        db.execSQL(DATABASE_CREATE_SCRIPT4);
        db.execSQL(DATABASE_CREATE_SCRIPT5);
        db.execSQL(DATABASE_CREATE_SCRIPT6);
        db.execSQL( "CREATE TRIGGER [count+]\n" +
                "AFTER INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "update user\n" +
                "set count = count+1,\n" +
                "last=new.date\n" +
                "\n" +
                "where\n" +
                "new.sf_num = user.pk_num;\n" +
                "\n" +
                "END;");
        db.execSQL("CREATE TRIGGER [count-]\n" +
                "AFTER DELETE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "\n" +
                "update user\n" +
                "set count = count-1,\n" +
                "last=(select max(date) from clients where old.sf_num = sf_num)\n" +
                "where\n" +
                "old.sf_num = user.pk_num;\n" +
                "\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [clear]\n" +
                "BEFORE INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count+1\n" +
                "where\n" +
                "new.date=date;\n" +
                "\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [clear_f]\n" +
                "BEFORE INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN not EXISTS (Select * \n" +
                "From history where \n" +
                "date=new.date)\n" +
                "BEGIN\n" +
                "\n" +
                "insert into history\n" +
                "(id,date, d_count)\n" +
                "values ((select count(*) \n" +
                "from history)+1, \n" +
                "new.date,0);\n" +
                "\n" +
                "END;;");


        db.execSQL("CREATE TRIGGER [add]\n" +
                "BEFORE INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN not EXISTS (Select * From user where pk_num=new.sf_num)\n" +
                "BEGIN\n" +
                "insert into user(id,pk_num,name, count)\n" +
                "values ((select count(*) from user)+1, new.sf_num, new.name, 0);\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [user_or]\n" +
                "BEFORE INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN not EXISTS (Select * From user_ord where us_num=new.sf_num)\n" +
                "BEGIN\n" +
                "insert into user_ord(id,us_num, us_count)\n" +
                "values ((select count(*) from user_ord)+1, new.sf_num,1);\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [user_or+]\n" +
                "BEFORE INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "Begin\n" +
                "update user_ord\n" +
                "set us_count = us_count+1\n" +
                "where\n" +
                "new.sf_num = user_ord.us_num;\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [update]\n" +
                "AFTER UPDATE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "update user\n" +
                "set\n" +
                "last=new.date\n" +
                "where\n" +
                "new.sf_num = user.pk_num;\n" +
                "END;");

        db.execSQL("CREATE TRIGGER \n" +
                "[del_history]\n" +
                "AFTER DELETE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count-1\n" +
                "where date=old.date;\n" +
                "\n" +
                "\n" +
                "END;");
        db.execSQL("CREATE TRIGGER [upd_hist-]\n" +
                "AFTER UPDATE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count-1\n" +
                "where date=old.date;\n" +
                "END;");
        db.execSQL("CREATE TRIGGER [upd_hist+]\n" +
                "BEFORE UPDATE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN not EXISTS (Select * \n" +
                "From history where \n" +
                "date=new.date)\n" +
                "BEGIN\n" +
                "\n" +
                "insert into history\n" +
                "(id,date, d_count)\n" +
                "values ((select count(*) \n" +
                "from history)+1, \n" +
                "new.date,1);\n" +
                "\n" +
                "END;");
        db.execSQL("CREATE TRIGGER [upd_his=]\n" +
                "BEFORE UPDATE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN EXISTS (Select * From \n" +
                "history where \n" +
                "date=new.date)\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count+1\n" +
                "where date= new.date;\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [del]\n" +
                "AFTER UPDATE\n" +
                "ON [history]\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "\n" +
                "delete from history\n" +
                "where d_count<1;\n" +
                "\n" +
                "END;");

        db.execSQL("CREATE TRIGGER [up_date_cl]\n" +
                "AFTER UPDATE OF [pk_num]\n" +
                "ON [user]\n" +
                "BEGIN\n" +
                "\n" +
                "update clients\n" +
                "set\n" +
                "sf_num=new.pk_num\n" +
                "where old.pk_num= clients.sf_num;\n"+
                "\n"+
                "END;");
        //spec symbol
        db.execSQL("CREATE TRIGGER [add_symbol]\n" +
                "AFTER INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN not EXISTS (Select * \n" +
                "From history where \n" +
                "date=new.date) and\n" +
                "\n" +
                "new.name like '!%'\n" +
                "BEGIN\n" +
                "insert into history\n" +
                "(id,date, d_count)\n" +
                "values ((select count(*) from history)+1, new.date,20);\n" +
                "\n" +
                "END");

        db.execSQL("CREATE TRIGGER [dell_symbol]\n" +
                "AFTER DELETE\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN old.name like '!%'\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count-20\n" +
                "where date=old.date;\n" +
                "\n" +
                "END");

        db.execSQL("CREATE TRIGGER [symbol==]\n" +
                "AFTER INSERT\n" +
                "ON [clients]\n" +
                "FOR EACH ROW\n" +
                "WHEN EXISTS (Select * From \n" +
                "history where \n" +
                "date=new.date) and \n" +
                "\n" +
                "new.name like '!%'\n" +
                "BEGIN\n" +
                "update history\n" +
                "set d_count=d_count+20\n" +
                "where date= new.date;\n" +
                "\n" +
                "END");



        db.execSQL("CREATE VIEW [count] AS\n" +
                " select date, count(*) from clients where  date like strftime('%Y-%m-%%')  group by date;" );

        db.execSQL("CREATE VIEW [main] AS\n" +
                " select max(date) from clients max where sf_num = 904 order by _id desc;, ");

        db.execSQL( "CREATE VIEW [new_users] AS\n" +
                " select * from user where count<=1;" );

        db.execSQL( "CREATE VIEW [views] AS select user.[pk_num] AS number,clients.[date], user.[name] AS name\n" +
                "From user,clients\n" +
                "Where user.pk_num=clients.[sf_num] and date like strftime('%Y-%m-%%')\n" +
                "order  by  number desc;");

        Log.i("db", "create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //проверяете какая версия сейчас и делаете апдейт
      //  db.execSQL("DROP TABLE IF EXISTS tableName");
      //  onCreate(db);
    }
}
