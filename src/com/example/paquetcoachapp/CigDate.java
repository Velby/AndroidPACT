package com.example.paquetcoachapp;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class CigDate {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int cigarettes;
	
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
		this.year=year%100;
		this.month=month;
		this.day=day;
		this.hour=hour;
		this.minute=minute;
		this.cigarettes=cig;
		
	}
	
	public CigDate(DateTime date,int cigs) {
		year=date.getYear()%100;
		month=date.getMonthOfYear();
		day=date.getDayOfMonth();
		hour=date.getHourOfDay();
		minute=date.getMinuteOfHour();
		cigarettes=cigs;
	}
	
	public CigDate() {
		DateTime date=new DateTime();
		year=date.getYear()%100;
		month=date.getMonthOfYear();
		day=date.getDayOfMonth();
		hour=date.getHourOfDay();
		minute=date.getMinuteOfHour();
		cigarettes=0;
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
		return new DateTime(year+2000, month, day, hour, minute);
	}
	
	public boolean sameDay(CigDate date) {
		return ((date.getDay()==day)&&(date.getYear()==year)&&(date.getMonth()==month));
	}

	
	public int joursEntre(CigDate date) { //renvoie le nombre de jours entre deux dates, peut etre négatif
		DateTime future = date.toDateTime(); 
		DateTime present = this.toDateTime();
		int result=(Days.daysBetween(present,future).getDays());
		if (this.compareDate(date)<0) return result;
		else return -result;
	}
	
	public String toStringTime() { //renvoie l'heure sous forme 08:03
		String sHour=hour+"";
		String sMinute=minute+"";
		if (hour<10) sHour=0+sHour;
		if (minute<10) sMinute=0+sMinute;
		return (sHour+":"+sMinute);
	}
	
	public String toStringDate() { //renvoie l'heure sous forme 08:03
		String sMonth=month+"";
		String sDay=day+"";
		if (day<10) sDay=0+sDay;
		if (month<10) sMonth=0+sMonth;
		return (sDay+"/"+sMonth+"/"+year);
	}
	
	public CigDate plusDays(int n) {
		return new CigDate((this.toDateTime()).plusDays(n),0);
	}
	
	public CigDate minusDays(int n) {
		return new CigDate((this.toDateTime()).minusDays(n),0);
	}

	

}
