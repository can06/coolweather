package com.can.coolweather.util;

/**
 * 网络工具的回调接口
 * 
 * @author can
 *
 */
public interface HttpCallbackListener {

	void onFinish(String data);

	void onError(Exception e);

}
