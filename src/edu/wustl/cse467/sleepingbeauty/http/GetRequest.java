/*		GetRequest.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This class realizes an get request to the given URL with the given parameters
 */

package edu.wustl.cse467.sleepingbeauty.http;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
public class GetRequest extends HttpRequest {

	/* Class constructor
	 * 	Receives the GET data as a HashMap
	 */
	public GetRequest(HashMap<String, String> data) {
		super(data);
	}
	
	/*
	 * getResponse
	 * Receives the url and executes the get request
	 * Returns a response object for the HttpRequest execute function
	 */
	@Override
	protected HttpResponse getResponse(String url) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		String paramString = URLEncodedUtils.format(attributes, "utf-8");
		HttpGet get = new HttpGet(url+paramString);
		return client.execute(get);
	}
}
