package com.example.myitime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    ListView editList;
    private List<EditItem> listEditItem = new ArrayList<>();
    //每次打开这个页面都要初始化list，添加日期、重复、图片、置顶四个功能
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        init();
    }

    private void init() {
    }

    private class EditItem {//在这里面设置方法对活动类进行设置，显示
        private String Title;//活动名称
        private String Description;//活动描述
        private int year;//事件年份
        private int month;//事件月份
        private int date;//事件日期
        private int hour;//事件时
        private int minute;//事件分
        //不设秒，默认0秒
        private String period;//周期
        private int TimeIconResourceId;//时间图片id

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

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

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public int getTimeIconResourceId() {
            return TimeIconResourceId;
        }

        public void setTimeIconResourceId(int timeIconResourceId) {
            TimeIconResourceId = timeIconResourceId;
        }
    }
}
