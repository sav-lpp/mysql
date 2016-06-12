package util;

import android.text.TextUtils;
import db.CoolWeatherDB;
import entity.City;
import entity.County;
import entity.Province;

public class Utilty {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvince(CoolWeatherDB coolWeatherDB,
			String reaponse){
		if(TextUtils.isEmpty(reaponse)){
			String[]allProvinces=reaponse.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);

					coolWeatherDB.saveProvince(province);
				}
				return true;
			}}
		return false;
	}
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCity(CoolWeatherDB coolWeatherDB,
			String reaponse, int provinceId){
		if(TextUtils.isEmpty(reaponse)){
			String[]allCity = reaponse.split(",");
			if(allCity!=null&&allCity.length>0){
				for(String p:allCity){
					String[] array=p.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}}
		return false;
	}
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public synchronized static boolean handleCounty(CoolWeatherDB coolWeatherDB,
			String reaponse, int cityId){
		if(TextUtils.isEmpty(reaponse)){
			String[]allCounty = reaponse.split(",");
			if(allCounty!=null&&allCounty.length>0){
				for(String p:allCounty){
					String[] array=p.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}}
		return false;
	}

}











