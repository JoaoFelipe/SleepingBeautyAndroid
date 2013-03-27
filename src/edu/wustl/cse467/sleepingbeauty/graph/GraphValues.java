/*		GraphValues.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Description:
 * 	This class represents a set of 3 values (X, Y, Z)
 * 	It has functions to append data, zoom in, zoom out, set visibility
 */


package edu.wustl.cse467.sleepingbeauty.graph;

import android.graphics.Color;

import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class GraphValues {
	private GraphViewSeries xSeries;
	private GraphViewSeries ySeries;
	private GraphViewSeries zSeries;
	
	private double absMax;
	private boolean visible;
	private double factor = 1.0;
	
	CustomGraphView graphView;
	
	/*
	 * Constructor
	 * Receives the graphView and initializes the GraphSeries for X, Y, Z and the zoom
	 */
	public GraphValues(CustomGraphView graphView) {
		this.graphView = graphView;
		
		xSeries = new GraphViewSeriesLimited("X Axis", new GraphViewSeriesStyle(Color.RED, 2)); 
		ySeries = new GraphViewSeriesLimited("Y Axis", new GraphViewSeriesStyle(Color.BLUE, 2));  
		zSeries = new GraphViewSeriesLimited("Z Axis", new GraphViewSeriesStyle(Color.GREEN, 2)); 
		
		graphView.addSeries(xSeries);
		graphView.addSeries(ySeries);
		graphView.addSeries(zSeries);
		
		absMax = 0.0;
		visible = false;
	}
	
	/*
	 * Constructor
	 * Receives the graphView and initializes the GraphSeries for X, Y, Z and the zoom
	 * Receives also the zoom factor that is used to set the amount of zoom
	 */
	public GraphValues(CustomGraphView graphView, double factor) {
		this(graphView);
		this.factor = factor;
	}
	 
   /*
    * setVisible
    * Receives a boolean indicating if it should be visible or not	
    */
	public void setVisible(boolean value) {
		xSeries.setVisible(value);
		ySeries.setVisible(value);
		zSeries.setVisible(value);
		visible = value;
		graphView.invalidate();
	}
	
	/* 
	 * appendData
	 * Append data to the series. Calculates the absMax to automatically set the zoom
	 * Receives currentTime(x-axis), float[] values (array of values X, Y, Z), and scrollToEnd indicating if it should scroll to end of graph
	 * 
	 */
	public void appendData(long currentTime, float[] values, boolean scrollToEnd) {
		absMax = Math.max(absMax, Math.abs(values[0]));
		absMax = Math.max(absMax, Math.abs(values[1]));
		absMax = Math.max(absMax, Math.abs(values[2]));
		
		
		xSeries.appendData(new GraphViewData(currentTime, values[0]), false);
		ySeries.appendData(new GraphViewData(currentTime, values[1]), false);
		zSeries.appendData(new GraphViewData(currentTime, values[2]), false);
		
		if (visible) {
			if (scrollToEnd) {
				graphView.scrollToEnd();
				graphView.setManualYAxisBounds(absMax, -absMax);
			}
			graphView.invalidate();
		}
	}
	
	/*
	 * Zoom In
	 * Receives the amount of zoom (value)
	 */
	public void zoomIn(double value) {
		if (absMax > value*factor) {
			absMax -= value*factor;
			graphView.setManualYAxisBounds(absMax, -absMax);
			graphView.redrawAll();
			graphView.invalidate();
		}
	}
	
	/*
	 * Zoom Out
	 * Receives the amount of zoom (value)
	 */
	public void zoomOut(double value) {
		absMax += value*factor;
		graphView.setManualYAxisBounds(absMax, -absMax);
		graphView.redrawAll();
		graphView.invalidate();
	}
}
