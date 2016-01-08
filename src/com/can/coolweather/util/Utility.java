package com.can.coolweather.util;

import com.can.coolweather.db.CoolWeatherDB;
import com.can.coolweather.model.City;
import com.can.coolweather.model.County;
import com.can.coolweather.model.Province;

import android.text.TextUtils;

/**
 * ������������
 * 
 * @author can
 *
 */
public class Utility {

	public Utility() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �����ʹ�����������ص�ʡ������
	 * 
	 * @param data
	 *            ���������ص�����
	 * @param mCoolWeatherDB
	 *            �洢����
	 * @return boolean����
	 */
	public synchronized static boolean handlerProvinceResponse(String data, CoolWeatherDB mCoolWeatherDB) {
		//�ж��ַ����Ƿ�Ϊ�ջ򡰡���
		if (!TextUtils.isEmpty(data)) {
			String[] allProvince = data.split(","); // ����,���ַ����ָ����
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) {
					String[] provinceArray = p.split("\\|"); // ������01|�������ַ������ָ����
					Province province = new Province();
					province.setProvinceCode(provinceArray[0]);
					province.setProvinceName(provinceArray[1]);

					// ���������������ݴ洢��Province��
					mCoolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��м�����
	 * 
	 * @param data
	 *            ���������ص�����
	 * @param mCoolWeatherDB
	 *            �洢����
	 * @param provinceId
	 *            �еĹ�����
	 * @return ���ز�������
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

					// ���������������ݴ洢��City��
					mCoolWeatherDB.saveCity(city);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * �����ʹ�����������ص�����
	 * 
	 * @param data
	 *            ���������ص�����
	 * @param mCoolWeatherDB
	 *            �洢����
	 * @param cityId
	 *            �صĹ�����
	 * @return ���ز�������
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

					// ���������������ݴ洢��county��
					mCoolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}

		return false;
	}
}
