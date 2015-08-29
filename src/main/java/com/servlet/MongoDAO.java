package com.servlet;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDAO {

	//private static final String APP_ID = "appId";
	//private static final String PAYLOAD = "payload";

	private static String COLLECTION_NAME = "AppMetrics";
	
	//@Autowired
	//private DBConnectionManager dbConnectionManager;
	
	public static boolean insertData(DataBean data){
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
		Document document = getDBObject(data);
		collection.insertOne(document);
		return false;
	}
	
	private static Document getDBObject(DataBean data){
		Document doc = null;
		if( StringUtils.isNotBlank(data.getPayload())){
			doc = getPayloadDocument(data.getPayload());
		}
		return doc;
	}
	
	private static Document getPayloadDocument(String payload){
		Document doc = new Document();
		StringReader reader = new StringReader(payload);
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);
		JsonElement jsonElement = new JsonParser().parse(jsonReader);
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
		Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
		while(iter.hasNext()){
			Entry<String, JsonElement> entry = iter.next();
			doc.put(entry.getKey(), entry.getValue().toString());
		}
		return doc;
	}
	//public static void main(String[] args) {
//		 DataBean bean = new DataBean();
//		 bean.setPayload("{foo: bar, foo2: baz}");
//		MongoDAO dao = new MongoDAO();
//		dao.insertData(bean);
//	}
}


