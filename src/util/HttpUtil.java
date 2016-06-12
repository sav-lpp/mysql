package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener){
		//线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream is=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(is));
					StringBuilder builder=new StringBuilder();
					String line;
					while ((line=reader.readLine())!=null) {
						builder.append(line);
					}
					if(listener!=null){
						//回调onFinish（）方法
						listener.onFinish(builder.toString());
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	
}
