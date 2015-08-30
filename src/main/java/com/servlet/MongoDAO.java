package com.servlet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDAO {

	//private static final String APP_ID = "appId";
	//private static final String PAYLOAD = "payload";

	private static String APP_METRICS_COLLECTION_NAME = "AppMetrics";
	private static String APP_CONFIGS_COLLECTION_NAME = "AppConfigs";
	
	//@Autowired
	//private DBConnectionManager dbConnectionManager;
	
	public static void insertConfig(String config){
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(APP_CONFIGS_COLLECTION_NAME);
		Document document = new Document();
		document.put("appId", "APP1");
		document.put("config",config);
		collection.insertOne(document);
	}
	
	public static String findConfig(){
		String config = null;
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(APP_CONFIGS_COLLECTION_NAME);
		Document document = new Document();
		document.put("appId", "APP1");
		MongoCursor<Document> iter= collection.find(document).iterator();
		if(iter.hasNext()){
			Document doc = iter.next();
			config = (String) doc.get("config");
		}
		return config;
	}
	
	public static boolean insertData(DataBean data){
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(APP_METRICS_COLLECTION_NAME);
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
	
	
	public static String getSummary(){
		String cmd = "\"aggregate\", {"+
        "pipeline: ["+
        "{ $unwind: \"$data\" },"+
        "{"+
            "$group: {"+
                "_id: { _id: \"$_id\", metric: \"$data.metric\" },"+
            "}"+
        "},"+
        "{"+
            "$group: {"+
                "_id: { metric:\"$_id.metric\" },"+
                "sum: { $sum: 1 },"+
            "}"+
        "}"+
    "]"+
"}";

		Document doc  = new Document();
		Document aggDoc = new Document();
		List<Document> pipeArr = new ArrayList<Document>();
		pipeArr.add(new Document("$unwind","$data"));
		pipeArr.add(new Document("$group",new Document("_id",new Document("_id","$_id, metric: $data.metric"))));
		Document grDoc = new Document();
		grDoc.put("_id", new Document("metric","$_id.metric"));
		grDoc.put("sum", new Document("$sum",1));
		
		doc.put("aggregate",aggDoc);
		aggDoc.put("pipeline", pipeArr);
		
		pipeArr.add(new Document("$group",grDoc));
		
		System.out.println(new Document("tt",pipeArr).toJson().toString());
		
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(APP_METRICS_COLLECTION_NAME);
		MongoCursor<Document> d = collection.aggregate(pipeArr).iterator();
		while(d.hasNext()){
			System.out.println(d.next().toJson());
			
		}
		
		//Document d = db.runCommand(doc);
		return null;
	}
	
	
	public static Map<String,Integer> readData(){
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		MongoDatabase db = dbConnectionManager.getDatabase();
		MongoCollection<Document> collection = db.getCollection(APP_METRICS_COLLECTION_NAME);
		
		Document doc = new Document();
		doc.put("metric", 1);
		doc.put("value", 1);
		doc.put("_id", 0);
		Map<String,Integer> summary = new HashMap<String,Integer>();
		FindIterable<Document> fi=collection.find();
		MongoCursor<Document> cur = fi.iterator();
		while(cur.hasNext()){
			ArrayList<Document> lDoc = (ArrayList<Document>)cur.next().get("data");
			
			for(Document dd:lDoc){
				String key = (String) dd.get("metric");
				Integer val = (Integer) dd.get("value");
				
				if(summary.containsKey(key)){
					val += summary.get(key);
				}
				summary.put(key, val);
			}
			
		}
		return summary;
	}
	//public static void main(String[] args) {
//		 DataBean bean = new DataBean();
//		 bean.setPayload("{foo: bar, foo2: baz}");
//		MongoDAO dao = new MongoDAO();
//	dao.insertData(bean);
	//	Map<String,Integer> tt = readData();
	//	Iterator<String> iter = tt.keySet().iterator();
	//	while(iter.hasNext()){
	//		String key = iter.next();
	//		System.out.println(key + " "+ tt.get(key));
	//	}
	//}
}


