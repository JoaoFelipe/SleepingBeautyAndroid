/*		RoughValues.java
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
 * 	This class represents a timestamp and, a rough value and the linear acceleration values
 * 	It has a method to put the information into a data of post
 * Version log:
 * 	3/27/2013, Joao Felipe
 * 		Adding linear acceleration data
 */
package edu.wustl.cse467.sleepingbeauty.sensor;

import java.util.HashMap;

public class RoughValues {
	public boolean roughy;
	public long timestamp;
	public float x;
	public float y;
	public float z;
	
	/*
	 * Constructor
	 * Receives the values and the timestamp
	 */
	public RoughValues(boolean roughy, long timestamp, float[] values) {
		this.x = values[0];
		this.y = values[1];
		this.z = values[2];
		this.roughy = roughy;
		this.timestamp = timestamp;
	}
	
	/*
	 * addToPostData
	 * Receives the name of the context, the data hashmap and the position
	 */
	public void addToPostData(String name, HashMap<String, String> data, int position) {
		data.put(name+"["+position+"][time]", timestamp+"");
		data.put(name+"["+position+"][x]", x+"");
		data.put(name+"["+position+"][y]", y+"");
		data.put(name+"["+position+"][z]", z+"");
		data.put(name+"["+position+"][really_rough]", roughy?"1":"0");
	}
}
