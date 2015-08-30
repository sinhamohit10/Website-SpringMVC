package com.servlet;

import java.util.Map;

public class DataInsertionService {


	public static void inserData(DataBean data){
			GAPusher.pushToGA(data);
			MongoDAO.insertData(data);
	}
	
	public static void insertConfig(String config){
		MongoDAO.insertConfig(config);
	}

	public static String findConfig(){
		return MongoDAO.findConfig();
	}
	
	public static Map<String, Integer> getData(){
		return MongoDAO.readData();
	}
	//	public static void main(String[] args) {
	//		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
	//		 MongoDAO mongoDao = (MongoDAO)context.getBean("mongoDao");
	//		 DataBean bean = new DataBean();
	//		 bean.setPayload("{foo: bar, foo2: baz}");
	//		 mongoDao.insertData(bean);
	//	}
}


