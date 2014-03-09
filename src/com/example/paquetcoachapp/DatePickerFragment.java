package com.example.paquetcoachapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	
	FakePack fakePack;
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            fakePack = (FakePack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement TimeListeners");
	        }
	    }

	
	
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
	ArrayList<Long> conso= fakePack.getConso();
	Date date;
	int n=conso.size() -1;
	if (n>-1) {
		date= new Date(conso.get(n));
	}
	else date=fakePack.getDate();
// Use the app date as the default values for the picker
int year = date.getRealYear();
int month = date.getMonth();
int day=date.getDay();

// Create a new instance of DatePickerDialog and return it
return new DatePickerDialog(getActivity(), this, year, month, day);

}

public void onDateSet(DatePicker view, int year, int month, int day) {
	
	fakePack.onDateUpdate( year,  month, day);

}

}
