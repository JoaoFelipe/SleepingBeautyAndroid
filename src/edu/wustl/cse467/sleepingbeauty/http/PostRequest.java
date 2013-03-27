/*		PostRequest.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 25, 2013
 * 
 * Description:
 * 	This class realizes an get request to the given URL with the given parameters
 * Version Log:
 * 	3/26/2013, Joao Felipe
 * 		Refactoring to isolate HTTP request from POST request and create the GET request
 */

package edu.wustl.cse467.sleepingbeauty.http;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class PostRequest extends HttpRequest {
	
	/* Class constructor
	 * 	Receives the POST data as a HashMap
	 */
	public PostRequest(HashMap<String, String> data) {
		super(data);
	}
	
	/*
	 * getResponse
	 * Receives the url and executes the post request
	 * Returns a response object for the HttpRequest execute function
	 */
	@Override
	protected HttpResponse getResponse(String url) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(attributes, "UTF-8"));
		return client.execute(post);
	}
}
