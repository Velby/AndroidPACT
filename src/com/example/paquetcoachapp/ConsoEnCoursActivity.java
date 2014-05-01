package com.example.paquetcoachapp;

import java.io.File;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ConsoEnCoursActivity extends Activity {
	
	private CigDate today= new CigDate();
	CigDate latest;
	private int smoked=0;
	private int allowance; //nombre de cigarettes autorisé
	SharedPreferences prefs; 
	private int[] choices={-1,0,1,3,5,7,10,15,20,30,40};
	
	public void updateList() { //met a jour le arraylist en fonction du fichier en mémoire
		File appfile = new File(this.getFilesDir(), "donneesAppli");
		smoked=0;
		CigDateArray allCigs=new CigDateArray(appfile);
		if (allCigs.size()>0){
			latest=allCigs.latest();
			if (latest.sameDay(today)) smoked=allCigs.cigsToday();
		}	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conso_en_cours);
		prefs=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		updateList();
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
		changerTexte();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conso_en_cours, menu);
		return true;
	}
	
	public void onResume() {
		super.onResume();
		updateList();
		changerTexte();
	}

	public void changerTexte() { //Créé le texte à afficher sur l'activité
		
		String texteConso="";
		TextView monTexte = null;
		if (latest==null) texteConso="Vous n'avez jamais fumé!";
		else {
			if (allowance>=0) {
				if (smoked>0) {
					if(allowance>=smoked){
						texteConso= "Vous avez fumé "+smoked+" cigarettes sur "+allowance+" prévues ajourd'hui\n"+
						"dernière à "+latest.toStringTime();
					}
					else {
						texteConso ="Vous avez fumé "+smoked+" cigarettes sur "+allowance+" prévues ajourd'hui "+
								"( Dernière à "+today.toStringTime()+")"+".\n C'est mal!";
					}
				}
				else {
					texteConso= "Vous n'avez pas fumé aujourd'hui!\n"+
							"dernière cigarette le "+latest.toStringDate()+" à "+latest.toStringTime();
				}
			}
			else {
				if (smoked>0) {
					if(allowance>=smoked){
						texteConso= "Vous avez fumé "+smoked+" cigarettes ajourd'hui\n"+
						"dernière à "+latest.toStringTime();
					}
					else {
						texteConso ="Vous avez fumé "+smoked+" cigarettes ajourd'hui "+
								"( Dernière à "+today.toStringTime()+")"+".\n C'est mal!";
					}
				}
				else {
					texteConso= "Vous n'avez pas fumé aujourd'hui!\n"+
							"dernière cigarette le "+latest.toStringDate()+" à "+latest.toStringTime();
				}
	
			}
		}
	monTexte = (TextView)findViewById(R.id.text_conso);
    monTexte.setText(texteConso);
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
	    else
	    builder.create().show();
	}

}



