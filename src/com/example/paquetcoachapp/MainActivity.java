package com.example.paquetcoachapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
// modifier les paramètres
	private void openSettings() {
	   	Intent intent = new Intent(this,SettingsActivity.class);
    	startActivity(intent);
		
	}

	//lancer une recherche sur l'appli
	private void openSearch() {
	   	Intent intent = new Intent(this, SearchActivity.class);
    	startActivity(intent);
		
	}


 
    
    
}
