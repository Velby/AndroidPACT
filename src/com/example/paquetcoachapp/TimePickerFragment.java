package com.example.paquetcoachapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	
	
	
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
int hour = date.getHour();
int minute = date.getMinute();

// Create a new instance of TimePickerDialog and return it
return new TimePickerDialog(getActivity(), this, hour, minute,
true);

}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	
	fakePack.onTimeUpdate( hourOfDay,  minute);

}

}
