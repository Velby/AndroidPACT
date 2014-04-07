package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
		// on vérifie l'ordre chronologique
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
	
		public CigDate now() {
		int n=this.size();
		if (n>0) return this.get(n-1);
		else return new CigDate(14,01,01,12,00,00);
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

			// La méthode read renvoie -1 dès qu'il n'y a plus rien à lire
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
				CigDate today=this.get(n);
			CigDate current=this.get(n);
			while ((n>-1)&&(today.sameDay(this.get(n)))) {
				current=this.get(n);
				smoked=smoked+current.getCigarettes();
				n--;
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

}
