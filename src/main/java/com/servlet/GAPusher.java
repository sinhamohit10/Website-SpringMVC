package com.servlet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class GAPusher {
	private static Log log = LogFactory.getLog(GAPusher.class);
	public static void pushToGA(DataBean data){
		//v=1&t=event&tid=UA-32752844-1&cid=555&ec=video&ea=play&el=holiday&ev=300
		String path = "https://www.google-analytics.com/collect";
		HttpClient client = HttpClientBuilder.create().build();
		StringReader reader = new StringReader(data.getPayload());
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);
		JsonElement jsonElement = new JsonParser().parse(jsonReader);
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
		Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
		while(iter.hasNext()){
			HttpPost post = new HttpPost(path);
			Entry<String, JsonElement> entry = iter.next();
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("v", "1"));
			urlParameters.add(new BasicNameValuePair("t", "event"));
			urlParameters.add(new BasicNameValuePair("tid", "UA-32752844-1"));
			urlParameters.add(new BasicNameValuePair("cid", "555"));
			urlParameters.add(new BasicNameValuePair("ec", data.getScreenName())); //screenname
			urlParameters.add(new BasicNameValuePair("ea", data.getAction())); //activityName:interactionType
			urlParameters.add(new BasicNameValuePair("el", entry.getKey()));
			urlParameters.add(new BasicNameValuePair("ev", entry.getValue().toString()));
			try{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			log.info("HttpResponse :"+response.getStatusLine().getStatusCode());
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}
	}
}
