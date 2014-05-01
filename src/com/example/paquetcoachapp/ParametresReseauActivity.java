package com.example.paquetcoachapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class ParametresReseauActivity extends Activity {

	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parametres_reseau);
		prefs=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		prefs.edit().putBoolean("siPseudoChoisi", false)
		.commit();
		prefs.edit().putBoolean("siIPChoisie",false)
		.commit();
		prefs.edit().putString("Pseudo", "")
		.commit();
		prefs.edit().putString("IP", "")
		.commit();
		prefs.edit().putString("Encrypt","")
		.commit();
		prefs.edit().putString("NDecrypt","")
		.commit();
		prefs.edit().putString("Decrypt","")
		.commit();
		prefs.edit().putString("NEncrypt","")
		.commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parametres_reseau, menu);
		return true;
	}
	
public void validerPseudo(View view){
		
	
		
	
		if (!(prefs.getBoolean("siPseudoChoisi", false))){
		
		EditText tonEdit = (EditText)findViewById(R.id.editText1);
		String pseudoTape = tonEdit.getText().toString();
		if(pseudoTape.equals("")){
			
			Toast.makeText(this, "Pseudo incorrect", Toast.LENGTH_LONG).show();
		}
		else{
			String[] donnes =new String[3];
			donnes=LienServeurWeb.nouvelUser(pseudoTape, prefs.getString("IP",""));
			
			prefs.edit().putString("Pseudo", pseudoTape)
						.commit();  
			prefs.edit().putBoolean("siPseudoChoisi", true)
						.commit();    
		
			String url = donnes[0];
			
			
			
			WebView browser = (WebView)findViewById(R.id.webView1);
			browser.loadUrl(url);
			browser.loadUrl(donnes[1]);
			browser.loadUrl(donnes[2]);
			
			prefs.edit().putString("Encrypt",donnes[3])
			.commit();
			prefs.edit().putString("NDecrypt",donnes[4])
			.commit();
			prefs.edit().putString("Decrypt",donnes[5])
			.commit();
			prefs.edit().putString("NEncrypt",donnes[6])
			.commit();
		
		((EditText) findViewById(R.id.editText1)).setText("");
		Toast.makeText(this, "Pseudo enregistré !", Toast.LENGTH_LONG).show();
		}
		}
		
		else{
			Toast.makeText(this, "Pseudo non modifiable", Toast.LENGTH_LONG).show();
		}
		
	}

public void afficherPseudo(View view){
	String pseudo = prefs.getString("Pseudo","");
	if(pseudo.equals("")){
		Toast.makeText(this, "Vous n'avez pas encore de pseudo", Toast.LENGTH_LONG).show();
	}
	else{
		Toast.makeText(this, "Votre pseudo (définitif) : " + pseudo, Toast.LENGTH_LONG).show();
	}
	
}

public void validerIP(View view){
	
	
	EditText tonEdit = (EditText)findViewById(R.id.editText2);
	String adresseIPstring = tonEdit.getText().toString();
	

	prefs.edit().putString("IP", adresseIPstring)
				.commit();  
	prefs.edit().putBoolean("siIPChoisie", true)
				.commit();  
			((EditText) findViewById(R.id.editText2)).setText("");
			Toast.makeText(this, "IP enregistrée !", Toast.LENGTH_LONG).show();
			
		
}

public void afficherIP(View view){
	String adresseIP = prefs.getString("IP","");
	if(adresseIP.equals("")){
		String bonbon = prefs.getString("IP", "pi");
		Toast.makeText(this, bonbon, Toast.LENGTH_LONG).show();
	}
	else{
		Toast.makeText(this, "Adresse IP enregistrée : " + adresseIP , Toast.LENGTH_LONG).show();
	}
	
}

}