package com.example.paquetcoachapp;

import java.io.File;
import java.util.ArrayList;
import org.joda.time.DateTime;

import com.androidplot.xy.*;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class StatsActivity extends Activity {
	
	private CigDateArray allData;
	private CigDate today;
	private XYPlot plot;
	private int n=10;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		refreshData();
		// initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
 
        XYSeries series1 = this.derniersJours();                         
 
 
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
        plot.setTitle(""+n+" derniers jours");
        plot.setRangeLabel("cigs");
        plot.setDomainLabel("days");
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
	
	public SimpleXYSeries derniersJours() {
		int current = 0;
		CigDate currentDate= today;
		DateTime dateTime =today.toDateTime(); //servira pour trouver les dates des jours précédents
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
	
	
	

}
