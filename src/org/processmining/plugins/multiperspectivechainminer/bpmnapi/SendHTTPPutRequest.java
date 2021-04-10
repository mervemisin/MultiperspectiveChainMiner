package org.processmining.plugins.multiperspectivechainminer.bpmnapi;

import java.io.File;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SendHTTPPutRequest {

	 public static void callWithFile(String uriSt, String filePath, String perspective, String model) throws Exception {
		 
		 String charset = "UTF-8";
         File uploadFile1 = new File(filePath); //"C:/Users/Mervenur/Desktop/dataset/decisionminerexamplelogalpharesult.pnml"
         URI uri = new URI(uriSt); // "http://localhost:0146/api/datamodel"

         HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(uploadFile1))
                .addTextBody("model", model)
                .addTextBody("perspective", perspective)
                .build();

		 HttpPut request = new HttpPut(uri);
		 request.setEntity(entity);
		
		 HttpClient client = HttpClientBuilder.create().build();
		 HttpResponse response = client.execute(request);
	             
		 HttpEntity entityResp = response.getEntity();

         // Read the contents of an entity and return it as a String.
         String content = EntityUtils.toString(entityResp);
         System.out.println(content);
	}
}
