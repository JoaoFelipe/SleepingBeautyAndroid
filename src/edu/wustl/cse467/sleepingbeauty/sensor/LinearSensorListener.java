/*		AccelerometerSensorListener.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * 			Tiago Pimentel
 * 		  t.pimentelms@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Invariants:
 * 	URL should be a valid URL
 * 	MINIMAL_IMPACT >= 0;
 *    STRONG_IMPACT >= MINIMAL_IMPACT;
 * 	TIME > 0
 * Description:
 * 	This class represents the linear acceleration sensor.
 * 	It gets the linear acceleration data,  calculate the derived data and append to the graph
 * 	If it recognizes an impact, it sends to the website
 * Version log:
 *    3/27/2013, Joao Felipe
 *    	Sending linear acceleration data to website when detect impact
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

public class LinearSensorListener implements SensorEventListener, Runnable {

	public static String URL = "http://sleepingbeauty.herokuapp.com/rough_movements";
	public static int TIME = 20;
	public static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	public static double MINIMAL_IMPACT = 0.00025;
	public static double STRONG_IMPACT = 0.01;
	
	private CustomGraphView graphView;
	private TextView accelerationText;
	private CheckBox autoScrollCheck;
	private ImageView imageView;
	
	private long firstTime;
	
	private float[] previousValues;
	private long previousTime;
	
	private List<RoughValues> roughValues;
	private boolean lastRough;
	
	/*
	 * Constructor
	 * Receives a graph, a TextView (that shows the values of current acceleration), a check box (auto scroll), and a image (internet status)
	 */
	public LinearSensorListener(
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
		
		roughValues = new ArrayList<RoughValues>();
		lastRough = false;
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
	 * When it is called, it updates the graph, and the label and calculates the derived linear acceleration
	 * If it recognizes an impact, it sends to the website. If recognizes rough impacts also
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		long timestamp = new Date().getTime();
		
		long currentTime = timestamp - firstTime;
		graphView.appendLinearData(currentTime, event.values, autoScrollCheck.isChecked());
		
		long deltaTime = currentTime - previousTime;
		
		float[] newValues =  new float[] {
			0,0,0
		};
		if (deltaTime > 0) {
			newValues[0] = (event.values[0] - previousValues[0])/deltaTime;
			newValues[1] = (event.values[1] - previousValues[1])/deltaTime;
			newValues[2] = (event.values[2] - previousValues[2])/deltaTime;
		} 
		
		if (Math.abs(newValues[0]) >= MINIMAL_IMPACT || Math.abs(newValues[1]) >= MINIMAL_IMPACT || Math.abs(newValues[2]) >= MINIMAL_IMPACT) {
			if (!lastRough) {
				roughValues.add(new RoughValues(false, timestamp, previousValues));
				lastRough = true;
			}
			boolean roughy = (Math.abs(newValues[0]) >= STRONG_IMPACT || Math.abs(newValues[1]) >= STRONG_IMPACT || Math.abs(newValues[2]) >= STRONG_IMPACT);
			roughValues.add(new RoughValues(roughy, timestamp, event.values));
			if (roughy) {
				this.run();
			}
		} else {
			if (lastRough) {
				roughValues.add(new RoughValues(false, timestamp, event.values));
				lastRough = false;
			}
		}
		
		graphView.appendDerivedLinearData(currentTime, newValues, autoScrollCheck.isChecked());
		
		
		if (graphView.getCurrentGraph() == 2) {
			accelerationText.setText("X: " + event.values[0]+
				"\nY: " + event.values[1]+
				"\nZ: " + event.values[2]);
		} else if (graphView.getCurrentGraph() == 3) {
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
		if (roughValues.size() > 0) {
			HashMap<String, String> data = new HashMap<String, String>();
			for (int i = 0; i < roughValues.size(); i++) {
				roughValues.get(i).addToPostData("rough_movs", data, i);
			}
			roughValues.clear();
			PostRequestAsync post = new PostRequestAsync(data, imageView);
			post.execute(URL);	
		}
	}

}
