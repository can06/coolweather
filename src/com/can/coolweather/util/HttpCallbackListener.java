package com.can.coolweather.util;

/**
 * ���繤�ߵĻص��ӿ�
 * 
 * @author can
 *
 */
public interface HttpCallbackListener {

	void onFinish(String data);

	void onError(Exception e);

}
