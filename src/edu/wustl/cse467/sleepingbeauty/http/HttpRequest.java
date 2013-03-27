/*		HttpRequest.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This class realizes an htpp request to the given URL with the given parameters
 * 	It is the core of GetRequest and PostRequest
 */

package edu.wustl.cse467.sleepingbeauty.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public abstract class HttpRequest {

	protected ArrayList<BasicNameValuePair> attributes = null;
	public String result;
	public int status;
	
	/* Class constructor
	 * 	Receives the data as a HashMap
	 */
	public HttpRequest(HashMap<String, String> data) {
		attributes = new ArrayList<BasicNameValuePair>();
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			attributes.add(new BasicNameValuePair(key, data.get(key)));
		}
	}
	
	/*
	 * Execute
	 * 	Receives the url string
	 * 	Returns a string of the post result
	 * 
	 * 	This method executes the http request
	 */
	public String execute(String url) {
		byte[] resultByteArray = null;
		String resultString = "";
		try {
			HttpResponse response = getResponse(url);
			StatusLine statusLine = response.getStatusLine();
			status = statusLine.getStatusCode();
			if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK || statusLine.getStatusCode() == 406){
				resultByteArray = EntityUtils.toByteArray(response.getEntity());
				resultString = new String(resultByteArray, "UTF-8");
				return resultString;
			} 
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(status);
			status = 0;
			
			//e.printStackTrace();
		}
		return "";
	}
	
	protected abstract HttpResponse getResponse(String url) throws ClientProtocolException, IOException;
}

