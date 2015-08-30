package com.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


@SuppressWarnings("serial")
@WebServlet(
        name = "UploadServlet", 
        urlPatterns = {"/upload"}
    )
public class UploadServlet extends HttpServlet {
    private File file;
    private static AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJ5LHQX7FR4VSESLA", 
			"6oPAfgjrFFzliEl/aHE65EOtLgHlR+3io4+fCK5W");
    private static AmazonS3 s3client = new AmazonS3Client(awsCredentials);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String fileName = "";
    	try {
    	FileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	List fileItems = null;
		try {
			fileItems = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		
    	// Process the uploaded file items
        Iterator i = fileItems.iterator();
        while ( i.hasNext () ) 
        {
           FileItem fi = (FileItem)i.next();
           if ( !fi.isFormField () )	
           {
              // Get the uploaded file parameters
              fileName = fi.getName();
              // Write the file
              if( fileName.lastIndexOf("\\") >= 0 ){
                 file = new File(fileName.substring( fileName.lastIndexOf("\\"))) ;
              }else{
                 file = new File(fileName.substring(fileName.lastIndexOf("\\")+1)) ;
              }
              try {
            	  if(file != null) {
            		fi.write( file ) ;
            		s3client.putObject(new PutObjectRequest("hack-lazydev", fileName,file));
            		fi.delete();
            	  }
			} catch (Exception e) {
				e.printStackTrace();
			}
           }
        }
    	} catch (Exception e) {
    	}
        ServletOutputStream out = response.getOutputStream();
        out.write(fileName.getBytes());
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	doGet(req, resp);
    }
    
    public static void main(String[] args) {
    	String url = "http://128.199.218.214:32770/upload";
    	File file = new File("build.properties");
    	try {
    		String boundary = "---------------------------7da24f2e50046";
    	    HttpClient httpclient = new DefaultHttpClient();

    	    HttpPost httppost = new HttpPost(url);

    	    InputStreamEntity reqEntity = new InputStreamEntity(
    	            new FileInputStream(file), -1);
    	    reqEntity.setContentType("application/octet-stream");
    	    //reqEntity.setChunked(true); // Send in multiple parts if needed
    	    httppost.setEntity(reqEntity);
    	    HttpResponse response = httpclient.execute(httppost);
    	    //Do something with response...

    	} catch (Exception e) {
    	    // show error
    	}
    }
}