package com.lockscreen.utility;

/*Developer: TAI ZHEN KAI
Project 2015*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class RestClient {
		 
	
	    public static final int GET = 0;
		public static final int POST = 1;
		private ArrayList <NameValuePair> params;
	    private ArrayList <NameValuePair> headers;
	 
	    private String url;
	 
	    public void setUrl(String url) {
			this.url = url;
		}

		private int responseCode;
	    private String message;
	 
	    private String response;
	 
	    public String getResponse() {
	        return response;
	    }
	 
	    public String getErrorMessage() {
	        return message;
	    }
	 
	    public int getResponseCode() {
	        return responseCode;
	    }
	 
	    public RestClient(String url)
	    {
	        this.url = url;
	        params = new ArrayList<NameValuePair>();
	        headers = new ArrayList<NameValuePair>();
	    }
	 
	    public void AddParam(String name, String value)
	    {
	        params.add(new BasicNameValuePair(name, value));
	    }
	 
	    public void AddHeader(String name, String value)
	    {
	        headers.add(new BasicNameValuePair(name, value));
	    }
	 
	    public void Execute(int method) throws Exception
	    {
	        switch(method) {
	            case GET:
	            {
	                //add parameters     	
	                String combinedParams = "";
	                if(!params.isEmpty()){
	                    combinedParams += "?";
	                    for(NameValuePair p : params)
	                    {
	                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
	                        if(combinedParams.length() > 1)
	                        {
	                            combinedParams  +=  "&" + paramString;
	                        }
	                        else
	                        {
	                            combinedParams += paramString;
	                        }
	                    }
	                }
	                HttpGet request = new HttpGet(url + combinedParams);
	                //add headers
	                for(NameValuePair h : headers)
	                {
	                    request.addHeader(h.getName(), h.getValue());
	                }
	                executeRequest(request, url); 
	                break;
	            }
	            case POST:
	            {
	                HttpPost request = new HttpPost(url);
	 
	                //add headers
	                for(NameValuePair h : headers)
	                {
	                    request.addHeader(h.getName(), h.getValue());
	                }
	 
	                if(!params.isEmpty()){
	                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	                }
	                executeRequest(request, url);      
	                break;
	            }
	        }
	    }
	 
	    public void ExecuteHybridJsonPost(String jsonString) throws Exception
	    {
	        HttpPost request = new HttpPost(url);
	       
	        //add headers
	        for(NameValuePair h : headers)
	        {
	            request.addHeader(h.getName(), h.getValue());
	            
	        }
	        StringEntity se = new StringEntity(jsonString, HTTP.UTF_8);    

	        request.setEntity(se);
	        executeRequest(request, url); 
	    }
	    
	    public void ExecuteHybridJsonDelete() throws Exception
	    {
	        HttpDelete request = new HttpDelete(url);
	       
	        //add headers
	        for(NameValuePair h : headers)
	        {
	            request.addHeader(h.getName(), h.getValue());
	        }
	       // StringEntity se = new StringEntity(jsonString);                
	       // request.setEntity(se);
	        executeRequest(request, url);          
	    }
	    
	    
	    public void ExecuteHybridJsonPut(String jsonString) throws Exception
	    {
	        HttpPut request = new HttpPut(url);
	 
	        //add headers
	        for(NameValuePair h : headers)
	        {
	            request.addHeader(h.getName(), h.getValue());
	        }
	        StringEntity se = new StringEntity(jsonString, HTTP.UTF_8);
	        request.setEntity(se);
	        executeRequest(request, url);
	        
	    }
	    
	    private void executeRequest(HttpUriRequest request, String url)
	    {
	        HttpClient client = new DefaultHttpClient();
	        HttpResponse httpResponse;
	        try {			
	            httpResponse = client.execute(request);    	
	            responseCode = httpResponse.getStatusLine().getStatusCode();
	            message = httpResponse.getStatusLine().getReasonPhrase();
	 
	            HttpEntity entity = httpResponse.getEntity();
	            if (entity != null) { 

	                response = EntityUtils.toString(entity, HTTP.UTF_8);
	            }
	        } catch (ClientProtocolException e)  {
	            client.getConnectionManager().shutdown();
	            e.printStackTrace();
	        } catch (IOException e) {
	            client.getConnectionManager().shutdown();
	            e.printStackTrace();
	        }
	    }
	 
	    private static String convertStreamToString(InputStream is) {
	 
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	 
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	    
	    
}
