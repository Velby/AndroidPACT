package com.example.paquetcoachapp;

import java.io.File;

import org.joda.time.DateTime;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class FiguresActivity extends Activity {
    private ListView listView ;
    private SharedPreferences prefs;
    private CigDateArray allData;
    private CigDate lastSmoke;
    private double moyenneSur7Jours;
    private double moyenneSurUnMois;
    private double moyenneSurUnAn;
    private double prixParMois;
    private double depensesTotal;
    private int cigarettesTotal;
    private int fumeesAujourdhui;
	private double prixParCigarette;
	private String tempsPerdu;
	private String allowanceString;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figures);
        String[] values;
        prefs=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.list);
        calcul();
        if (lastSmoke==null) {
        	values=new String[] {"NO DATA"};
        }
        else{
        	values = new String[] { "Fumées ajourd'hui:	"+fumeesAujourdhui+allowanceString, 
        								"Dernière cigarette "+lastSmokeString(),
                                         "Moyenne sur une semaine:	"+moyenneSur7Jours+"/jour",
                                         "Moyenne sur un mois:	"+moyenneSurUnMois+"/jour",
                                         "Moyenne sur un an:	"+moyenneSurUnAn+"/jour", 
                                         "Dépenses par mois: 	"+prixParMois+"€",
                                         "Prix d'une cigarette: 	"+prixParCigarette+"€",
                                         "Total dépensé: 	"+depensesTotal+"€",
                                         "Total fumé: 	"+cigarettesTotal+"cigs",
                                         "Première Cigarette"+firstSmokeString(),
                                         tempsPerdu};
        }
        


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter); 
  
    }
    
    private String firstSmokeString() {
		CigDate firstSmoke=allData.get(0);
		if (firstSmoke.sameDay(new CigDate())) {
			return "aujourd'hui à "+ firstSmoke.toStringTime();
		}
		else {
			return "le "+firstSmoke.toStringDate();
		}
	}

	private String lastSmokeString() {
    	if (lastSmoke==null) return "(pas de données)";
		if (lastSmoke.sameDay(new CigDate())) {
			return "aujourd'hui à "+ lastSmoke.toStringTime();
		}
		else {
			return "le "+lastSmoke.toStringDate()+" à "+lastSmoke.toStringTime();
		}
	}
	
	private String timeLostString(int[] tab) {
		String result="no Data";
		
		if (tab[3]!=0) result = tab[3]+"années et "+tab[2]+"jours";
		else if(tab[2]!=0) result= tab[2]+"jours et "+tab[1]+"heures";
			else if(tab[1]!=0) result = tab[1]+"heures et "+tab[0]+"minutes";
				else result= tab[0]+"minutes";
		return result;
	}


	public void calcul() {
    	File appfile = new File(this.getFilesDir(), "donneesAppli");
		allData=new CigDateArray(appfile);
		if (allData.size()>0){
			lastSmoke=allData.latest();
			prixParCigarette=Math.round(prefs.getInt("PrixPaquet", 690)/20.0)/100.0;
			moyenneSur7Jours=Math.round(allData.moyenneParJour(7)*100.0)/100.0;
			moyenneSurUnAn=Math.round(allData.moyenneParJour(365)*100.0)/100.0;
			moyenneSurUnMois=Math.round(allData.moyenneParJour(31)*100.0)/100.0;
			fumeesAujourdhui=allData.cigsToday();
			prixParMois=Math.round(moyenneSurUnMois*prixParCigarette*31*100.0)/100.0;
			cigarettesTotal=allData.allCigs();
			depensesTotal=Math.round(cigarettesTotal*prixParCigarette*100.0)/100.0;	
			int[] timeLost= new int[4];
			int minLost=11*cigarettesTotal;
			timeLost[0]=minLost%60;
			timeLost[1]=(minLost/60)%24;
			timeLost[2]=(minLost/(60*24))%365;
			timeLost[3]=(minLost/(60*24*365));
			tempsPerdu="Vous avez perdu "+timeLostString(timeLost)+" de votre vie";
			int allowance=prefs.getInt("allowance", -1);
			allowanceString="";
			if (allowance!=-1) allowanceString="/"+allowance;
			
			
			
		}
		else lastSmoke=null;		
    }
    
    

}
