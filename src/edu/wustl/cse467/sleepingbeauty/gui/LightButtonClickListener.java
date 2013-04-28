/*		LightButtonClickListener.java
 * Purpose: Midterm Demo/Changed for Final Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This represents the lights button.
 * Version Log:
 *		4/21/2013, Joao Felipe
 *			Using URL from MainActivity, allowing the URL configuration
 */
package edu.wustl.cse467.sleepingbeauty.gui;

import java.util.Date;
import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.MainActivity;
import edu.wustl.cse467.sleepingbeauty.http.LightButtonPostRequestAsync;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class LightButtonClickListener implements OnClickListener {

	public static String URL_PATH = "/light_power";
	
	private ToggleButton lightsButton;
	private ImageView imageView;
	
	/*
	 * Constructor
	 * Receives the light ToggleButton
	 */
	public LightButtonClickListener(ImageView imageView, ToggleButton lightsButton){
		this.lightsButton = lightsButton;
		this.imageView = imageView;
	}

	/*
	 * onClick
	 * Clicking on the light button
	 */
	@Override
	public void onClick(View v) {
		if (!LightStatus.buttonClicked) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("light_power[time]", new Date().getTime()+"");
			data.put("light_power[on]", lightsButton.isChecked()?"1":"0");
			new LightButtonPostRequestAsync(data, imageView).execute(MainActivity.URL+URL_PATH);
		} else {
			lightsButton.setChecked(!lightsButton.isChecked());
		}
	}
	
}
