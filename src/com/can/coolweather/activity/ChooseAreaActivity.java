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
	private ArrayAdapter<String> adapter; // listview��������
	private CoolWeatherDB mCoolWeatherDB;
	private ProgressDialog mProgressDialog; // ��ȶԻ���

	private List<String> dataList = new ArrayList<String>();
	// ʡ�б�
	private List<Province> provinceList;
	// ���б�
	private List<City> cityList;
	// ���б�
	private List<County> countyList;

	// ѡ�е�ʡ��
	private Province selectionProvince;
	// ѡ�еĳ���
	private City selectionCity;

	// ��ǰѡ�еļ���
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
					// �����м����
					queryCitise();
				} else if (currentLevel == LEVEL_CITY) {
					selectionCity = cityList.get(position);
					// �����ؼ����
					queryCountise();
				}

			}

		});
		// ����ʡ�����
		queryProvince();
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ���ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryProvince() {
		// ��ѯ��ݿ�
		provinceList = mCoolWeatherDB.loadProvince();
		// �жϴ���ݿⷵ�ص�����Ƿ�Ϊ��
		if (provinceList.size() > 0) {
			// ��ݲ�Ϊ��
			// ���dataList��������
			dataList.clear();
			for (Province province : provinceList) {
				// ���ʡ���
				dataList.add(province.getProvinceName());
			}
			// ����������Ϊ��̬
			adapter.notifyDataSetChanged();
			// ��ʾ�ڵ�һ��
			mListView.setSelection(0);
			tv_title.setText("�й�");

			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFormServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ�е�ʡ�����е��У����ȴ���ݿ��ѯ�����û�в�ѯ����ȥ���������ѯ
	 */
	private void queryCitise() {
		//
		cityList = mCoolWeatherDB.loadCity(selectionProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			// ����������Ϊ��̬
			adapter.notifyDataSetChanged();
			// ��ʾ�ڵ�һ��
			mListView.setSelection(0);
			tv_title.setText(selectionProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFormServer(selectionProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * ��ѯѡ�е��������е��أ����ȴ���ݿ��ѯ�����û�в�ѯ����ȥ���������ѯ
	 */
	private void queryCountise() {
		countyList = mCoolWeatherDB.loadCounty(selectionCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			// ����������Ϊ��̬,�Զ�ˢ��
			adapter.notifyDataSetChanged();
			// ��ʾ�ڵ�һ��
			mListView.setSelection(0);
			tv_title.setText(selectionCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFormServer(selectionCity.getCityCode(), "county");
		}

	}

	/**
	 * ��ݴ���Ĵ�ź����ʹӷ������ϲ�ѯʡ�������
	 * 
	 * @param code
	 *            ���
	 * @param type
	 *            ����
	 */
	private void queryFormServer(String code, final String type) {
		// ��ַ
		String path;

		if (!TextUtils.isEmpty(code)) {
			path = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			path = "http://www.weather.com.cn/data/list3/city.xml";
		}

		// ��ʾ�Ի���
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
							// �رնԻ���
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
						// �رս�ȶԻ���
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

	// ��ʾ��ȶԻ���
	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("���ڼ�����...");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	// �رս�ȶԻ���
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
