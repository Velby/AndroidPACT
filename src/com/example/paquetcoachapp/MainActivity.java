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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.heloworld.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);//le bouton home fait crasher l'application donc je l'ai désactivé
	 
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_activity_action_bar, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onResume(Menu menu) {
    	this.refreshData();
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle presses on the action bar items
    	switch (item.getItemId()) {
    	case R.id.action_search:
    		openSearch();
    		return true;
    	case R.id.action_settings:
    		openSettings();
    		return true;
    	case R.id.action_refresh:
    		refreshData();
    	case R.id.action_fakePack:
    		openFakePack();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
// ouvrir les paramètres
	private void openSettings() {
	   	Intent intent = new Intent(this,SettingsActivity.class);
    	startActivity(intent);
		
	}

	//lancer une recherche sur l'appli
	private void openSearch() {
	   	Intent intent = new Intent(this, SearchActivity.class);
    	startActivity(intent);
		
	}
	
	//lancer le faux paquet
	private void openFakePack() {
	   	Intent intent = new Intent(this, FakePack.class);
    	startActivity(intent);
		
	}

	//méthode pour aller voir la consommation en cours
	public void open_conso(View view) {
		Intent intent = new Intent(this, ConsoEnCours.class);
    	startActivity(intent);
	}
	
	//méthode pour ouvrir les stats
	public void open_stats(View view) {
		Intent intent = new Intent(this, Stats.class);
    	startActivity(intent);
	}

		
		//méthode pour aller voir le résau social
	public void open_resau(View view) {
		Intent intent = new Intent(this, Resau.class);
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
			while ((date = in.readLong()) != -1) {
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
	
 
    
    
}
