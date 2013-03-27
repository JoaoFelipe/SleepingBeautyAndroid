/*		CustomGraphView.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Invariants:
 * 	TITLE is the Graph title. With the current layout, it may not be used
 * 	VIEW_PORT_SIZE is the amount of X values per view. 1 <= VIEW_PORT_SIZE 
 * Description:
 * 	This is the Graph class. It has methods to set which graph use, to append data to graphs and to zoomIn or zoomOut
 * Version Log:
 * 	3/24/2013, Joao Felipe
 * 		Limiting the amount of data of the graph
 * 		Making the graph work with accelerometer data, derived data, linear data, derivedLinear data
 */

package edu.wustl.cse467.sleepingbeauty.graph;

import android.content.Context;
import com.jjoe64.graphview.LineGraphView;

public class CustomGraphView extends LineGraphView{

	private static String TITLE = "";
	private static long VIEW_PORT_SIZE = 100000;
	
	private GraphValues accelerometer;
	private GraphValues derived;
	private GraphValues linear;
	private GraphValues derivedLinear;

	private int currentGraph;
	
	/*
	 * Graph Constructor
	 * Instantiate series, and variables used by the Graph
	 * Defines the Style of the Graph(Scalable, with legend)
	 * Defines the first graph to be accelerometer
	 */
	public CustomGraphView(Context context) {
		super(context, TITLE);
		
		accelerometer = new GraphValues(this);
		derived = new GraphValues(this, 0.01);
		linear = new GraphValues(this, 0.5);
		derivedLinear = new GraphValues(this, 0.01);
		
		this.useAccelerometer();
		
		this.setViewPort(0, VIEW_PORT_SIZE);
		this.setScalable(true);
		this.setShowLegend(true);
		this.setLegendAlign(LegendAlign.TOP);
		this.setManualYAxis(true);
	}
	
	/*
	 * useAccelerometer
	 * set the graph to show accelerometer data
	 */
	public void useAccelerometer() {
		currentGraph = 0;
		accelerometer.setVisible(true);
		derived.setVisible(false);
		linear.setVisible(false);	
		derivedLinear.setVisible(false);
		
	}
	
	/*
	 * useDerived
	 * set the graph to show derived accelerometer data
	 */
	public void useDerived() {
		currentGraph = 1;
		accelerometer.setVisible(false);
		derived.setVisible(true);
		linear.setVisible(false);
		derivedLinear.setVisible(false);
	}
	
	/*
	 * useLinear
	 * set the graph to show linear acceleration data
	 */
	public void useLinear() {
		currentGraph = 2;
		accelerometer.setVisible(false);
		derived.setVisible(false);
		linear.setVisible(true);
		derivedLinear.setVisible(false);
	}
	
	/*
	 * useDerivedLinear
	 * set the graph to show derived linear acceleration data
	 */
	public void useDerivedLinear() {
		currentGraph = 3;
		accelerometer.setVisible(false);
		derived.setVisible(false);
		linear.setVisible(false);
		derivedLinear.setVisible(true);
	}
	
	/*
	 * Get current showed graph 
	 * Returns
	 * 	0 - Accelerometer
	 * 	1 - Derived Accelerometer
	 * 	2 - Linear Acceleration
	 * 	3 - Derived Linear Acceleration
	 */
	public int getCurrentGraph() {
		return currentGraph;
	}

	/*
	 * Append accelerometer data to graph
	 * Receives currentTime (Graph x-axis value)
	 *          array of float values: values of the accelerometer sensor
	 *          scrollToEnd: boolean indicating if Auto Scroll is enable
	 */
	public void appendAccelerometerData(long currentTime, float[] values, boolean scrollToEnd) {
		accelerometer.appendData(currentTime, values, scrollToEnd);
	}
	
	/*
	 * Append accelerometer data to graph
	 * Receives currentTime (Graph x-axis value)
	 *          array of float values: values of the derived accelerometer
	 *          scrollToEnd: boolean indicating if Auto Scroll is enable
	 */
	public void appendDerivedData(long currentTime, float[] values, boolean scrollToEnd) {
		derived.appendData(currentTime, values, scrollToEnd);
	}
	
	/*
	 * Append accelerometer data to graph
	 * Receives currentTime (Graph x-axis value)
	 *          array of float values: values of the linear acceleration
	 *          scrollToEnd: boolean indicating if Auto Scroll is enable
	 */
	public void appendLinearData(long currentTime, float[] values, boolean scrollToEnd) {
		linear.appendData(currentTime, values, scrollToEnd);
	}
	
	/*
	 * Append accelerometer data to graph
	 * Receives currentTime (Graph x-axis value)
	 *          array of float values: values of the derived linear acceleration
	 *          scrollToEnd: boolean indicating if Auto Scroll is enable
	 */
	public void appendDerivedLinearData(long currentTime, float[] values, boolean scrollToEnd) {
		derivedLinear.appendData(currentTime, values, scrollToEnd);
	}
	
	/*
	 * Zoom In
	 * Receives the amount of zoom (value)
	 */
	public void zoomIn(double value) {
		if (currentGraph == 0) {
			accelerometer.zoomIn(value);
		} else if (currentGraph == 1) {
			derived.zoomIn(value);
		} else if (currentGraph == 2) {
			linear.zoomIn(value);
		} else {
			derivedLinear.zoomIn(value);
		}
	}
	
	/*
	 * Zoom Out
	 * Receives the amount of zoom (value)
	 */
	public void zoomOut(double value) {
		if (currentGraph == 0) {
			accelerometer.zoomOut(value);
		} else if (currentGraph == 1) {
			derived.zoomOut(value);
		} else if (currentGraph == 2) {
			linear.zoomOut(value);
		} else {
			derivedLinear.zoomOut(value);
		}
	}
	
}
