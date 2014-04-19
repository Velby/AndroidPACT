package com.example.paquetcoachapp;

import java.io.File;
import java.util.ArrayList;







import com.androidplot.xy.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

public class StatsActivity extends Activity {
	
	private CigDateArray allData;
	private CigDate today;
	private XYPlot plot;
	public SeekBar seekBar;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		refreshData();
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(

                new OnSeekBarChangeListener() {
    int progress = 0;
        @Override
      public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) { 
        refreshData();
    	plotGraph(progresValue*7);
        
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        //pas utile pour l'instant
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        //pas utile pour l'instant
      }
  });
	}

	public void onResume () {
		super.onResume();
		refreshData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}
	
	public void refreshData() {
		File appfile = new File(this.getFilesDir(), "donneesAppli");
		allData=new CigDateArray(appfile);
		if (allData.size()>0){
			today=allData.now();
		}
		else today=new CigDate(2014,01,01,01,01,00);		
	}
	
	public SimpleXYSeries derniersJours(int n) {
		int current = 0;
		CigDate currentDate= today;
		int joursDepuis=0;
		int[] days=new int[n];
		int[] cigs=new int[n];
		for (int i=0; i<n; i++) {
			days[i]=i+1-n;
		}
		
		while (joursDepuis<n) {
			cigs[n-1-joursDepuis]+=currentDate.getCigarettes();	
			current++;
			if (current >= allData.size()) joursDepuis=n;
			else {
				currentDate=allData.getEnd(current);
				joursDepuis=currentDate.joursEntre(today);
			}
		}
		ArrayList<Integer> x= new ArrayList<Integer>(n);
		ArrayList<Integer> y= new ArrayList<Integer>(n);
		for (int i=0; i<n; i++) {
			x.add(days[i]);
			y.add(cigs[i]);
		}
		SimpleXYSeries result= new SimpleXYSeries(x,y,"");
		return result;
	}
	
	
	public SimpleXYSeries aujourdhui() {
		int current=0;//nombre de dates vues
		CigDate currentTime= today;
		int n=today.getHour()+1;
		int[] hours=new int[n];
		int[] cigs=new int[n];
		for (int i=0; i<n; i++) {
			hours[i]=i;
		}
		
		while (currentTime.sameDay(today)) {
			cigs[currentTime.getHour()]+=currentTime.getCigarettes();	
			current++;
			if (current >= allData.size()) currentTime.setYear(0);
			else {
				currentTime=allData.getEnd(current);
			}
		}
		ArrayList<Integer> x= new ArrayList<Integer>(n);
		ArrayList<Integer> y= new ArrayList<Integer>(n);
		for (int i=0; i<n; i++) {
			x.add(hours[i]);
			y.add(cigs[i]);
		}
		SimpleXYSeries result= new SimpleXYSeries(x,y,"");
		return result;
	}
	
	public void plotGraph(int n) { 
		if (plot!=null) plot.clear();
		// initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        
        XYSeries series1;
        if (n!=0) series1 = this.derniersJours(n);  
        else series1 = aujourdhui(); 
 
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        BarFormatter series1Format = new BarFormatter(Color.RED, Color.BLACK);
 
        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
 
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        BarRenderer render=(BarRenderer) plot.getRenderer(BarRenderer.class);
        render.setBarWidth(20);
        render.setBarGap(12);
        if (n!=0) {
        	plot.setTitle(""+(n/7)+" semaines");
        	plot.setDomainLabel("jours");
        }
        else {
        	plot.setTitle("Aujourd'hui");
        	plot.setDomainLabel("heures");
        }
        plot.setRangeLabel("cig");
        
        plot.redraw();
        plot.invalidate();
	}
}
