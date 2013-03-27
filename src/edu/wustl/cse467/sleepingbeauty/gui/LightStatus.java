/*		LightStatus.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Invariants:
 * 	SECONDS > 0
 * Description:
 * 	This class checks the light status for each SECONDS seconds
 */

package edu.wustl.cse467.sleepingbeauty.gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wustl.cse467.sleepingbeauty.http.LightGetRequestAsync;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class LightStatus implements Runnable {

	static int SECONDS = 1;
	public static boolean buttonClicked = false;
	
	ImageView internetStatus;
	ToggleButton lightButton;
	
	public LightStatus(ImageView internetStatus, ToggleButton lightButton){
		this.internetStatus = internetStatus;
		this.lightButton = lightButton;
		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		scheduleTaskExecutor.scheduleAtFixedRate(this, 0, SECONDS, TimeUnit.SECONDS);
	}
	
	@Override
	public void run() {
		if (!buttonClicked) {
			new LightGetRequestAsync(internetStatus, lightButton).execute();
		}
		
	}
	

}
