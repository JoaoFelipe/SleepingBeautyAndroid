/*		GraphViewSeriesLimited.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * 			Tiago Pimentel
 * 			t.pimentelms@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Invariants:
 * 	DEFAULT_LIMIT >= 1000 to not cut in the middle of the graph
 * Description:
 * 	This class extends GraphViewSeries to limit the amount of data in the graph and free memory.
 */

package edu.wustl.cse467.sleepingbeauty.graph;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class GraphViewSeriesLimited extends GraphViewSeries {

	static int DEFAULT_LIMIT = 2000;
	
	protected int limit;
	protected GraphViewData[] values;
	
	/*
	 * Constructor
	 * Receives series name and style (color and size)
	 */
	public GraphViewSeriesLimited(String description, GraphViewSeriesStyle style) {
		super(description, style, new GraphViewData[]{});
		this.values = new GraphViewData[]{};
		this.limit = DEFAULT_LIMIT;
	}
	
	/*
	 * setLimit
	 * Receives the new limit of the series
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	/*
	 * appendData
	 * Append data to the series
	 * Receives value to add to serie and a scrollToEnd that is not used (necessary to override the interface)
	 */
	@Override
	public void appendData(GraphViewData value, boolean scrollToEnd) {
		int size;
		int startingPosition;
		if (values.length < limit) {
			size = values.length + 1;
			startingPosition = 0;
		} else {
			size = limit;
			startingPosition = 1;
		}
		GraphViewData[] newValues = new GraphViewData[size];
		System.arraycopy(values, startingPosition, newValues, 0, size - 1);

		newValues[size - 1] = value;
		values = newValues;
		resetData(newValues);
	}
	
	

}
