/*		LightButtonPostRequestAsync.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This class realizes an asynchronous post to the given URL with the given parameters
 * 	This will set the button click to true, and avoid getting the button click status
 */

package edu.wustl.cse467.sleepingbeauty.http;


import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.gui.LightStatus;
import android.widget.ImageView;

public class LightButtonPostRequestAsync extends PostRequestAsync {

	/* Class constructor
	 * 	Receives the POST data as a HashMap
	 */
	public LightButtonPostRequestAsync(HashMap<String, String> data, ImageView imageView) {
		super(data, imageView);
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
		LightStatus.buttonClicked = true;
		return super.doInBackground(params);
	}
	
	@Override
	protected void onPostExecute(String r) {
		super.onPostExecute(r);
		LightStatus.buttonClicked = false;
	}

}
