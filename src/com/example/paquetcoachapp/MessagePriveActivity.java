package com.example.paquetcoachapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessagePriveActivity extends Activity {
	
	
	
	private String contenuConversation ="<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>"
			+ "<br><br/>"
			+ "<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>" 
			+ "<br><br/>"
			+ "<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>" 
			+ "<br><br/>"
			+ "<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>" 
			+ "<br><br/>"
			+ "<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>" 
			+ "<br><br/>"
			+ "<br><u>Quentin (30/04/14 � 15h07)</u></br>"
			+ "<br>salut</br>"
			+ "<br><br/>"
			+ "<br><u>John (30/04/14 � 16h07)</u></br>"
			+ "<br>salut � toi</br>" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_prive);
		TextView textViewDestinataire = (TextView)findViewById(R.id.textView2);
		String destinataire=(String) getIntent().getSerializableExtra("nomDestinataire");
		textViewDestinataire.setText("Conversation avec " + destinataire);
		
		TextView textViewConversation = (TextView)findViewById(R.id.textView1);
		
		textViewConversation.setText(Html.fromHtml(contenuConversation));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_prive, menu);
		return true;
	}
	
	public void envoyerMessage(View view){
		EditText tonEdit = (EditText)findViewById(R.id.editText1);
		String message = tonEdit.getText().toString();
		
		if(!(message.equals(""))){
		contenuConversation = contenuConversation + "<br><br/>"
				+ "<br><u>Quentin (01/05/14 � 11h10)</u></br>"
				+ "<br>" +message +"</br>";
		((EditText) findViewById(R.id.editText1)).setText("");
		
		Toast.makeText(this, "Envoy� !", Toast.LENGTH_LONG).show();
		Intent intent = getIntent();
	    finish();
	    startActivity(intent);
	    }
		
		else{
			Intent intent = getIntent();
		    finish();
		    startActivity(intent);
		}
	}
	
	
	
	

}
	
