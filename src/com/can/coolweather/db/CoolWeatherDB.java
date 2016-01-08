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
 * 操作数据库的封装类
 * 
 * @author can
 *
 */
public class CoolWeatherDB {

	// 数据库名
	private static final String DB_NAME = "cool_weather.db";

	// 数据库版本号
	private static final int VERSION = 1;

	private SQLiteDatabase mSqLiteDatabase;
	private static CoolWeatherDB mCoolWeatherDB;

	// 将构造方法私有化
	private CoolWeatherDB(Context context) {
		CollWeatherOpenHelper mCollWeatherOpenHelper = new CollWeatherOpenHelper(context, DB_NAME, null, VERSION);
		mSqLiteDatabase = mCollWeatherOpenHelper.getWritableDatabase();
	}

	/**
	 * 获取CoolWeatherDB类
	 * 
	 * @param context
	 *            上下文
	 * @return CoolWeatherDB
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (mCoolWeatherDB == null) {
			mCoolWeatherDB = new CoolWeatherDB(context);
		}
		return mCoolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库
	 * 
	 * @param province
	 *           省份
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
	 * 从数据库读取全国所有省份信息
	 * 
	 * @return 省份的集合
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
	 * 将City实例存储到数据库
	 * 
	 * @param city
	 *            城市的实例
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
	 * 从数据库读取省级所有的城市信息
	 * 
	 * @param provinceId	省份的ID号
	 * @return 	城市的集合
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
				city.setProvinceId(provinceId);;
				citys.add(city);

			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return citys;
	}

	/**
	 * 将County实例存储到数据库
	 * 
	 * @param county
	 *            县的实例
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
	 * 从数据库读取市级所有的县信息
	 * @param cityId	城市的ID号
	 * @return 	县的集合
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
				county.setCityId(cityId);
				countys.add(county);
				
			}while(cursor.moveToNext());
				if(cursor!=null){
					cursor.close();
				}
		}
		return countys;
	}
}
