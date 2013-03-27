/*		TimeValues.java
 * Purpose: Midterm Demo
 * Author : Joao Felipe
 * 		  joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Mar., 26, 2013
 * 
 * Invariants:
 * 	URL should be a valid URL
 * Description:
 * 	This class represents a timestamp and 3 values of an event
 * 	It has a method to put the information into a data of post
 */
package edu.wustl.cse467.sleepingbeauty.sensor;

import java.util.HashMap;

public class TimeValues {
	public float x;
	public float y;
	public float z;
	
	public long timestamp;
	
	
	/*
	 * Constructor
	 * Receives the values and the timestamp
	 */
	public TimeValues(float[] values, long timestamp) {
		this.x = values[0];
		this.y = values[1];
		this.z = values[2];
		
		this.timestamp = timestamp;
	}
	
	/*
	 * addToPostData
	 * Receives the name of the context, the data hashmap and the position
	 */
	public void addToPostData(String name, HashMap<String, String> data, int position) {
		data.put(name+"["+position+"][x]", x+"");
		data.put(name+"["+position+"][y]", y+"");
		data.put(name+"["+position+"][z]", z+"");
		data.put(name+"["+position+"][measure_time]", timestamp+"");
	}
}
