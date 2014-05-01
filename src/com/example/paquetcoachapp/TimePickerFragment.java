package com.example.paquetcoachapp;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	
	
	
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
