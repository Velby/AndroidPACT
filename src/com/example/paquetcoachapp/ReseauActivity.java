package com.example.paquetcoachapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ReseauActivity extends Activity {


	public static SharedPreferences settings;
	public static SharedPreferences.Editor editeur;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reseau);
		
		settings = getPreferences(MODE_WORLD_READABLE);
		editeur = settings.edit();
		editeur.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reseau, menu);
		return true;
	}
	
	
	public void envoieMessagePrive(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, MessagePriveActivity.class);
    	startActivity(intent);
    	
    }
	
	public void ouvreParametresReseau(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, ParametresReseauActivity.class);
    	startActivity(intent);
    	
    }
	
	public void accederConversation(View view){
		EditText tonEdit = (EditText)findViewById(R.id.editText1);
		String destinataire = tonEdit.getText().toString();
		if(destinataire.equals("")){
			Toast.makeText(this, "Tapez un destinataire", Toast.LENGTH_LONG).show();
		}
		
		else{
			boolean conditionPseudo = ReseauActivity.settings.getBoolean("PseudoChoisi", false);
			boolean conditionIP = ReseauActivity.settings.getBoolean("IPChoisie", false);
			if(conditionPseudo&&conditionIP){
				LienServeurWeb.
			//if urlexislts...    A FAIRE
			
			Intent intent = new Intent(this, MessagePriveActivity.class);
			intent.putExtra("nomDestinataire", destinataire);
			tonEdit.setText("");
			startActivity(intent);
			}
			
			else{
				tonEdit.setText("");
				Toast.makeText(this, "Vous devez configurer vos paramètres (pseudo et IP serveur local)", Toast.LENGTH_LONG).show();
			}
		}
	}

	

}
