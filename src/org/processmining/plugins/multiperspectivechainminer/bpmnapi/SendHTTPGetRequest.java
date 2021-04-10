package org.processmining.plugins.multiperspectivechainminer.bpmnapi;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SendHTTPGetRequest {

public static void callDownloadFile(String uri, String savedFilePath) throws Exception {
	
	/*File file = new File(savedFilePath);
	Response execute = Request.Get(uri).execute();
	execute.saveContent(file);*/
	
	
	File myFile = new File(savedFilePath);

	CloseableHttpClient client = HttpClients.createDefault();
	try (CloseableHttpResponse response = client.execute(new HttpGet(uri))) {
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	        try (FileOutputStream outstream = new FileOutputStream(myFile)) {
	            entity.writeTo(outstream);
	        }
	    }
	}
	
	/*File file = new File(savedFilePath);
	Response execute = Request.Get(uri).execute();
	execute.saveContent(file);*/
	
	/*try (final CloseableHttpClient client = createHttpClient()) {
	     HttpEntity httpEntity = getEntity(client);
	     return downloadFile(httpEntity, targetDirectory, fileName);
	}*/

}
}