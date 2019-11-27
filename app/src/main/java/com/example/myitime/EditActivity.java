package com.example.myitime;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    ListView editList;
    private List<EditItem> listEditItem = new ArrayList<EditItem>();

    //每次打开这个页面都要初始化list，添加日期、重复、图片、置顶四个功能
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        init();
        ItemArrayAdapter adapter=new ItemArrayAdapter(EditActivity.this,R.layout.edit_items,listEditItem);
        ListView listView=(ListView)findViewById(R.id.setting_item_list);
        listView.setAdapter(adapter);
    }

    private void init() {
        //在这里面设置Date、Period、Image、Stick四项到List中并显示
        listEditItem.add(new EditItem("Date",android.R.drawable.ic_menu_recent_history));
        listEditItem.add(new EditItem("Period",android.R.drawable.ic_menu_rotate));
        listEditItem.add(new EditItem("Image",android.R.drawable.ic_menu_gallery));
        listEditItem.add(new EditItem("Stick",android.R.drawable.ic_menu_upload));
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
        private String Item;//项名称
        private int ItemIcon;//项图标id

        private EditItem(String item,int icon){
            this.setItem(item);
            this.setItemIcon(icon);
        }

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

        public String getItem() {
            return Item;
        }

        public void setItem(String item) {
            Item = item;
        }

        public int getItemIcon() {
            return ItemIcon;
        }

        public void setItemIcon(int itemIcon) {
            ItemIcon = itemIcon;
        }

    }

    protected class ItemArrayAdapter extends ArrayAdapter<EditItem>
    {
        private  int resourceId;
        public ItemArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<EditItem> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            EditItem edititem=getItem(position);
            View item = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            ImageView img = (ImageView)item.findViewById(R.id.item_image_view);
            TextView name = (TextView)item.findViewById(R.id.item_name);
            TextView time = (TextView)item.findViewById(R.id.item_time);
            img.setImageResource(edititem.getItemIcon());
            name.setText(edititem.getItem());
            time.setText("");

            return item;
        }
    }
}
