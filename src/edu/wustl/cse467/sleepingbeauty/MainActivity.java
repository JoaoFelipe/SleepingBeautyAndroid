/*		MainActivity.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * 			Tiago Pimentel
 * 		   t.pimentelms@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 23, 2013
 * 
 * Invariants:
 * 	URL should a valid URL
 * Description:
 * 	This is the program application. 
 * 	It instantiates the sensor, the graph and the zoom and obtains the values from the sensor.
 * 	For each obtained value, it updates the graph and sends a post request to the URL
 * Version log:
 * 	3/24/2013, Joao Felipe
 * 		Refactoring to separate the Graph and Zoom functionalities from this file
 */

package edu.wustl.cse467.sleepingbeauty;

import java.util.Date;
import java.util.HashMap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;
import android.app.Activity;

public class MainActivity extends Activity implements SensorEventListener {

	public static String URL = "http://sleepingbeauty.heroku.com";
	
	private Sensor accelerometer;
	private SensorManager sensorManager;
	private AccelerometerGraph graphView;
	
	private TextView accelerationText;
	private CheckBox autoScrollCheck;
	private LinearLayout layout;
	
	long firstTime;
	
	/*
	 * This method is called after starting the application. 
	 * It instantiates the sensor, the graph and the layout elements that will be accessed
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		System.out.println("Entrei");
		graphView = new AccelerometerGraph(this);
		new AccelerometerGraphZoomControls(graphView, (ZoomControls) findViewById(R.id.zoom));
		
		autoScrollCheck = (CheckBox) findViewById(R.id.autoscroll);
		accelerationText = (TextView) findViewById(R.id.acceleration);
		
		layout = (LinearLayout) findViewById(R.id.linear);  
		layout.addView(graphView);  
		
		firstTime = new Date().getTime();
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
	 * When it is called, it updates the graph, the label and post the data to the URL
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		long timestamp = new Date().getTime();
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("accel_data[x]", event.values[0]+"");
		data.put("accel_data[y]", event.values[1]+"");
		data.put("accel_data[z]", event.values[2]+"");
		data.put("accel_data[measure_time]", timestamp+"");
		
		long currentTime = timestamp - firstTime;
		graphView.appendData(currentTime, event.values, autoScrollCheck.isChecked());
		
		accelerationText.setText("X: " + event.values[0]+
				"\nY: " + event.values[1]+
				"\nZ: " + event.values[2]);
		
		PostRequestAsync post = new PostRequestAsync(data);
		post.execute(URL);	
	}

}
