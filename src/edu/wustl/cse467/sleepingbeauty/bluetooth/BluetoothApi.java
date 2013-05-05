/*		BluetoothApi.java
 * Purpose: Final Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Apr., 29, 2013
 * 
 * Description:
 * 	This is the bluetooth API. It connects to the SleepingBeautyBluetooth network 
 *  and provides functions to send and rececive data via bluetooth 
 * Version log:
 *	  4/30/2013, Joao Felipe
 *    	Refactoring LightInformation to possibility change from web and from bluetooth
 */


package edu.wustl.cse467.sleepingbeauty.bluetooth;

import java.nio.ByteBuffer;
import java.util.Set;

import edu.wustl.cse467.sleepingbeauty.R;
import edu.wustl.cse467.sleepingbeauty.gui.LightInformation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class BluetoothApi {

	ImageView status;
	private BluetoothCommandService mCommandService = null;
	private BluetoothDevice mDevice = null;
	

	public static final byte OFF = 0;
	public static final byte ON = 1;
	public static final byte START = 2;
	public static final byte END = 3;
	public static final byte SET_LIGHT_STATUS = 76;
	public static final byte REQUEST_LIGHT_STATUS = 82;
	public static final byte SET_ROUGH = 109;
	public static final byte SET_REALLY_ROUGH = 77;
	
	

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    
    // Key names received from the BluetoothCommandService Handler
    public static final String STATUS = "status";
    public static final String TIME = "time";
    
    public static final String TOAST = "toast";
	
    // Look for this server name in the paired devices
    public static final String SERVER_NAME = "SleepingBeautyBluetooth";
    
	// The Handler that gets information back from the BluetoothCommandService
    private final Handler mHandler;

    // Construtor
	public BluetoothApi(ImageView status, LightInformation lightInformation) {
		this.status = status;
		mHandler = new HandlerExtension(status, lightInformation);
		//mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	// This method tries to find the server in the paired devices, then tries to connect to it
	public void connect() {
		Thread tr = new Thread() {
			@Override
			public void run() {
				
				while (true) {
					try {
						while (mDevice == null) {
							findDevice();
							Thread.sleep(1000);
						}
						while (mCommandService.running) {}
						mCommandService.connect(mDevice);
					
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		tr.start();
	}
	
	// This method finds the server in the paired devices
	public void findDevice() {
		if (mDevice != null) {
			return;
		}
		Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	if (SERVER_NAME.equals(device.getName()) || "joao-P151EMx-0".equals(device.getName())) {
                	mDevice = device;
                	System.out.println(device.getAddress());
                }
		    }
		    
		}
	}
	
	// This method starts the bluetooth if it is active
	public void onStart() {
		if (mCommandService == null) {
			setupCommand();
		}
	}
	
	// This method starts the bluetooth if it is active but wasn't during the start
	public void onResume() {
		if (mCommandService != null) {
			if (mCommandService.getState() == BluetoothCommandService.STATE_NONE) {
				mCommandService.start();
			}
		} else {
			setupCommand();
		}
	}
	
	// This method stops the bluetooth
	public void onDestroy() {
		if (mCommandService != null) {
			mCommandService.stop();
		}
	}
	
	// This method initializes the CommandServicet o perform bluetooth connections
	private void setupCommand() {
        mCommandService = new BluetoothCommandService(mHandler);
	}
	
	// Write to the connected OutStream.
    // Receives the bytes to write
    public void write(byte[] buffer) {
    	if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
    	mCommandService.write(buffer);
    }
    
    // Write to the connected OutStream.
    // Receives one int to write
    public void write(int out) {
    	if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
    	mCommandService.write(out);
    }
    
    // Send light status via bluetooth
    public void writeLight(long time, boolean status) {
    	if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
    	ByteBuffer buffer = ByteBuffer.allocate(12);
    	buffer.put(START);
    	buffer.put(SET_LIGHT_STATUS);
	    buffer.putLong(time);
	    buffer.put(status? ON : OFF);
	    buffer.put(END);
	    byte[] buf = buffer.array();
	    this.write(buf);
	}
	
    // Request light status via bluetooth
    public void requestLight() {
    	if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
	    ByteBuffer buffer = ByteBuffer.allocate(3);
	    buffer.put(START);
	    buffer.put(REQUEST_LIGHT_STATUS);
	    buffer.put(END);
	    byte[] buf = buffer.array();
	    this.write(buf);
	}

    // Send rough movement via bluetooth
	public void writeRough(long timestamp) {
		if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
    	ByteBuffer buffer = ByteBuffer.allocate(11);
    	buffer.put(START);
    	buffer.put(SET_ROUGH);
	    buffer.putLong(timestamp);
	    buffer.put(END);
	    byte[] buf = buffer.array();
	    this.write(buf);
	}
	
	// Send really rough movement via bluetooth
	public void writeReallyRough(long timestamp) {
		if (mCommandService == null || !mCommandService.running ) {
    		return;
    	}
    	ByteBuffer buffer = ByteBuffer.allocate(11);
    	buffer.put(START);
    	buffer.put(SET_REALLY_ROUGH);
	    buffer.putLong(timestamp);
	    buffer.put(END);
	    byte[] buf = buffer.array();
	    this.write(buf);
	}
	
}

//The Handler that gets information back from the BluetoothCommandService
class HandlerExtension extends Handler {
	
	private ImageView status;
	private LightInformation lightInformation;

	HandlerExtension(ImageView status, LightInformation lightInformation) {
		this.status = status;
		this.lightInformation = lightInformation;
	}
	
	@Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
        case BluetoothApi.MESSAGE_STATE_CHANGE:
            switch (msg.arg1) {
            case BluetoothCommandService.STATE_CONNECTED:
            	status.setImageResource(R.drawable.green);
                break;
            case BluetoothCommandService.STATE_CONNECTING:
                break;
            case BluetoothCommandService.STATE_LISTEN:
            case BluetoothCommandService.STATE_NONE:
            	status.setImageResource(R.drawable.red);
                break;
            }
            break;
        case BluetoothApi.SET_LIGHT_STATUS:
        	status.setImageResource(R.drawable.green);
        	lightInformation.setLight(
        		msg.getData().getLong(BluetoothApi.TIME), 
        		msg.getData().getByte(BluetoothApi.STATUS) == 1? true : false 
        	);
        	break;
        }
    }
}