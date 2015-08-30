package com.servlet;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;

public class ElasticDAO {

	public static void insertData() throws IOException{
		//Create Client
		Settings settings = ImmutableSettings.settingsBuilder().put("es_demo", "128.199.218.214").build();
		TransportClient client = new TransportClient(settings);
		client = client.addTransportAddress(new InetSocketTransportAddress("128.199.218.214", 9300));

		//Create Index and set settings and mappings

		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("appmetrics");
		createIndexRequestBuilder.execute().actionGet();

		//Add documents
		IndexRequestBuilder indexRequestBuilder = client.prepareIndex("appmetrics", "node");
		//build json object
		XContentBuilder contentBuilder = jsonBuilder().startObject().prettyPrint();
		contentBuilder.field("metric", "jai");
		contentBuilder.endObject();
		indexRequestBuilder.setSource(contentBuilder);
		IndexResponse response = indexRequestBuilder .execute().actionGet();

		//Get document
		//GetRequestBuilder getRequestBuilder = client().prepareGet(indexName, type, id);
		//getRequestBuilder.setFields(new String[]{"name"});
		//GetResponse response = getRequestBuilder.execute().actionGet();
		//String name = response.field("name").getValue().toString();
	}
	public static void main(String[] args) throws Exception{
		insertData();
	}
}
