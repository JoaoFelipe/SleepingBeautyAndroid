/*		AccelerometerSensorListener.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Invariants:
 * 	URL should be a valid URL
 * 	TIME > 0
 * Description:
 * 	This class represents the accelerometer sensor.
 * 	It gets the accelerometer data, calculate the derived data
 * 	and append to the graph
 * Version log:
 * 	3/24/2013, Joao Felipe and Tiago Pimentel (t.pimentelms@gmail.com)
 * 		Adding the derived information 
 *    3/26/2013, Joao Felipe
 * 		Grouping accelerometer data and sending by time
 *    3/27/2013, Joao Felipe
 *    	Stop sending accelerometer data to website
 */
package edu.wustl.cse467.sleepingbeauty.sensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wustl.cse467.sleepingbeauty.graph.CustomGraphView;
import edu.wustl.cse467.sleepingbeauty.http.PostRequestAsync;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AccelerometerSensorListener implements SensorEventListener, Runnable {

	public static String URL = "http://sleepingbeauty.herokuapp.com/accel_datas";
	public static int TIME = 20;
	public static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	private CustomGraphView graphView;
	private TextView accelerationText;
	private CheckBox autoScrollCheck;
	private ImageView imageView;
	
	private long firstTime;
	
	private float[] previousValues;
	private long previousTime;
	
	private List<TimeValues> accelValues;
	
	/*
	 * Constructor
	 * Receives a graph, a TextView (that shows the values of current acceleration), a check box (auto scroll), and a image (internet status)
	 */
	public AccelerometerSensorListener(
			CustomGraphView graphView, 
			TextView accelerationText, CheckBox autoScrollCheck,  
			ImageView imageView) {
		this.graphView = graphView;
		this.accelerationText = accelerationText;
		this.autoScrollCheck = autoScrollCheck;
		this.imageView = imageView;
		
		firstTime = new Date().getTime();
		previousValues = new float[] {
				0, 0, 0
		};
		
		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		scheduleTaskExecutor.scheduleAtFixedRate(this, 0, TIME, TIME_UNIT);
		
		accelValues = new ArrayList<TimeValues>();
		
	}
	/*
	 * This method is called when the accuracy of the sensor changes
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/*
	 * This method is called when it receives a value from the sensor
	 * The param event has the attribute values that represents the values of the sensors
	 * When it is called, it updates the graph, the label and add values to the accelValues
	 * It also calculates the derived data
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		long timestamp = new Date().getTime();
		
		accelValues.add(new TimeValues(event.values, timestamp));
		
		long currentTime = timestamp - firstTime;
		graphView.appendAccelerometerData(currentTime, event.values, autoScrollCheck.isChecked());
	   
		long deltaTime = currentTime - previousTime;
		float[] newValues =  new float[] {
			0,0,0
		};
		if (deltaTime > 0) {
			newValues[0] = (event.values[0] - previousValues[0])/deltaTime;
			newValues[1] = (event.values[1] - previousValues[1])/deltaTime;
			newValues[2] = (event.values[2] - previousValues[2])/deltaTime;
		} 
		
		graphView.appendDerivedData(currentTime, newValues, autoScrollCheck.isChecked());
		
		
		if (graphView.getCurrentGraph() == 0) {
			accelerationText.setText("X: " + event.values[0]+
				"\nY: " + event.values[1]+
				"\nZ: " + event.values[2]);
		} else if (graphView.getCurrentGraph() == 1) {
			accelerationText.setText("X: " + newValues[0]+
				"\nY: " + newValues[1]+
				"\nZ: " + newValues[2]);
		}
		
		System.arraycopy(event.values, 0, previousValues, 0, 3);
		previousTime = currentTime;
		
		
	}
	
	/*
	 * run
	 * This method executes each TIME TIME_UNIT
	 * It posts the acquired data to the URL
	 */
	@Override
	public void run() {
		HashMap<String, String> data = new HashMap<String, String>();
		for (int i = 0; i < accelValues.size(); i++) {
			accelValues.get(i).addToPostData("accel_data", data, i);
		}
		accelValues.clear();
		PostRequestAsync post = new PostRequestAsync(data, imageView);
		post.execute(URL);	
	}

}
