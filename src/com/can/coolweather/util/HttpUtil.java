package com.can.coolweather.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * ���繤����
 * 
 * @author can
 *
 */
public class HttpUtil {

	public HttpUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void sendHttpRequest(final String path, final HttpCallbackListener httpCallbackListener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient mHttpClient = new DefaultHttpClient();
				HttpGet mHttpGet = new HttpGet(path);
				HttpResponse mHttpResponse = null;
				try {
					mHttpResponse = mHttpClient.execute(mHttpGet);

					if (mHttpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity mHttpEntity = mHttpResponse.getEntity();
						String data = EntityUtils.toString(mHttpEntity, "utf-8");

						if (httpCallbackListener != null) {
							// �ص�onFinish����
							httpCallbackListener.onFinish(data);
						}
					}
				} catch (Exception e) {
					// �ص�onError����
					httpCallbackListener.onError(e);
				}

			}
		}).start();
	}
}
