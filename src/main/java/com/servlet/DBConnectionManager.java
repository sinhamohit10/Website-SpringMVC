package com.servlet;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

@Repository("dbConnectionManager")
public class DBConnectionManager {

	private static String DB_URL = "128.199.248.136";
	private static int DB_PORT = 27017;
	private static String DB_NAME = "seq";
	private static String DB_USERNAME = "test";
	private static String DB_PASSWORD = "test@123";
    private MongoClient mongoClient;

    protected final Log logger = LogFactory.getLog(getClass());
    
	private MongoClient getMongoClientConnection(){
		MongoClient mongoClient = null;
		try{
			MongoCredential credential = MongoCredential.createCredential(DB_USERNAME, DB_NAME, DB_PASSWORD.toCharArray());
			mongoClient = new MongoClient(new ServerAddress(DB_URL,DB_PORT), Arrays.asList(credential));
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return mongoClient;
	}
	
	public MongoClient getMongoClient(){
		if(mongoClient==null){
			mongoClient = getMongoClientConnection();
		}
		return mongoClient;
	}

	public MongoDatabase getDatabase(){
		MongoDatabase db = null;
		try{
			MongoClient client = getMongoClient();
			db = client.getDatabase(DB_NAME);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return db;
	}
}
