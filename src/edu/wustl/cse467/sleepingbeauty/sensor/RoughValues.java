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
 * 	This class represents a timestamp and a rough value
 * 	It has a method to put the information into a data of post
 */
package edu.wustl.cse467.sleepingbeauty.sensor;

import java.util.HashMap;

public class RoughValues {
	public boolean roughy;
	public long timestamp;
	
	/*
	 * Constructor
	 * Receives the values and the timestamp
	 */
	public RoughValues(boolean roughy, long timestamp) {
		this.roughy = roughy;
		this.timestamp = timestamp;
	}
	
	/*
	 * addToPostData
	 * Receives the name of the context, the data hashmap and the position
	 */
	public void addToPostData(String name, HashMap<String, String> data, int position) {
		data.put(name+"["+position+"][time]", timestamp+"");
		data.put(name+"["+position+"][roughy]", roughy?"1":"0");
	}
}
