/*		AccelerometerGraph.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 24, 2013
 * 
 * Invariants:
 * 	0 <= ZOOM_VALUE
 * Description:
 * 	This represents the ZoomControls buttons.
 */

package edu.wustl.cse467.sleepingbeauty;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ZoomControls;

public class AccelerometerGraphZoomControls {
	
	public static double ZOOM_VALUE = 1.0;
	
	private ZoomControls zoomControls;
	private AccelerometerGraph graphView;
	
	/*
	 * Constructor:
	 * 	Activates the zoom and instantiates the listeners
	 */
	public AccelerometerGraphZoomControls(AccelerometerGraph graphView, ZoomControls zoomControls) {
		this.graphView = graphView;
		this.zoomControls = zoomControls;
		this.zoomControls.setIsZoomInEnabled(true);
		this.zoomControls.setIsZoomOutEnabled(true);
		this.zoomControls.setOnZoomInClickListener(new ZoomInClickListener());
		this.zoomControls.setOnZoomOutClickListener(new ZoomOutClickListener());
	}
	
	private class ZoomInClickListener implements OnClickListener {
		/*
		 * Click in the zoom in button
		 * Calls zoomIn function of Graph
		 * Parameter View is not used.
		 */
		@Override
		public void onClick(View v) {
			graphView.zoomIn(ZOOM_VALUE);
		}
	}
	
	private class ZoomOutClickListener implements OnClickListener {
		/*
		 * Click in the zoom out button
		 * Calls zoomOut function of Graph
		 * Parameter View is not used.
		 */
		@Override
		public void onClick(View v) {
			graphView.zoomOut(ZOOM_VALUE);
		}
	}
	
}
