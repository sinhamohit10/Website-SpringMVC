package com.servlet;


public class DataInsertionService {
	
	
	public static void inserData(DataBean data){
		MongoDAO.insertData(data);
	}

//	public static void main(String[] args) {
//		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
//		 MongoDAO mongoDao = (MongoDAO)context.getBean("mongoDao");
//		 DataBean bean = new DataBean();
//		 bean.setPayload("{foo: bar, foo2: baz}");
//		 mongoDao.insertData(bean);
//	}
}


