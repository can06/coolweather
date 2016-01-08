package com.can.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.can.coolweather.R;
import com.can.coolweather.db.CoolWeatherDB;
import com.can.coolweather.model.City;
import com.can.coolweather.model.County;
import com.can.coolweather.model.Province;
import com.can.coolweather.util.HttpCallbackListener;
import com.can.coolweather.util.HttpUtil;
import com.can.coolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ListView mListView;
	private TextView tv_title;
	private ArrayAdapter<String> adapter; // listview的适配器
	private CoolWeatherDB mCoolWeatherDB;
	private ProgressDialog mProgressDialog; // 进度对话框

	private List<String> dataList = new ArrayList<String>();
	// 省列表
	private List<Province> provinceList;
	// 市列表
	private List<City> cityList;
	// 县列表
	private List<County> countyList;

	// 选中的省份
	private Province selectionProvince;
	// 选中的城市
	private City selectionCity;

	// 当前选中的级别
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		mListView = (ListView) findViewById(R.id.list_view);
		tv_title = (TextView) findViewById(R.id.tv_title);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		mListView.setAdapter(adapter);
		mCoolWeatherDB = CoolWeatherDB.getInstance(this);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectionProvince = provinceList.get(position);
					// 加载市级数据
					queryCitise();
				} else if (currentLevel == LEVEL_CITY) {
					selectionCity = cityList.get(position);
					// 加载县级数据
					queryCountise();
				}

			}

		});
		// 加载省级数据
		queryProvince();
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvince() {
		// 查询数据库
		provinceList = mCoolWeatherDB.loadProvince();
		// 判断从数据库返回的数据是否为空
		if (provinceList.size() > 0) {
			// 数据不为空
			// 清除dataList里面的数据
			dataList.clear();
			for (Province province : provinceList) {
				// 添加省名称
				dataList.add(province.getProvinceName());
			}
			// 设置适配器为动态
			adapter.notifyDataSetChanged();
			// 显示在第一行
			mListView.setSelection(0);
			tv_title.setText("中国");

			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFormServer(null, "province");
		}
	}

	/**
	 * 查询选中的省内所有的市，优先从数据库查询，如果没有查询到再去服务器里查询
	 */
	private void queryCitise() {
		//
		cityList = mCoolWeatherDB.loadCity(selectionProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			// 设置适配器为动态
			adapter.notifyDataSetChanged();
			// 显示在第一行
			mListView.setSelection(0);
			tv_title.setText(selectionProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFormServer(selectionProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询选中的市内所有的县，优先从数据库查询，如果没有查询到再去服务器里查询
	 */
	private void queryCountise() {
		countyList = mCoolWeatherDB.loadCounty(selectionCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			// 设置适配器为动态,自动刷新
			adapter.notifyDataSetChanged();
			// 显示在第一行
			mListView.setSelection(0);
			tv_title.setText(selectionCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFormServer(selectionCity.getCityCode(), "county");
		}

	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * 
	 * @param code
	 *            代号
	 * @param type
	 *            类型
	 */
	private void queryFormServer(String code, final String type) {
		// 网址
		String path;

		if (!TextUtils.isEmpty(code)) {
			path = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			path = "http://www.weather.com.cn/data/list3/city.xml";
		}

		// 显示对话框
		showProgressDialog();

		HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {

			@Override
			public void onFinish(String data) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handlerProvinceResponse(data, mCoolWeatherDB);
				} else if ("city".equals(type)) {
					result = Utility.handlerCityResponse(data, mCoolWeatherDB, selectionProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handlerCountyResponse(data, mCoolWeatherDB, selectionCity.getId());
				}

				if (result) {
					//
					runOnUiThread(new Runnable() {
						public void run() {
							// 关闭对话框
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvince();
							} else if ("city".equals(type)) {
								queryCitise();
							} else if ("county".equals(type)) {
								queryCountise();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						// 关闭进度对话框
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

	// 显示进度对话框
	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("正在加载中...");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	// 关闭进度对话框
	private void closeProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCitise();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvince();
		} else {
			finish();
		}
	}
}
