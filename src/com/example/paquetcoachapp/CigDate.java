package com.example.paquetcoachapp;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class CigDate {

	private int year=0;
	private int month=0;
	private int day=0;
	private int hour=0;
	private int minute=0;
	private int cigarettes=0;
	
	public int getCigarettes() {
		return cigarettes;
	}
	public void setCigarettes(int cigarettes) {
		this.cigarettes = cigarettes;
	}
	public int getYear() {
		return year;
	}
	public int getRealYear() {
		return year+2000;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public CigDate(long data) {
		long current=data;
		this.year= (int) (long) (current/100000000L);
		current-= year*100000000L;
		this.month= (int) (long) (current/1000000L);
		current-= month*1000000L;
		this.day= (int) (long) (current/10000L);
		current-= day*10000L;
		this.hour= (int) (long) (current/100L);
		current-= hour*100L;
		this.minute= (int) (long) (current);
		this.cigarettes= 1;
		
	}
	
	public CigDate(int year, int month, int day, int hour, int minute, int cig) {
		if (year<100) this.year=year;
		else this.year=year-2000;
		this.month=month;
		this.day=day;
		this.hour=hour;
		this.minute=minute;
		this.cigarettes=cig;
		
	}
	
	public void smokeUp() { //ajoute une cigarette à la date
		this.cigarettes++;
	}
	
	public Long toLong() {
		return (year*100000000L +month*1000000L + day*10000L + hour*100L + minute);
	}
	
	public int compareDate(CigDate date) {
		if ((date.getDay()==day)&&(date.getYear()==year)&&(date.getMinute()==minute)&&(date.getMonth()==month)&&(date.getHour()==hour)) {
			return 0;
		}
		else if (this.toLong() > date.toLong()) return 1;
		    else return -1;
		
	}
	
	public DateTime toDateTime() {
		return new DateTime(year, month, day, hour, minute);
	}
	
	public boolean sameDay(CigDate date) {
		return ((date.getDay()==day)&&(date.getYear()==year)&&(date.getMonth()==month));
	}
	public String dayToString() {
		return ""+day+"/"+month+"/"+year;
	}
	public String dateToString() {
		return ""+day+"/"+month+"/"+year+" à "+hour+":"+minute;
		
	}
	
	public int joursEntre(CigDate date) {
		DateTime past = new DateTime(2000+ date.getYear(),date.getMonth(),date.getDay(),date.getHour(),date.getMinute()); 
		DateTime present = new DateTime(2000+ year,month,day,hour,minute);
		return (Days.daysBetween(present,past).getDays());
	}
	

	

}
