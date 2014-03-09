package com.example.paquetcoachapp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DateArray extends ArrayList<Date> {
	private static final long serialVersionUID = 1704573164395748186L;
	
	public void add(Long k) {
		Date date= new Date(k);
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
	
	
	public DateArray(ArrayList<Long> list)  {
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
	
		public Date now() {
		int n=this.size();
		if (n>0) return this.get(n-1);
		else return null;
	}
		
	public DateArray() {
		super();
	}
	

	
	public DateArray(File appfile) {
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
		for (Date date : this) {
			for (int i=0; i<date.getCigarettes();i++){ //un Long par cigarette sur la minute
				result.add(date.toLong());
			}
		}		
		return result;
	}
	
	public int cigsToday(){
		int smoked=0;
		int n=this.size() -1;
		Date today=this.get(n);
		Date current=this.get(n);
		while ((n>-1)&&(today.sameDay(this.get(n)))) {
			current=this.get(n);
			smoked=smoked+current.getCigarettes();
			n--;
			
		}
		return smoked;
	}

}
