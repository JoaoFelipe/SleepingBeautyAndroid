/*		LightGetRequestAsync.java
 * Purpose: Midterm Demo/Changed for Final Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This class realizes an asynchronous get to the light URL to check its status
  * Version Log:
 *		4/21/2013, Joao Felipe
 *			Using URL from MainActivity, allowing the URL configuration
 */

package edu.wustl.cse467.sleepingbeauty.http;


import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.MainActivity;
import edu.wustl.cse467.sleepingbeauty.R;
import edu.wustl.cse467.sleepingbeauty.gui.LightStatus;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class LightGetRequestAsync extends AsyncTask<String, String, String> {

	public static String URL_PATH = "/light_power/last.txt";
	
	private GetRequest getRequest;
	private ImageView imageView;
	private ToggleButton toggleButton;
	
	
	/* Class constructor
	 * 	Receives the image of internet status and the light button
	 */
	public LightGetRequestAsync(ImageView imageView, ToggleButton toggleButton) {
		getRequest = new GetRequest(new HashMap<String, String>());
		this.imageView = imageView;
		this.toggleButton = toggleButton;
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
		return getRequest.execute(MainActivity.URL + URL_PATH);
	}
	
	@Override
	protected void onPostExecute(String r) {
		if (getRequest.status == 200 || getRequest.status == 406) {
			imageView.setImageResource(R.drawable.green);
			if (!LightStatus.buttonClicked) {
				if (r.equals("0")) {
					toggleButton.setChecked(false);
				} else {
					toggleButton.setChecked(true);
				}
			}
		} else {
			imageView.setImageResource(R.drawable.red);
			System.out.println("stat"+getRequest.status);
		}
		
	}

}
