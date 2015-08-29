package com.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dataInsertionService")
public class DataInsertionService {
	
	@Autowired
	private MongoDAO mongoDao;
	
	public void inserData(DataBean data){
		mongoDao.insertData(data);
	}

//	public static void main(String[] args) {
//		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
//		 MongoDAO mongoDao = (MongoDAO)context.getBean("mongoDao");
//		 DataBean bean = new DataBean();
//		 bean.setPayload("{foo: bar, foo2: baz}");
//		 mongoDao.insertData(bean);
//	}
}


