package db;

import java.util.ArrayList;
import java.util.List;

import entity.City;
import entity.County;
import entity.Province;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *	封装了对数据库的增、查操作
 */
public class CoolWeatherDB {
	/**
	 * 数据库名
	 */
	public static final String DB_NAME="cool_weather";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION=1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	/**
	 * 构造方法私有化
	 */
	private CoolWeatherDB(Context context){
		CoolWeatherIOpenHelper dbHelper=new CoolWeatherIOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	/**
	 * 获取CoolWeather的实例DB
	 */
	public synchronized static CoolWeatherDB getinstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	/**
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}}
	/**
	 * 从数据库中读取全国所有的省份信息
	 */
	public List<Province>loadProvices(){
		List<Province>list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_Code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	/**
	 * 将city实例存储到数据库
	 */
	public void saveCity(City city){
		if(city != null){
		ContentValues values=new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("city", null, values);
		}
	}
	/**
	 * 从数据库读取某省下所有的城市信息
	 */
	public List<City>loadCity(int provinceId){
		List<City>list=new ArrayList<City>();
		Cursor c=db.query("city", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(c.moveToFirst()){
			do{
				City city=new City();
				city.setId(c.getInt(c.getColumnIndex("id")));
				city.setCityName(c.getString(c.getColumnIndex("city_name")));
				city.setCityCode(c.getString(c.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
			}while(c.moveToNext());
		}
		if(c!=null){
			c.close();
		}
		return list;
	}
	/**
	 * 将county存储到数据库
	 */
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values=new ContentValues();
			values.put("county_code", county.getCountyCode());
			values.put("county_name", county.getCountyName());
			values.put("city_id", county.getCityId());
			db.insert("county", null, values);
		}
	}
	/**
	 * 从数据库读取所有县的信息
	 */
	public List<County>loadCounty(int cityId){
		List<County>list=new ArrayList<County>();
		String[] selectionArgs={cityId+""};
		Cursor c=db.query("county", null, "city_id=?", selectionArgs, null, null, null);
		if(c.moveToFirst()){
			do{
				County county=new County();
				county.setId(c.getInt(c.getColumnIndex("id")));
				county.setCountyName(c.getString(c.getColumnIndex("county_name")));
				county.setCountyCode(c.getString(c.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			}while(c.moveToNext());
		}
		c.close();
		return list;
	}
	
}












