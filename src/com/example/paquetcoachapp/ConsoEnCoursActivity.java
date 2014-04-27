package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ConsoEnCoursActivity extends Activity {
	
	private CigDate today= new CigDate(00,01,01,01,01,01);
	private CigDateArray cigsToday= new CigDateArray();
	private int smoked=0;
	private int allowance=12; //nombre de cigarettes autorisé
	
	public void setsmoked(int i) {
		this.smoked=i;
	}
	
	public void setAllowance(int i) {
		this.allowance=i;
	}	

	public int getsmoked() {
		return smoked;
	}

	public int getAllowance() {
		return allowance;
	}
	
	public void updateList() { //met a jour le arraylist en fonction du fichier en mémoire
		File appfile = new File(this.getFilesDir(), "donneesAppli");
		smoked=0;
		CigDateArray allCigs=new CigDateArray(appfile);
		CigDateArray result=new CigDateArray();
		if (allCigs.size()>0){
			today=allCigs.now();
			for (CigDate date: allCigs) {
				if (date.sameDay(today)) {
					result.add(date);
					for (int i=0; i<date.getCigarettes();i++) smoked++;
				}
			}
		}
		else today=new CigDate(2014,01,01,01,01,00);
		
		cigsToday=result;		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conso_en_cours);		
		updateList();
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
		String texte_pour_conso="";
		TextView monTexte = null;

	if (allowance>=0) {
		if(allowance>=smoked){
			texte_pour_conso= "Vous avez fumé "+smoked+" cigarettes sur "+allowance+" prévues ajourd'hui\n"+
					"dernière le "+today.dateToString();
		}
		else {
			texte_pour_conso ="Vous avez fumé "+smoked+" cigarettes sur "+allowance+" prévues ajourd'hui "+
			"("+today.dayToString()+")"+".\n C'est mal!";
		}
	}
	else {
		texte_pour_conso= "Vous avez fumé "+smoked+" cigarettes ajourd'hui "+
				"("+today.dayToString()+")";
	}

	monTexte = (TextView)findViewById(R.id.text_conso);
    monTexte.setText(texte_pour_conso);
	}
	

}


