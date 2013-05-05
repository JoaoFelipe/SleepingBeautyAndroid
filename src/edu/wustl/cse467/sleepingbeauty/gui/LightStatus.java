/*		LightStatus.java
 * Purpose: Midterm Demo/Final Demo
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
  * Version log:
 *	  4/30/2013, Joao Felipe
 *    	Refactoring LightInformation to possibility change from web and from bluetooth
 *      Sending light request via bluetooth
 */

package edu.wustl.cse467.sleepingbeauty.gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wustl.cse467.sleepingbeauty.bluetooth.BluetoothApi;
import edu.wustl.cse467.sleepingbeauty.http.LightGetRequestAsync;
import android.widget.ImageView;

public class LightStatus implements Runnable {

	static int SECONDS = 1;
	public static boolean buttonClicked = false;
	
	ImageView internetStatus;
	BluetoothApi bluetooth;
	private LightInformation lightInformation;
	
	public LightStatus(ImageView internetStatus, LightInformation lightInformation, BluetoothApi bluetooth){
		this.internetStatus = internetStatus;
		this.lightInformation = lightInformation;
		this.bluetooth = bluetooth;
		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		scheduleTaskExecutor.scheduleAtFixedRate(this, 0, SECONDS, TimeUnit.SECONDS);
	}
	
	@Override
	public void run() {
		if (!buttonClicked) {
			bluetooth.requestLight();
			new LightGetRequestAsync(internetStatus, lightInformation).execute();
		}
		
	}
	

}
