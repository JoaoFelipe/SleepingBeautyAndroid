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
 *    3/25/2013, Joao Felipe
 *    	Refactoring to isolate PostRequest from the AsyncTask
 *       Adding an ImageView to check show the internet status when the post fails
 */

package edu.wustl.cse467.sleepingbeauty.http;


import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.R;

import android.os.AsyncTask;
import android.widget.ImageView;

public class PostRequestAsync extends AsyncTask<String, String, String> {

	private PostRequest postRequest;
	private ImageView imageView;
	
	/* Class constructor
	 * 	Receives the POST data as a HashMap
	 */
	public PostRequestAsync(HashMap<String, String> data, ImageView imageView) {
		postRequest = new PostRequest(data);
		this.imageView = imageView;
	}
	
	/*
	 * doInBackground
	 * 	Receives the url string
	 * 	Returns a string of the post result
	 * 
	 * 	Calls postRequest.execute
	 */
	@Override
	protected String doInBackground(String... params) {
		return postRequest.execute(params[0]);
	}
	
	@Override
	protected void onPostExecute(String r) {
		if (postRequest.status == 200 || postRequest.status == 406) {
			imageView.setImageResource(R.drawable.green);
		} else {
			imageView.setImageResource(R.drawable.red);
			System.out.println("stat "+postRequest.status);
			System.out.println(r);
		}
	}

}
