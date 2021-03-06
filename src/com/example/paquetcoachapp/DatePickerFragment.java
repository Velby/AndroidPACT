package com.example.paquetcoachapp;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	
	FakePackActivity fakePack;
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            fakePack = (FakePackActivity) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement TimeListeners");
	        }
	    }

	
	
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
	ArrayList<Long> conso= fakePack.getConso();
	CigDate date;
	int n=conso.size() -1;
	if (n>-1) {
		date= new CigDate(conso.get(n));
	}
	else date=fakePack.getDate();
// Use the app date as the default values for the picker
int year = date.getRealYear();
int month = date.getMonth();
int day=date.getDay();

// Create a new instance of DatePickerDialog and return it
return new DatePickerDialog(getActivity(), this, year, month-1, day);

}

public void onDateSet(DatePicker view, int year, int month, int day) {
	
	fakePack.onDateUpdate( year,  month+1, day);

}

}
