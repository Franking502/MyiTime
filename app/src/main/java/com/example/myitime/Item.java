package com.example.myitime;

public class Item {

    private String Title;//活动名称
    private String Description;//活动描述
    private int CoverResourceId;//图片id
    private String period;//周期
    private int year;//事件年份
    private int month;//事件月份
    private int date;//事件日期
    private int hour;//事件时
    private int minute;//事件分
    private int second;//事件秒

    public String getPeriod() { return period; }

    public void setPeriod(String period) { this.period = period; }

    public int getYear() {
        return year;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
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

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getCoverResourceId() {
        return CoverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        CoverResourceId = coverResourceId;
    }

    public Item(String title,int icon,int year,int month,int date){
        setTitle(title);
        setCoverResourceId(icon);
        setYear(year);
        setMonth(month);
        setDate(date);
    }
}
