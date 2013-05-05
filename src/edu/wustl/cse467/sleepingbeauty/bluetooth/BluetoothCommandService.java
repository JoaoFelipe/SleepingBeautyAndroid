/*		BluetoothApi.java
 * Purpose: Final Demo
 * Author : Joao Felipe
 * 		   joaofelipenp@gmail.com
 * CSE 467S - Embedded Computing Systems
 * WUSTL, Spring 2013
 * Date   : Apr., 29, 2013
 * 
 * Description:
 * 	This is the bluetooth command service. It has threads to connect to a bluetooth network, 
 *  and has threads to keep the connection and redirect messages to/from the Bluetooth API
 */
package edu.wustl.cse467.sleepingbeauty.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothCommandService {
	// Debugging
    private static final String TAG = "BluetoothCommandService";
    private static final boolean D = true;

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    
    
    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    // Constants that indicate command to computer
    public static final int EXIT_CMD = -1;
    public static final int VOL_UP = 1;
    public static final int VOL_DOWN = 2;
    public static final int MOUSE_MOVE = 3;
    
    public boolean running;
    
    // Constructor
    // Receives handler to return the data to the API
    public BluetoothCommandService(Handler handler) {
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mState = STATE_NONE;
    	mHandler = handler;
    	running = false;
    }
    
    // Set state of connection
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        switch (state) {
	        case STATE_CONNECTED:
	        case STATE_CONNECTING:
	    		running = true;
	    		break;
	        case STATE_LISTEN:
	        case STATE_NONE:
	    	default:
        		running = false;
        		break;
        }
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(BluetoothApi.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    // return current state
    public synchronized int getState() {
        return mState;
    }
    
    // Start the bluetooth service
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        setState(STATE_LISTEN);
    }
    
    // Start the ConnectThread to initiate a connection to a remote device.
    // Receives the device to connect
    public synchronized void connect(BluetoothDevice device) {
    	if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }
    
    // Start the ConnectedThread to begin managing a Bluetooth connection
    // Receives the sockect on which the connection was made and the device that has been connected
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    // Stop all threads
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        
        setState(STATE_NONE);
    }
    
    // Write to the ConnectedThread in an unsynchronized manner
    // Receives the bytes to write
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    // Write to the ConnectedThread in an unsynchronized manner
    // Receives a single int
    public void write(int out) {
    	// Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    // Indicate that the connection attempt failed and notify the UI Activity.
    private void connectionFailed() {
        setState(STATE_LISTEN);
    }

    // Indicate that the connection was lost and notify the UI Activity.
    private void connectionLost() {
		setState(STATE_LISTEN);
    }
    
    // This thread runs while attempting to make an outgoing connection with a device
    // It runs straight through; the connection either succeeds or fails.
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        // Constructor
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                BluetoothCommandService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothCommandService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    // This thread runs during a connection with a remote device.
    // It handles all incoming and outgoing transmissions.
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        // Constructor
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            
            ArrayList<Byte> values = new ArrayList<Byte>();
            boolean first = false;
            int count = 0;
            
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                	// Read from the InputStream
                    int bytes = mmInStream.read(buffer);
                    for (int i = 0; i < bytes; ++i){
                    	if (buffer[i] == BluetoothApi.START && count <= 0) {
                    		values = new ArrayList<Byte>();
                    		first = true;
                    	} else if (buffer[i] == BluetoothApi.END && count <= 0) {
                    		ByteBuffer buf = ByteBuffer.allocate(values.size());
                    	    for (int j = 0; j < values.size(); j++) {
                    	    	buf.put(values.get(j));
                    	    }
                    	    buf.flip();
                    	    byte cmd = buf.get();
                    	    if (cmd == BluetoothApi.SET_LIGHT_STATUS) {
                    	    	long time = buf.getLong();
                    	        byte status = buf.get();
                    	        Message msg = mHandler.obtainMessage(BluetoothApi.SET_LIGHT_STATUS);
                    	        Bundle bundle = new Bundle();
                    	        bundle.putLong(BluetoothApi.TIME, time);
                    	        bundle.putByte(BluetoothApi.STATUS, status);
                    	        msg.setData(bundle);
                    	        mHandler.sendMessage(msg);
                    	    }
                    	} else {
                    		values.add(buffer[i]);
                    		if (first) {
                    			if (buffer[i] == BluetoothApi.SET_LIGHT_STATUS) {
                        			count = 9;
                    			}
                    			first = false;
                    		}
                    	}
                    	count--;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        // Write to the connected OutStream.
        // Receives the bytes to write
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }
        
        // Write to the connected OutStream.
        // Receives one int to write
        public void write(int out) {
        	try {
                mmOutStream.write(out);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
            	mmOutStream.write(EXIT_CMD);
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}