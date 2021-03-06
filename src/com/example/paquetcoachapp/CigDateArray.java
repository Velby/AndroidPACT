package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CigDateArray extends ArrayList<CigDate> {
	private static final long serialVersionUID = 1704573164395748186L;
	
	public void add(Long k) {
		CigDate date= new CigDate(k);
		int n=this.size() -1;
		if (n==-1) {
			this.add(date);
		}
		else {
			int v=date.compareDate(this.get(n));
			if (v==0) this.get(n).smokeUp();
			else {
				if (v<0)
					try {
						throw new Exception("PAS CHRONOLOGIQUE");
					} catch (Exception e) {
						e.printStackTrace();
					}
				else this.add(date);
			}
		}
	}
	
	
	public CigDateArray(ArrayList<Long> list)  {
		// on v�rifie l'ordre chronologique
		int n= list.size();
		if (n<1) {
			for (Long k : list) {
				this.add(k);
			}
		}
		else {
			if (list.get(n-1)>list.get(0)) {
				for (Long k : list) {
					this.add(k);
				}
			}
			else {
				for (int i=n-1; i>-1; i--) {
					this.add(list.get(i));
				}
			}
		}
	}
	
		public CigDate latest() {
		int n=this.size();
		if (n>0) return this.get(n-1);
		else return new CigDate();
	}
		
	public CigDateArray() {
		super();
	}
	
	public CigDate getEnd(int n) {
		int m =this.size();
		return this.get(m-1-n);
	}
	

	
	public CigDateArray(File appfile) {
		try {
			DataInputStream in = null;
			in = new DataInputStream(new FileInputStream(appfile));
			long dateLong;

			// La m�thode read renvoie -1 d�s qu'il n'y a plus rien � lire
			while ((dateLong = in.readLong()) != -1) {				
				this.add(dateLong);
			}
			if (in != null)
				in.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	



	public ArrayList<Long> toLongArray() {
		ArrayList<Long> result=new ArrayList<Long>();
		for (CigDate date : this) {
			for (int i=0; i<date.getCigarettes();i++){ //un Long par cigarette sur la minute
				result.add(date.toLong());
			}
		}		
		return result;
	}
	
	public int cigsToday(){
		int smoked=0;
		int n=this.size() -1;
		if (n>-1) {
			CigDate today=new CigDate();
			CigDate current=this.latest();
			while ((n>-1)&&(current.sameDay(today))) {				
				smoked=smoked+current.getCigarettes();
				n--;
				if (n>-1)current=this.get(n);
			}	
		}
		return smoked;
	}
	
	public int cigsOnDay(CigDate date){
		int smoked=0;
		int n=this.size() -1;
		CigDate current=this.get(n);
		while ((n>-1)&&(date.compareDate(current)<0)) {
			current=this.get(n);
			if (current.sameDay(date)) smoked=smoked+current.getCigarettes();			
			n--;
			
		}
		return smoked;
	}
	
	public double moyenneParJour(int days){
		CigDate today=new CigDate();
		CigDate firstDay=today.minusDays(days-1);
		int n=1; //nombre de jours consid�r�s (au cas o� il n'y a pas assez de donn�es)
		int size=this.size();
		int j=0;
		int cigs=0;
		CigDate currentDay=today;
		for (int i=size-1;i>-1;i--) {
			CigDate date=this.get(i);
			if ((firstDay.compareDate(date)>0)&& !(firstDay.sameDay(date))) break;			
			cigs+=date.getCigarettes();
			if (currentDay.compareDate(date)>0) {
				n=date.joursEntre(today);
				
				currentDay=date;
			}
			j=i;
		}
		if (j==0)return (double) cigs/n;
		else return (double) cigs/days;
	}
	
	public int allCigs() {
		int cigs=0;
		for (CigDate date:this) {
			cigs+=date.getCigarettes();
		}
		return cigs;
	}

}
