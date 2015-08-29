package com.servlet;


public class DataInsertionService {


	public static void inserData(DataBean data){
		if(data.isUseGa()){
			GAPusher.pushToGA(data);
		}else{
			MongoDAO.insertData(data);
		}
	}
	
	public static void insertConfig(String config){
		MongoDAO.insertConfig(config);
	}

	public static String findConfig(){
		return MongoDAO.findConfig();
	}
	//	public static void main(String[] args) {
	//		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
	//		 MongoDAO mongoDao = (MongoDAO)context.getBean("mongoDao");
	//		 DataBean bean = new DataBean();
	//		 bean.setPayload("{foo: bar, foo2: baz}");
	//		 mongoDao.insertData(bean);
	//	}
}


