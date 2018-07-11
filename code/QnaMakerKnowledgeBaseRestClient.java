package gr.generali.gbox.api.data.ws.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Christodoulos.Rokos
 *
 */
public class QnaMakerKnowledgeBaseRestClient {
	
private static final  Logger logger = LoggerFactory.getLogger(QnaMakerKnowledgeBaseRestClient.class);
	
	private static String baseURI = "https://westus.api.cognitive.microsoft.com/qnamaker";	
	
	private static String host = "";
	
	private static String subscriptionKey = "";
	
	private static String endpoint_key = "6";
	
	private static String primarysKnowledgeBaseKey = "";
	
	public JSONObject create(JSONObject createJSONObject) throws Exception{
		
		JSONObject restOutput = null;

        try
        {
        	HttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(baseURI+"/v4.0/knowledgebases/create");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            StringEntity reqEntity = new StringEntity(createJSONObject.toString());
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                String entityString = EntityUtils.toString(entity);
                restOutput = new JSONObject(entityString);
            }       
  
    	}catch (Exception e) {	
        	logger.error("Error while running QnaMakerKnowledgeBaseRestClient.create() with input: " + createJSONObject.toString(), e);
    	}
        return restOutput; 
	}
	
    public JSONObject getEndpointkeys() throws Exception{
    	
    	JSONObject restOutput = null;
        try
        {
        	HttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(baseURI + "/v4.0/endpointkeys");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
           
            if (entity != null) 
            {
            	String entityString = EntityUtils.toString(entity);
            	restOutput = new JSONObject(entityString);
            }       
        }catch (Exception e) {	
        	logger.error("Error while running QnaMakerKnowledgeBaseRestClient.getEndpointkeys() ", e);
    	}
        return restOutput;  
    }
	
    public JSONObject getAnswers(String kb, String question) throws Exception {
    	JSONObject restOutput = null;
        try
        {
	        URL url = new URL(host + "/knowledgebases/" + kb + "/generateAnswer");
	        String answers = getAnswersPost(url, question);
	        if (answers!=null){
	        	restOutput = new JSONObject(answers);
	        }	        
        }catch (Exception e) {	
        	logger.error("Error while running QnaMakerKnowledgeBaseRestClient.getAnswers() with kb: " + kb + " and question:" + question, e);
    	}
        return restOutput;  
    }
    
    public JSONObject getPrimarysKnowledgeBaseAnswers(JSONObject question) throws Exception {
    	JSONObject restOutput = null;
        try
        {
	        URL url = new URL(host + "/knowledgebases/" + primarysKnowledgeBaseKey + "/generateAnswer");	
	        String answers = getAnswersPost(url, question.toString());
	        if (answers!=null){
	        	restOutput = new JSONObject(answers);
	        }	
        }catch (Exception e) {	
        	logger.error("Error while running QnaMakerKnowledgeBaseRestClient.getPrimarysKnowledgeBaseAnswers() with kb: " + primarysKnowledgeBaseKey + " and question:" + question, e);
    	}
        return restOutput;  

    }
    
    private static String getAnswersPost(URL url, String content) throws Exception {
    	String restOutput = null;
    	
    	try{
    		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty("Content-Length", content.length() + "");
	        connection.setRequestProperty("Authorization", "EndpointKey " + endpoint_key);
	        connection.setDoOutput(true);
	
	        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	        byte[] encoded_content = content.getBytes("UTF-8");
	        wr.write(encoded_content, 0, encoded_content.length);
	        wr.flush();
	        wr.close();
	
	        StringBuilder response = new StringBuilder ();
	        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
	        BufferedReader in = new BufferedReader(inputStreamReader);
	
	        String line;
	        while ((line = in.readLine()) != null) {
	            response.append(line);
	        }
	        in.close();
	
	        restOutput = response.toString();
	        
    	}catch (Exception e) {	
        	logger.error("Error while running QnaMakerKnowledgeBaseRestClient.getPrimarysKnowledgeBaseAnswers() with url: " + url + " and content:" + content, e);
    	}
    	return restOutput;
    }
}
