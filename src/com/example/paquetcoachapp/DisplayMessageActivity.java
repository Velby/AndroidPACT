package com.example.paquetcoachapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);

        // Reception du message
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // On créé l'affichage
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // on met l'affichage dans le layout
        setContentView(textView);

    }
    


    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.display_message_action_bar, menu);
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
       
        case R.id.action_back:
        	this.goBackToMain();
        	
        }
        return super.onOptionsItemSelected(item);
    }

	private void goBackToMain() {
	super.finish();
		
	}
}