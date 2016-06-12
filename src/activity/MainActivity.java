package activity;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utilty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;

import db.CoolWeatherDB;
import entity.City;
import entity.County;
import entity.Province;

public class MainActivity extends Activity {

	private static final int LEVEL_PROVINCE=0;
	private static final int LEVEL_CITY=1;
	private static final int LEVEL_COUNTY=2;

	private ProgressDialog progressDialog;
	private TextView tvTitle;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String>dataList=new ArrayList<String>();

	/**各级列表
	 */
	private List<Province>provincesList;
	private List<City>cityList;
	private List<County>countyList;
	/**选中的省
	 */
	private Province selectProvince;
	/**选中城市
	 */
	private City selectCity;
	/**选中的级别
	 */
	private int currentLevel;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_area);
		//控件初始化
		inity();
		coolWeatherDB=CoolWeatherDB.getinstance(this);
		//添加监听
		setListener();
		showProgressDialog();
	}
	/**控件初始化
	 */
	private void inity() {
		listView=(ListView) findViewById(R.id.activity_choose_listview);
		tvTitle=(TextView) findViewById(R.id.activity_choose_title);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dataList);
		listView.setAdapter(adapter);
	}
	/**添加监听
	 */
	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				if(currentLevel==LEVEL_PROVINCE){
					selectProvince=provincesList.get(position);
					queryCity();
				}else if(currentLevel==LEVEL_CITY){
					selectCity=cityList.get(position);
					queryCity();
			}}});
		//加载省级数据
		queryProvince();
	}

	/** 查询全国所有的省
	 */
	public  void queryProvince(){
		provincesList=coolWeatherDB.loadProvices();
		if(provincesList.size()>0){
			dataList.clear();
			for(Province province:provincesList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			tvTitle.setText("中国");
			currentLevel=LEVEL_PROVINCE;
		}else{
			queryFromServer(null, "province");
		}
	}
	/** 查询全国所有的市
	 */
	public  void queryCity(){
		cityList=coolWeatherDB.loadCity(selectProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			tvTitle.setText(selectProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else{
			queryFromServer(selectProvince.getProvinceCode(), "city");
		}
	}
	/** 查询全国所有的县
	 */
	public  void queryCounty(){
		countyList=coolWeatherDB.loadCounty(selectCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County county:countyList){
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			tvTitle.setText(selectCity.getCityName());
			currentLevel=LEVEL_COUNTY;
		}else{
			queryFromServer(selectCity.getCityCode(), "county");
		}
	}

	/**服务器查询数据
	 */
	private void queryFromServer(final String code, final String type){
		String address;
		Log.d("lpp", "sd0");
		if(TextUtils.isEmpty(code)){
			address="http:///www.weather.com.cn/data/list3/city19.xml";
		}else{
		//	address="http:///www.weather.com.cn/data/list3/city.xml";
			address="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid=&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
		}
//		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				Log.d("lpp", "sd1");
				boolean result=true;
				if("province".equals(type)){
					result=Utilty.handleProvince(coolWeatherDB, response);
				}else if("city".equals(type)){
					result=Utilty.handleCity(coolWeatherDB, response, selectProvince.getId());
				}else if("county".equals(type)){
					result=Utilty.handleCounty(coolWeatherDB, response, selectCity.getId());
				}
				if(result){
					//回到主线程
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvince();
							}else if("city".equals(type)){
								queryCity();
							}else if("county".equals(type)){
								queryCounty();
							}
						}
					});
				}
			}

//			@Override
//			public void OnError(Exception e) {
//				// 回到主线程   操作UI
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						closeProgressDialog();
//						Toast.makeText(MainActivity.this, "加载失败", 0).show();
//					}});
			}
			);
	}


	/**
	 * 显示进度的对话框
	 */
	private void showProgressDialog() {
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载。。。");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/**关闭进度显示框
	 */
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}}
	/**捕获返回键，判断级别
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(currentLevel==LEVEL_COUNTY){
			queryCity();
		}else if(currentLevel==LEVEL_CITY){
			queryProvince();
		}else{
			finish();
		}
	}
}












