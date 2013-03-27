/*		MainActivity.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 23, 2013
 * 
 * Description:
 * 	This is the program application. 
 * 	It instantiates the sensors, the graph and the interface
 * Version log:
 * 	3/24/2013 v1, Joao Felipe
 * 		Refactoring to separate the Graph and Zoom functionalities from this file
 *       Refactoring to separate Accelerometer sensor from this file
 *    3/24/2013 v2, Joao Felipe and Tiago Pimentel (t.pimentelms@gmail.com)
 *    	Adding zoom buttons
 *       Adding Image for internet check
 *       Adding Linear Acceleration sensor
 *       Adding derived information
 *    3/24/2013 v3, Joao Felipe
 *       Adding menu for different graphs
 *    	Refactoring to separate zoom functionality from this file
 *    3/25/2013, Joao Felipe and Tiago Pimentel
 *    	Adding button to turn the lights on and off
 *    3/26/2013, Joao Felipe
 *    	Refactoring to separate the light button from this file
 */

package edu.wustl.cse467.sleepingbeauty;


import edu.wustl.cse467.sleepingbeauty.graph.CustomGraphView;
import edu.wustl.cse467.sleepingbeauty.gui.GraphZoomControls;
import edu.wustl.cse467.sleepingbeauty.gui.LightButtonClickListener;
import edu.wustl.cse467.sleepingbeauty.gui.LightStatus;
import edu.wustl.cse467.sleepingbeauty.sensor.AccelerometerSensorListener;
import edu.wustl.cse467.sleepingbeauty.sensor.LinearSensorListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ZoomControls;
import android.app.Activity;

public class MainActivity extends Activity {

	public static String URL = "http://sleepingbeauty.heroku.com";

	CustomGraphView graphView;
	TextView graphTitle;
	
	/*
	 * This method is called after starting the application. 
	 * It instantiates the sensor, the graph and the layout elements that will be accessed
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("Start here");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		CheckBox autoScrollCheck = (CheckBox) findViewById(R.id.autoscroll);
		TextView accelerationText = (TextView) findViewById(R.id.acceleration);
		ImageView imageView = (ImageView) findViewById(R.id.wifistatus);
		
		ToggleButton lightsButton = (ToggleButton) findViewById(R.id.lightsButton);
		lightsButton.setOnClickListener(new LightButtonClickListener(imageView, lightsButton));
		
		graphView = new CustomGraphView(this);
		new GraphZoomControls(graphView, (ZoomControls) findViewById(R.id.zoom));
		
		graphTitle = (TextView) findViewById(R.id.graphtitle);
		
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(
				new AccelerometerSensorListener(graphView, accelerationText, autoScrollCheck, imageView), 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(
				new LinearSensorListener(graphView, accelerationText, autoScrollCheck, imageView), 
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 
				SensorManager.SENSOR_DELAY_NORMAL);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear);  
		layout.addView(graphView);  
		
		new LightStatus(imageView, lightsButton);
	}

	/*
	 * onCreateOptionsMenu
	 * Show the menu to select graph when clicked on menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*
	 * onOptionsItemSelected
	 * Set the graph according to selectted item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // Handle item selection
	   switch (item.getItemId()) {
	      case R.id.item1:
	      	graphView.useAccelerometer();
	      	graphTitle.setText(getString(R.string.accelerometer_title));
	         return true;
	      case R.id.item2:
	      	graphView.useDerived();
	      	graphTitle.setText(getString(R.string.derived_title));
            return true;
	      case R.id.item3:
	      	graphView.useLinear(); 
	      	graphTitle.setText(getString(R.string.linear_title));
	      	return true;
	      case R.id.item4:
	      	graphView.useDerivedLinear(); 
	      	graphTitle.setText(getString(R.string.derived_linear_title));
	      	return true;
	      default:
	         return super.onOptionsItemSelected(item);
	    }
	}
	
}
