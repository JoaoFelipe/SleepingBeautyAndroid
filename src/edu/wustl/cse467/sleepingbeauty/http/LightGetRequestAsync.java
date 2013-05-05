/*		LightGetRequestAsync.java
 * Purpose: Midterm Demo/Final Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This class realizes an asynchronous get to the light URL to check its status
  * Version log:
 *	  4/30/2013, Joao Felipe
 *    	Refactoring LightInformation to possibility change from web and from bluetooth
 */

package edu.wustl.cse467.sleepingbeauty.http;


import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.R;
import edu.wustl.cse467.sleepingbeauty.gui.LightInformation;
import edu.wustl.cse467.sleepingbeauty.gui.LightStatus;

import android.os.AsyncTask;
import android.widget.ImageView;

public class LightGetRequestAsync extends AsyncTask<String, String, String> {

	public static String URL = "http://sleepingbeauty.herokuapp.com/light_power/last.txt";
	
	private GetRequest getRequest;
	private ImageView imageView;
	private LightInformation lightInformation;
	
	
	/* Class constructor
	 * 	Receives the image of internet status and the light button
	 */
	public LightGetRequestAsync(ImageView imageView, LightInformation lightInformation) {
		getRequest = new GetRequest(new HashMap<String, String>());
		this.imageView = imageView;
		this.lightInformation = lightInformation;
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
		return getRequest.execute(URL);
	}
	
	@Override
	protected void onPostExecute(String r) {
		if (getRequest.status == 200 || getRequest.status == 406) {
			imageView.setImageResource(R.drawable.green);
			if (!LightStatus.buttonClicked) {
				String[] parts = r.split(",");
				if (parts.length < 2) {
					System.out.println("Ops: " + r);
					return;
				}
				lightInformation.setLight(Long.parseLong(parts[1]), parts[0].equals("1"));
			}
		} else {
			imageView.setImageResource(R.drawable.red);
			System.out.println("stat"+getRequest.status);
		}
		
	}

}
