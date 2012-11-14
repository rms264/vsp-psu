package vsp.dataObject;

import java.io.Serializable;

public class VolatilityInfo implements Serializable{

  private static final long serialVersionUID = 6324659450637099803L;
  double day;
  double week;
  double month;
  double year;
  public double getDay() {
    return day;
  }
  public void setDay(double day) {
    this.day = day;
  }
  public double getWeek() {
    return week;
  }
  public void setWeek(double week) {
    this.week = week;
  }
  public double getMonth() {
    return month;
  }
  public void setMonth(double month) {
    this.month = month;
  }
  public double getYear() {
    return year;
  }
  public void setYear(double year) {
    this.year = year;
  }
  
}
