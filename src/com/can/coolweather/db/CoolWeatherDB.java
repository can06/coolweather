package com.can.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.can.coolweather.model.City;
import com.can.coolweather.model.County;
import com.can.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;;

/**
 * ���ݿ������(��װ)
 * 
 * @author Administrator
 *
 */
public class CoolWeatherDB {

	// ���ݿ�����
	private static final String DB_NAME = "cool_weather.db";

	// ���ݿ�汾��
	private static final int VERSION = 1;

	private SQLiteDatabase mSqLiteDatabase;
	private static CoolWeatherDB mCoolWeatherDB;

	// �����췽��˽�л�
	private CoolWeatherDB(Context context) {
		CollWeatherOpenHelper mCollWeatherOpenHelper = new CollWeatherOpenHelper(context, DB_NAME, null, VERSION);
		mSqLiteDatabase = mCollWeatherOpenHelper.getWritableDatabase();
	}

	/**
	 * ��ȡCoolWeatherDB��ʵ��
	 * 
	 * @param context
	 *            ������
	 * @return CoolWeatherDB
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (mCoolWeatherDB == null) {
			mCoolWeatherDB = new CoolWeatherDB(context);
		}
		return mCoolWeatherDB;
	}

	/**
	 * ��Provinceʵ���洢�����ݿ�
	 * 
	 * @param province
	 *            ��ʾʡ
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			mSqLiteDatabase.insert("Province", null, values);
		}
	}

	/**
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ
	 * 
	 * @return ����ʡ�ļ���
	 */
	public List<Province> loadProvince() {
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = mSqLiteDatabase.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));

				provinces.add(province);

			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return provinces;
	}

	/**
	 * ��Cityʵ���洢�����ݿ�
	 * 
	 * @param city
	 *            ��ʾ��
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			mSqLiteDatabase.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ��ȡĳʡ���еĳ�����Ϣ
	 * 
	 * @param provinceId
	 * @return ���س��еļ���
	 */
	public List<City> loadCity(int provinceId) {
		List<City> citys = new ArrayList<City>();
		Cursor cursor = mSqLiteDatabase.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				citys.add(city);

			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return citys;
	}

	/**
	 * ��Countyʵ���洢�����ݿ�
	 * 
	 * @param county
	 *            ��ʾ��
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			mSqLiteDatabase.insert("County", null, values);
		}
	}

	/**
	 * �����ݿ��ȡĳ�����е�����Ϣ
	 * @param provinceId
	 * @return �����صļ���
	 */
	public List<County> loadCounty(int cityId){
		List<County> countys = new ArrayList<County>();
		Cursor cursor = mSqLiteDatabase.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				countys.add(county);
				
			}while(cursor.moveToNext());
				if(cursor!=null){
					cursor.close();
				}
		}
		return countys;
	}
}