package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	private SharedPreferences prefs;
	private int allowance;
	private int[] choices={-1,0,1,3,5,7,10,15,20,30,40};

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        prefs=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);//le bouton home fait crasher l'application donc je l'ai désactivé
        refreshData();
        allowance = prefs.getInt("allowance", -2);
		if (allowance==-2) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			// 2. Chain together various setter methods to set the dialog characteristics
			builder
            .setMessage("Vous n'avez pas choisi de Régime")
            .setCancelable(false)
            .setPositiveButton("Choisir un régime",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, close
                    // current activity
                    changeAllowance();
                }
            });

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	 
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_activity_action_bar, menu);
    	MenuItem menuConso= menu.findItem(R.id.credits);
    	menuConso.setTitle(this.consoText());
    	return true;
    	
    }
    
    public void onResume() {
    	super.onResume();
    	this.refreshData();
    	invalidateOptionsMenu();    	
    }
    
 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle presses on the action bar items
    	switch (item.getItemId()) {
    	case R.id.action_fakePack:
    		openFakePack();
    		return true;
    	case R.id.action_bluetooth:
    		openBlueTooth();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

	//lancer le faux paquet
	private void openFakePack() {
	   	Intent intent = new Intent(this, FakePackActivity.class);
    	startActivity(intent);
		
	}

	//méthode pour aller voir la consommation en cours
	public void open_conso(View view) {
		refreshData();
		Intent intent = new Intent(this, ConsoEnCoursActivity.class);
    	startActivity(intent);
	}
	
	//méthode pour ouvrir les stats
	public void open_stats(View view) {
		refreshData();
		Intent intent = new Intent(this, StatsActivity.class);
    	startActivity(intent);
	}
	
	//méthode pour ouvrir les stats
		public void open_parametres(View view) {
			Intent intent = new Intent(this, ParametresActivity.class);
	    	startActivity(intent);
		}
	
	private void openBlueTooth() {
		refreshData();
		Intent intent = new Intent(this, BluetoothTestActivity.class);
    	startActivity(intent);
	}

	//méthode pour aller voir le résau social
	public void open_resau(View view) {
		Intent intent = new Intent(this, ResauActivity.class);
    	startActivity(intent);
	}
	
		//Création ou mise a jour du fichier des données
	public void refreshData()  {
		try {
		
		File appfile = new File(this.getFilesDir(), "donneesAppli");
		File packfile = new File(this.getFilesDir(), "donneesPack");// pour le FAUX paquet
		if (!packfile.exists()) {
			packfile.createNewFile();
			}
		
		if (!appfile.exists()) {
			appfile.createNewFile();
		}

		DataInputStream in = null;
		DataOutputStream out = null;
		
			in = new DataInputStream(new FileInputStream(packfile));
			out = new DataOutputStream(new FileOutputStream(appfile));
			long date;

			// La méthode read renvoie -1 dès qu'il n'y a plus rien à lire
			while ((date = in.readLong()) != -1) {// on recopie tout
				out.writeLong(date);
			}
			if (in != null)
				in.close();

			if (out != null)
				out.close();
		
		}
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	public String consoText() { //mettre a jour l'affichage dans la barre d'actions
		File appfile = new File(this.getFilesDir(), "donneesAppli");
		CigDateArray allCigs=new CigDateArray(appfile);
		int smoked= allCigs.cigsToday();
		allowance=prefs.getInt("allowance", -2);
		String allowanceString="";
		if (allowance!=-1) allowanceString="/"+allowance;
		return smoked+allowanceString;

	}
	
	private int choicePicked() {//Pour prédire la case déja cochée dans le dialog de changeAllowance
		int result=0;
		for (int i=0;i<11;i++){
			if (choices[i]==allowance) result=i;
		}
		return result;
	}
	
	public void changeAllowance() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Quel régime voulez vous suivre?")
	           .setSingleChoiceItems(new String[] {"Pas de Régime",
	        		   								"0/jour",
	        		   								"1/jour",
	        		   								"3/jour",
	        		   								"5/jour",
	        		   								"7/jour",
	        		   								"10/jour",
	        		   								"15/jour",
	        		   								"20/jour",
	        		   								"30/jour",
	        		   								"40/jour"},
	        		   choicePicked(), new DialogInterface.OnClickListener() {
	        	   			public void onClick(DialogInterface dialog, int which) {
	        	   				int newAllowance=choices[which];
	        	   				prefs.edit().putInt("allowance", newAllowance)
	        	   							.commit();
	        	   			}
	           	})
	    		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int id) {
	                	allowance=prefs.getInt("allowance", -1);
	                }
	             });
	    if (allowance>-2){
	    	builder.setCancelable(true);
	    	builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
	    		public void onCancel(DialogInterface dialog) {
                	prefs.edit().putInt("allowance", allowance)
                		.commit();
                }
	    	});
	    }
	    else builder.setCancelable(false);
	    builder.create().show();
	}
	
 
    
    
}
