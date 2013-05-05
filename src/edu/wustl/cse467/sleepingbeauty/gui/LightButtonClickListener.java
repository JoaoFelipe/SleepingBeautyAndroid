/*		LightButtonClickListener.java
 * Purpose: Midterm Demo/Final Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Description:
 * 	This represents the lights button.
 * Version log:
 *	  4/30/2013, Joao Felipe
 *    	Refactoring LightInformation to possibility change from web and from bluetooth
 *      Sending light information via bluetooth
 */
package edu.wustl.cse467.sleepingbeauty.gui;

import java.util.Date;
import java.util.HashMap;

import edu.wustl.cse467.sleepingbeauty.bluetooth.BluetoothApi;
import edu.wustl.cse467.sleepingbeauty.http.LightButtonPostRequestAsync;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LightButtonClickListener implements OnClickListener {

	public static String URL = "http://sleepingbeauty.herokuapp.com/light_power";
	
	private ImageView imageView;
	private BluetoothApi bluetooth;
	private LightInformation lightInformation;
	
	/*
	 * Constructor
	 * Receives the light ToggleButton
	 */
	public LightButtonClickListener(ImageView imageView, LightInformation lightInformation, BluetoothApi bluetooth){
		this.lightInformation = lightInformation;
		this.imageView = imageView;
		this.bluetooth = bluetooth;
	}

	/*
	 * onClick
	 * Clicking on the light button
	 */
	@Override
	public void onClick(View v) {
		if (!LightStatus.buttonClicked) {
			HashMap<String, String> data = new HashMap<String, String>();
			long time = new Date().getTime();
			data.put("light_power[time]", time+"");
			data.put("light_power[on]", lightInformation.isChecked()?"1":"0");
			bluetooth.writeLight(time, lightInformation.isChecked());
			new LightButtonPostRequestAsync(data, imageView).execute(URL);
		} else {
			lightInformation.setChecked(!lightInformation.isChecked());
		}
	}
	
}
