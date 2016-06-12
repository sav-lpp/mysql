package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CoolWeatherIOpenHelper extends SQLiteOpenHelper{

	
	public CoolWeatherIOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * province表
	 */
	public static final String CREAT_PROVINCE="create table Province ("
			+"id integer primary key autoincrement, "
			+"city_name text, "
			+"city_code text)";
	/**
	 * city表
	 */
	public static final String CREAT_CITY="create table City ("
			+"id integer primary key autoincrement, "
			+"city_name text, "
			+"city_code text,"
			+"province_id integer)";
	/**
	 * county表
	 */
	public static final String CREAT_COUNTY="create table County ("
			+"id integer primary key autoincrement, "
			+"city_name text, "
			+"city_code text, "
			+"city_id integer)";
			
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAT_PROVINCE);
		db.execSQL(CREAT_CITY);
		db.execSQL(CREAT_COUNTY);
		Log.d("lpp", "建表完成!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
