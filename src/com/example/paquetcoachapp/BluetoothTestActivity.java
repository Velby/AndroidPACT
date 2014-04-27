package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
  

  


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
  
public class BluetoothTestActivity extends Activity {
  private static final String TAG = "bluetooth1";
    
  Button btnOn, btnOff;
    
  private BluetoothAdapter btAdapter = null;
  private BluetoothSocket btSocket = null;
  private OutputStream outStream = null;
  private InputStream inStream=null;
      
  // SPP UUID service 
  private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // Adresse MAC
  private static String address = "00:06:66:63:1D:7D";
    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.activity_bluetooth_test);
  
    btnOn = (Button) findViewById(R.id.btnOn);
    btnOff = (Button) findViewById(R.id.btnOff);
      
    btAdapter = BluetoothAdapter.getDefaultAdapter();
    checkBTState();
  
    btnOn.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        getData();
        Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_LONG).show();
      }
    });
  
    btnOff.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        getData();
        Toast.makeText(getBaseContext(), "Turn off LED", Toast.LENGTH_LONG).show();
      }
    });
  }
   
  private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
      if(Build.VERSION.SDK_INT >= 10){
          try {
              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
              return (BluetoothSocket) m.invoke(device, MY_UUID);
          } catch (Exception e) {
              Log.e(TAG, "Could not create Insecure RFComm Connection",e);
          }
      }
      return  device.createRfcommSocketToServiceRecord(MY_UUID);
  }
    
  @Override
  public void onResume() {
    super.onResume();
  
    Log.d(TAG, "...onResume - try connect...");
   
    BluetoothDevice device = btAdapter.getRemoteDevice(address);

    
    try {
        btSocket = createBluetoothSocket(device);
    } catch (IOException e1) {
        errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
    }
        
    btAdapter.cancelDiscovery();
    
    //Etablissement de la connection
    Log.d(TAG, "...Connecting...");
    try {
      btSocket.connect();
      Log.d(TAG, "...Connection ok...");
    } catch (IOException e) {
      try {
        btSocket.close();
      } catch (IOException e2) {
        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
      }
    }
      
    // flux de données pour créer la connection
    Log.d(TAG, "...Create Socket...");
  
    try {
      outStream = btSocket.getOutputStream();
      inStream = btSocket.getInputStream();
    } catch (IOException e) {
      errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
    }
  }
  
  @Override
  public void onPause() {
    super.onPause();
  
    Log.d(TAG, "...In onPause()...");
  
    if (outStream != null|inStream!=null) {
      try {
        outStream.flush();
        inStream.close();
      } catch (IOException e) {
        errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
      }
    }
  
    try     {
      btSocket.close();
    } catch (IOException e2) {
      errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
    }
  }
    
  private void checkBTState() {
    // Cherche et demande la ressource BT
    if(btAdapter==null) { 
      errorExit("Fatal Error", "Bluetooth not support");
    } else {
      if (btAdapter.isEnabled()) {
        Log.d(TAG, "...Bluetooth ON...");
      } else {
        //Prompt user to turn on Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
      }
    }
  }
  
  private void errorExit(String title, String message){
    Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    finish();
  }
  
  private void sendData(String message) {
    byte[] msgBuffer = message.getBytes();
  
    Log.d(TAG, "...Send data: " + message + "...");
  
    try {
    	
      outStream.write(msgBuffer);
    } catch (IOException e) {
      String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
      if (address.equals("00:00:00:00:00:00")) 
        msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
        msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";
        
        errorExit("Fatal Error", msg);       
    }
  }
  
  public void getData() {
	  
      byte[] buffer = new byte[1024];  // buffer store for the stream
      int bytes; // bytes returned from read()
      boolean firstInt=true;
      int dates=0;
      int recieved=0;
      sendData("1");
      
      // Keep listening to the InputStream until an exception occurs
      while (true) {
          try {
        	  File appfile = new File(this.getFilesDir(), "donneesAppli");
        		
        		if (!appfile.exists()) {
        			appfile.createNewFile();
        		}
              // Read from the InputStream
        	   bytes = inStream.read(buffer);// lance un exception s'il n'y a plus rien à lire
              if (firstInt) { // réception du nombre total de nouvelles dates
            	  firstInt=false;
            	  dates=bytes;
              }
              else {
            	  // Send the obtained bytes to the UI activity
            	 recieved++; 	
            	DataOutputStream out =  new DataOutputStream(new FileOutputStream(appfile));
      			out.writeLong(bytes); // on stocke les nouvelles données
      			if (out != null)
      				out.close();
              }
              
          } 
          
          catch (IOException e) { // Atteint quand le paquet n'envoie plus rien
        	  if ((recieved==dates)&&(dates>0)) {
        		  sendData("0");
        		  Toast.makeText(getApplicationContext(), "Tout reçu \n ("+dates+" cigarettes fumées)", Toast.LENGTH_LONG).show();
        	  }
        	  else {
        		  if (dates==0) {
        			  Toast.makeText(getApplicationContext(), "Vous n'avez pas fumé depuis \n la dernière mise à jour", Toast.LENGTH_LONG).show();
        		  }
        		  else {
        			  Toast.makeText(getApplicationContext(), "Pas tout Reçu :(", Toast.LENGTH_LONG).show();
        		  }
        	  }
        	  
        	  
              break;
              
          }
      }
  }

}