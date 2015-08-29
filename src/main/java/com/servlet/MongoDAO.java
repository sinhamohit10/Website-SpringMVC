package com.servlet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

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
			doc = new Document();
			doc.put("time", new Date().toString());
			doc.put("appId", "ABC");
			doc.put("data", getPayloadDocument(data.getPayload()));
		}
		return doc;
	}
	
	private static List<Document> getPayloadDocument(String payload){
		List<Document> docs = new ArrayList<Document>();
		StringReader reader = new StringReader(payload);
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(true);
		JsonElement jsonElement = new JsonParser().parse(jsonReader);
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
		Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
		while(iter.hasNext()){
			Document nestedDoc = new Document();
			Entry<String, JsonElement> entry = iter.next();
			nestedDoc.put("metric",entry.getKey());
			nestedDoc.put("value",new Integer(entry.getValue().getAsInt()));
			docs.add(nestedDoc);
		}
		return docs;
	}
	//public static void main(String[] args) {
//		 DataBean bean = new DataBean();
//		 bean.setPayload("{foo: bar, foo2: baz}");
//		MongoDAO dao = new MongoDAO();
//		dao.insertData(bean);
//	}
}


