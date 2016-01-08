package com.can.coolweather.util;

import com.can.coolweather.db.CoolWeatherDB;
import com.can.coolweather.model.City;
import com.can.coolweather.model.County;
import com.can.coolweather.model.Province;

import android.text.TextUtils;

/**
 * 解析处理工具类
 * 
 * @author can
 *
 */
public class Utility {

	public Utility() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 解析和处理服务器返回的省级数据
	 * 
	 * @param data
	 *            服务器返回的数据
	 * @param mCoolWeatherDB
	 *            存储数据
	 * @return boolean类型
	 */
	public synchronized static boolean handlerProvinceResponse(String data, CoolWeatherDB mCoolWeatherDB) {
		//判断字符串是否为空或“”；
		if (!TextUtils.isEmpty(data)) {
			String[] allProvince = data.split(","); // 把以,的字符串分割出来
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					String[] provinceArray = p.split("\\|"); // 把例如01|北京的字符串给分割出来
					Province province = new Province();
					province.setProvinceCode(provinceArray[0]);
					province.setProvinceName(provinceArray[1]);

					// 将解析出来的数据存储到Province表
					mCoolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 * 
	 * @param data
	 *            服务器返回的数据
	 * @param mCoolWeatherDB
	 *            存储数据
	 * @param provinceId
	 *            市的关联键
	 * @return 返回布尔类型
	 */
	public  static boolean handlerCityResponse(String data, CoolWeatherDB mCoolWeatherDB, int provinceId) {
		if (!TextUtils.isEmpty(data)) {
			String[] allCity = data.split(",");
			if (allCity != null && allCity.length > 0) {
				for (String c : allCity) {
					String[] cityArray = c.split("\\|");
					City city = new City();
					city.setCityCode(cityArray[0]);
					city.setCityName(cityArray[1]);
					city.setProvinceId(provinceId);

					// 将解析出来的数据存储到City表
					mCoolWeatherDB.saveCity(city);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * 解析和处理服务器返回的数据
	 * 
	 * @param data
	 *            服务器返回的数据
	 * @param mCoolWeatherDB
	 *            存储数据
	 * @param cityId
	 *            县的关联键
	 * @return 返回布尔类型
	 */
	public  static boolean handlerCountyResponse(String data, CoolWeatherDB mCoolWeatherDB, int cityId) {
		if (!TextUtils.isEmpty(data)) {
			String[] allCounty = data.split(",");
			if (allCounty != null && allCounty.length > 0) {
				for (String c : allCounty) {
					String[] countyArray = c.split("\\|");
					County county = new County();
					county.setCountyCode(countyArray[0]);
					county.setCountyName(countyArray[1]);
					county.setCityId(cityId);

					// 将解析出来的数据存储到county表
					mCoolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}

		return false;
	}
}
