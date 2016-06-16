package com.fancye.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 调用百度短网址接口，生成短网址并返回
 * <a href="http://blog.csdn.net/ljasdf123/article/details/21828535">百度短网址接口</a>
 * 
 * @author: Fancye
 * @date: 2014年3月22日下午9:58:54
 */
public class ShortUrlUtil2 {
	
//	public static DefaultHttpClient httpclient;
	public static HttpClient httpclient;
	static {
//		httpclient = new DefaultHttpClient();
//		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); // 接受任何证书的浏览器客户端
		httpclient = HttpClientBuilder.create().build();
	}
	
	/**
	 * 生成端连接信息
	 * 
	 * @author: Fancye
	 * @date: 2014年3月22日下午5:31:15
	 */
	public static String  generateShortUrl(String url) {
		try {
			HttpPost httpost = new HttpPost("http://dwz.cn/create.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("url", url)); // 用户名称
			httpost.setEntity(new UrlEncodedFormEntity(params,  "utf-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println(jsonStr);
			JSONObject object = JSON.parseObject(jsonStr);
			System.out.println(object.getString("tinyurl"));
			return object.getString("tinyurl");
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		
	}
	
	/**
	 * 测试生成端连接
	 * @param args
	 * @author: Fancye
	 * @date: 2014年3月22日下午5:34:05
	 */
	public static void main(String []args){
		generateShortUrl("http://help.baidu.com/index");
	}
}
