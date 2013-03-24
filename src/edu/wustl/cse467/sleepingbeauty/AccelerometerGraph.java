/*		AccelerometerGraph.java
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
 * 	This is the Accelerometer Graph.
 */

package edu.wustl.cse467.sleepingbeauty;

import android.content.Context;
import android.graphics.Color;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class AccelerometerGraph extends LineGraphView{

	static String TITLE = "";
	static long VIEW_PORT_SIZE = 100000;
	
	private GraphViewSeries xSeries;
	private GraphViewSeries ySeries;
	private GraphViewSeries zSeries;
	
	private double absMax;
	
	/*
	 * Graph Constructor
	 * Instantiate series, and variables used by the Graph
	 * Defines the Style of the Graph(Scalable, with legend)
	 */
	public AccelerometerGraph(Context context) {
		super(context, TITLE);
		
		System.out.println("Entrei");
		xSeries = new GraphViewSeriesLimited("X Axis", new GraphViewSeriesStyle(Color.RED, 2)); 
		ySeries = new GraphViewSeriesLimited("Y Axis", new GraphViewSeriesStyle(Color.BLUE, 2));  
		zSeries = new GraphViewSeriesLimited("Z Axis", new GraphViewSeriesStyle(Color.GREEN, 2)); 
		
		this.addSeries(xSeries);
		this.addSeries(ySeries);
		this.addSeries(zSeries);
		
		this.setViewPort(0, VIEW_PORT_SIZE);
		this.setScalable(true);
		this.setShowLegend(true);
		this.setLegendAlign(LegendAlign.TOP);
		this.setManualYAxis(true);
		
		this.absMax = 0.0;
	}

	/*
	 * Append data to graph
	 * Receives currentTime (Graph x-axis value)
	 *          array of float values: values of the sensors
	 *          scrollToEnd: boolean indicating if Auto Scroll is enable
	 */
	public void appendData(long currentTime, float[] values, boolean scrollToEnd) {
		absMax = Math.max(absMax, values[0]);
		absMax = Math.max(absMax, values[1]);
		absMax = Math.max(absMax, values[2]);
		
		xSeries.appendData(new GraphViewData(currentTime, values[0]), false);
		ySeries.appendData(new GraphViewData(currentTime, values[1]), false);
		zSeries.appendData(new GraphViewData(currentTime, values[2]), false);
		
		if (scrollToEnd) {
			this.scrollToEnd();
			this.setManualYAxisBounds(absMax, -absMax);
		}
		this.invalidate();
	}
	
	/*
	 * Zoom In
	 * Receives the amount of zoom (value)
	 */
	public void zoomIn(double value) {
		if (absMax > value) {
			absMax -= value;
			this.setManualYAxisBounds(absMax, -absMax);
			this.redrawAll();
			this.invalidate();
		}
	}
	
	/*
	 * Zoom Out
	 * Receives the amount of zoom (value)
	 */
	public void zoomOut(double value) {
		absMax += value;
		this.setManualYAxisBounds(absMax, -absMax);
		this.redrawAll();
		this.invalidate();
	}
	
}
