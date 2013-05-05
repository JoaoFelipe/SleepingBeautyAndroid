/*		LightInformation.java
 * Purpose: Final Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Apr., 30, 2013
 * 
 * Description:
 * 	This class keeps track of the time that it received the last light information
 *  If it receives a signal change after the last information, it changes the light status
 * 	This is used to syncronize bluetooth and internet data
 */

package edu.wustl.cse467.sleepingbeauty.gui;

import android.widget.ToggleButton;

public class LightInformation {
	
	private ToggleButton lightsButton;
	private long time;
	private boolean status;
	
	public LightInformation(ToggleButton lightsButton) {
		this.lightsButton = lightsButton;
		time = 0;
		status = false;
	}
	
	public synchronized void setLight(long time, boolean status) {
		if (this.time >= time) {
			return;
		}
		this.time = time;
		this.status = status;
		lightsButton.setChecked(this.status);
	}
	
	public synchronized boolean isChecked() {
		return lightsButton.isChecked();
	}
	
	public synchronized void setChecked(boolean check) {
		lightsButton.setChecked(check);
	}
	
}
