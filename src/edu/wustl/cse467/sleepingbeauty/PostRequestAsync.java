/*		PostRequestAsync.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 23, 2013
 * 
 * Description:
 * 	This class realizes an asynchronous post to the given URL with the given parameters
 * Version log:
 * 	3/24/2013, Joao Felipe
 * 		Refactoring to make the BasicNameValuePair in the constructor
 */

package edu.wustl.cse467.sleepingbeauty;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class PostRequestAsync extends AsyncTask<String, String, String> {
	private ArrayList<BasicNameValuePair> attributes = null;
	
	/* Class constructor
	 * 	Receives the POST data as a HashMap
	 */
	public PostRequestAsync(HashMap<String, String> data) {
		attributes = new ArrayList<BasicNameValuePair>();
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			attributes.add(new BasicNameValuePair(key, data.get(key)));
		}
		
	}
	
	/*
	 * doInBackground
	 * 	Receives the url string
	 * 	Returns a string of the post result
	 * 
	 * 	This method executes the post request
	 */
	@Override
	protected String doInBackground(String... params) {
		byte[] resultByteArray = null;
		String resultString = "";
		String url = params[0];
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(attributes, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
				resultByteArray = EntityUtils.toByteArray(response.getEntity());
				resultString = new String(resultByteArray, "UTF-8");
				return resultString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
