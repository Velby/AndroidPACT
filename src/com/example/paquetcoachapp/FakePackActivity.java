	package com.example.paquetcoachapp;
	import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


	public class FakePackActivity extends  Activity  {
		
		private ArrayList<Long> conso=new ArrayList<Long>();
		private CigDateArray consoDates=new CigDateArray();
		private CigDate date=new CigDate();
		private int cigsToday=0;
		
		public CigDate getDate() {
			return date;
		}
		
		public ArrayList<Long> getConso() {
			return conso;
		}
		
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	// Handle presses on the action bar items
	    	switch (item.getItemId()) {
	    	case R.id.action_clear_all:
	    		clearAll();
	    		date.setCigarettes(0);
	    		changerTexte();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
	    	}
	    }
	    
	    
		

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_fake_pack);
			this.update();
	    	changerTexte();
		}
		
		  public void onResume(Menu menu) {
			  super.onResume();
			  
		    	this.update();
		    	changerTexte();
		    	invalidateOptionsMenu();
		    }

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.fake_pack, menu);
			MenuItem menuConso= menu.findItem(R.id.credits);
	    	menuConso.setTitle(cigsToday+"/"+12);
			return true;
		}
		
		

		public void smoke(View v) {
			int n= conso.size() -1;
			if (n==-1) {
				date.smokeUp();
				conso.add(date.toLong()); 
				consoDates= new CigDateArray(conso);
				cigsToday=consoDates.cigsToday();
				changerTexte();
				}
			else {
				Long lastLong= this.conso.get(n);
				Long dateLong=date.toLong();
					if ((lastLong-dateLong)<=0) {
						
						date.smokeUp();
						conso.add(dateLong); 
						consoDates= new CigDateArray(conso);
						cigsToday=consoDates.cigsToday();
						changerTexte();
						}
					else {
					TextView monTexte = (TextView)findViewById(R.id.text_date);
					CigDate lastDate= new CigDate(this.conso.get(n));
					monTexte.setText("PAS CHRONOLOGIQUE. Derniere date: \n"+lastDate.toStringTime()+"   "+lastDate.toStringDate());
					
				}
			}
			invalidateOptionsMenu();
			
		}
		
		
		public void changeTime(View v) {
		    TimePickerFragment picker = new TimePickerFragment();
		    picker.show(getFragmentManager(), "timePicker");
		}
		
		public void changeDate(View v) {
		    DatePickerFragment picker = new DatePickerFragment();
		    picker.show(getFragmentManager(), "datePicker");
		}

		
		public void sendData() {
			changerTexte();
			try {
			DataOutputStream out = null;
			File packfile = new File(this.getFilesDir(), "donneesPack");
			out = new DataOutputStream(new FileOutputStream(packfile));

			// La méthode read renvoie -1 dès qu'il n'y a plus rien à lire
			for (Long longDate: conso) {
					
						out.writeLong(longDate);
					
				}
			out.writeLong(-1);
			if (out != null)
				out.close();
				} catch (IOException e) {
						e.printStackTrace();
					}
		}
		
		public void sendDataClick(View v) {
			sendData();
		}
		
		public void clearAll() { //nettoie toutes les données
			conso=new ArrayList<Long>();
			consoDates=new CigDateArray();
			sendData();
			date=new CigDate();
			cigsToday=0;
			changerTexte();
			invalidateOptionsMenu();
			
		}
		
		public void update() { //met a jour le arraylist en fonction du fichier en mémoire
			conso=new ArrayList<Long>();
			try {
				DataInputStream in = null;
				File packfile = new File(this.getFilesDir(), "donneesPack");
				in = new DataInputStream(new FileInputStream(packfile));
				long dateLong=in.readLong();
				if (dateLong==-1) {
					Toast toast = Toast.makeText(this.getApplicationContext(), "No previous data!", Toast.LENGTH_LONG);
					toast.show();
				}
				

				// La méthode read renvoie -1 dès qu'il n'y a plus rien à lire
				
				while (dateLong != -1) {
					conso.add(dateLong);
					dateLong=in.readLong();
				}
				if (in != null)
					in.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			consoDates= new CigDateArray(conso);
			cigsToday=consoDates.cigsToday();
			date=new CigDate();
			}
		
		public void changerTexte() { //Créé le texte à afficher sur l'activité
			String text= date.toStringTime()+"   "+date.toStringDate()+"\n"
								+date.getCigarettes()+" cigs";
			final TextView monTexte = (TextView)findViewById(R.id.text_date);
			monTexte.setText(text);
			invalidateOptionsMenu();
		}

		public void onTimeUpdate(int hour, int minute) {
			this.date.setHour(hour);
			this.date.setMinute(minute);
			this.date.setCigarettes(0);
			changerTexte();			
		}


		public void onDateUpdate(int year, int month, int day) {
			this.date.setYear(year-2000);
			this.date.setMonth(month);
			this.date.setDay(day);
			this.date.setCigarettes(0);
			changerTexte();
			
		}
		
		private void randomConso(int moyenne, int ecartType, CigDate start) {// créé une consommation aléatoire sur une durée
			clearAll();
			start.setCigarettes(0);
			Random randomno = new Random();
			CigDate now=new CigDate();
			int duree=start.joursEntre(now)+1;
			date=start;
			for (int i=0; i<duree;i++){
				Double rand=randomno.nextGaussian()*ecartType+moyenne;//nombre aléatoire de cigarettes fumées dans la journée
				for (int j=0;j< rand.intValue() ;j++) smoke(null);
				if (i!=duree-1) date=date.plusDays(1);
			}
			sendData();
		}
		
		public void randomConsoClick(View v) {
			randomConso(5,4,date);
		}		

}


