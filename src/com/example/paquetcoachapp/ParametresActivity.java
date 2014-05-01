package com.example.paquetcoachapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;

public class ParametresActivity extends Activity {
	private ListView listView ;
	private SharedPreferences prefs;
	private int allowance;
	private int[] allowanceChoices={-1,0,1,3,5,7,10,15,20,30,40};
	private String[] list;
	NumberPicker picker1;
    NumberPicker picker2;
    NumberPicker picker3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_parametres);
		prefs=this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		allowance=prefs.getInt("allowance", -1);
		updateList();
		
		final OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		    public void onItemClick(AdapterView parent, View v, int position, long id) {
		        // Do something in response to the click
		    	switch (position) {
		    	case 0:
		    		changeAllowance();
		    		break;
		    	case 1:
		    		pricePicker();
		    		break;
		    	default:
		    		break;
		    	}
		    }
		};

		listView.setOnItemClickListener(mMessageClickedHandler);	
	}
	
	public String regime(){
		int allowance=prefs.getInt("allowance", -1);
		if (allowance==-1) return "pas de régime";
		else return allowance+"/jour";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parametres, menu);
		return true;
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
	        	   				int newAllowance=allowanceChoices[which];
	        	   				prefs.edit().putInt("allowance", newAllowance)
	        	   							.commit();
	        	   			}
	           	})
	    		.setPositiveButton("OK",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int id) {
	                	allowance=prefs.getInt("allowance", -1);
	                	updateList();
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
	
	private int choicePicked() {//Pour prédire la case déja cochée dans le dialog de changeAllowance
		int result=0;
		for (int i=0;i<11;i++){
			if (allowanceChoices[i]==allowance) result=i;
		}
		return result;
	}
	
	public void updateList() {
		listView = (ListView) findViewById(R.id.list);
		list = new String[] {"Changer de régime: "+regime(),
										"Prix d'un paquet: "+( prefs.getInt("PrixPaquet", 690))/100.0+"€",
										""
										
				};
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		          android.R.layout.simple_list_item_1, android.R.id.text1, list);

		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private void pricePicker() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View layout; // Creating an instance for View Object
	    LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    layout = inflater.inflate(R.layout.price_picker, null);
	    int currentPrice=prefs.getInt("PrixPaquet", 690);
	    picker1= (NumberPicker) layout.findViewById(R.id.NumberPicker1);
	    picker2= (NumberPicker) layout.findViewById(R.id.NumberPicker2);
	    picker3= (NumberPicker) layout.findViewById(R.id.NumberPicker3);
	    picker1.setMaxValue(9);
	    picker2.setMaxValue(9);
	    picker3.setMaxValue(9);
	    picker1.setValue(currentPrice/100);
	    picker2.setValue((currentPrice/10)%10);
	    picker3.setValue(currentPrice%10);
	    builder.setTitle("Prix d'un Paquet de 20 cigarettes")
	    		.setView(layout);
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int id) {
	                	int price=(picker1.getValue()*100+picker2.getValue()*10+picker3.getValue());
	                	prefs.edit().putInt("PrixPaquet", price)
	                				.commit();
	                	updateList();
	                }
	             });
    	builder.show();
	    
		
	}


}
