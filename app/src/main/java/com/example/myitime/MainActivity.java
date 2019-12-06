package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;

import android.content.Intent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_ITEM_ADD = 901;
    public static final int ITEM_DETAIL = 902;
    ListView listViewItems;
    private List<Item> listItem = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    FloatingActionButton addButton;
    ItemAdapter adapter;
    TextView TimeOnPic;

    TextView mDays_Tv, mHours_Tv, mMinutes_Tv, mSeconds_Tv;
    private Timer mTimer;
    //下面的具体时间通过系统时间获得，现在先初始化做倒计时
    Calendar calendar = Calendar.getInstance();
    private long mDay;// 天
    private long mHour;//小时,
    private long mMin;//分钟,
    private long mSecond;//秒

    private Handler timeHandlerleft = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//未过
                computeTimeleft();
                mDays_Tv.setText("还有"+mDay+"天"+mHour+"小时");//天数不用补位
                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    mTimer.cancel();
                }
            }
        }
    };

    private Handler timeHandlerpassed = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                computeTimepast();
                mHour=Calendar.HOUR;
                mDay=-mDay;
                mDays_Tv.setText("已过"+mDay+"天"+mHour+"小时");//天数不用补位
                /*mHours_Tv.setText(getTv(mHour));
                mMinutes_Tv.setText(getTv(mMin));
                mSeconds_Tv.setText(getTv(mSecond));
                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    mTimer.cancel();
                }*/
            }
        }
    };

    private void computeTimeleft() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                    if(mDay < 0){
                        // 倒计时结束
                        mDay = 0;
                        mHour= 0;
                        mMin = 0;
                        mSecond = 0;
                    }
                }
            }
        }
    }

    private void computeTimepast() {
        mSecond++;
        if (mSecond > 60) {
            mMin++;
            mSecond = 0;
            if (mMin > 60) {
                mMin = 0;
                mHour++;
                if(mHour > 24){
                    mHour = 0;
                    mDay++;
                }
            }
        }
    }

    private String getTv(long l){
        if(l>=10){
            return l+"";
        }else{
            return "0"+l;//小于10,,前面补位一个"0"
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewItems=this.findViewById(R.id.list_view_item);

        adapter = new ItemAdapter(
                MainActivity.this, R.layout.list_view_item, listItem);
        listViewItems.setAdapter(adapter);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view); //nav_view是drawer中的内容
        ActionBar actionBar=getSupportActionBar();

        //下面逐个处理侧拉菜单中的各个选项：
        navView.setCheckedItem(R.id.nav_count);//设置默认选项为计时
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getTitle().equals("计时")) {
                    Toast.makeText(MainActivity.this,"计时",Toast.LENGTH_SHORT).show();
                }

                if (menuItem.getTitle().equals("标签")) {
                    Toast.makeText(MainActivity.this,"标签",Toast.LENGTH_SHORT).show();
                }

                if (menuItem.getTitle().equals("小部件")) {
                    Toast.makeText(MainActivity.this,"小部件",Toast.LENGTH_SHORT).show();
                }

                if (menuItem.getTitle().equals("主题色")) {
                    Toast.makeText(MainActivity.this,"主题色",Toast.LENGTH_SHORT).show();
                }

                if(menuItem.getTitle().equals("设置")){
                    Toast.makeText(MainActivity.this,"设置",Toast.LENGTH_SHORT).show();
                }

                if(menuItem.getTitle().equals("关于")){
                    Toast.makeText(MainActivity.this,"关于",Toast.LENGTH_SHORT).show();
                }

                if(menuItem.getTitle().equals("帮助与反馈")){
                    Toast.makeText(MainActivity.this,"帮助与反馈",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case NEW_ITEM_ADD:
                if(resultCode == RESULT_OK){
                    String title=data.getStringExtra("title");
                    int year=data.getIntExtra("year",2019);
                    int month=data.getIntExtra("month",1);
                    int date=data.getIntExtra("date",1);
                    int image=data.getIntExtra("image",R.drawable.pic1);
                    boolean stick=data.getBooleanExtra("stick",false);
                    if(stick==true){
                        getListItem().add(0,new Item(title,image,year,month,date));
                    }
                    else{
                        getListItem().add(listItem.size(),new Item(title,image,year,month,date));
                    }
                    adapter.notifyDataSetChanged();
                }
        }
    }

    public List<Item> getListItem() {
        return listItem;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            default:
        }
        return true;
    }

    class ItemAdapter extends ArrayAdapter<Item> {

        private int resourceId;

        public ItemAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos=position;
            final Item item = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.item_image_view)).setImageResource(item.getCoverResourceId());
            ((TextView) view.findViewById(R.id.item_name)).setText(item.getTitle());
            ((TextView) view.findViewById(R.id.item_description)).setText(Integer.toString(item.getYear())+'年'+Integer.toString(item.getMonth())+'月'+Integer.toString(item.getDate())+'日');
            mDays_Tv=view.findViewById(R.id.text_view_lefttime);
            mTimer = new Timer();
            //下面处理首页图片上的时间显示
            TimeOnPic=view.findViewById(R.id.text_view_lefttime);
            int year=item.getYear();
            int month=item.getMonth();
            int date=item.getDate();
            Calendar calendar = Calendar.getInstance();
            int yearpassed=year-calendar.get(Calendar.YEAR);
            int monthpassed=month-calendar.get(Calendar.MONTH);
            int daypssed=date-calendar.get(Calendar.DATE);
            boolean passed=false;//false表示还没过
            if(yearpassed<0){
                //当前年时间大于设定年时间，过了
                passed=true;
            }
            else if(yearpassed==0){
                if(monthpassed<0){
                    passed=true;
                }
                else if(monthpassed==0){
                    if(daypssed<0){
                        passed=true;
                    }

                    else{
                        passed=false;
                    }
                }
                else{
                    passed=false;
                }
            }
            else{
                //当前年时间小于设定年时间，还没到
                passed=false;
            }

            //检查passed，如果是true就肯定过了；
            //如果是false就再检查具体时间是否已过
            if(passed){
                //日期已过，计算已过多少天
                String s1= String.valueOf(year)+'-'+String.valueOf(month)+'-'+String.valueOf(date);
                String s2 = String.valueOf(calendar.get(Calendar.YEAR))+'-'+String.valueOf(calendar.get(Calendar.MONTH))+'-'+String.valueOf(calendar.get(Calendar.DATE));
                DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                Date d1= null;
                try {
                    d1 = df.parse(s1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date d2= null;
                try {
                    d2 = df.parse(s2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDay=(d1.getTime()-d2.getTime())/(60*60*1000*24);
                startRun(0);

            }
            else{
                //日期未过，计算还有多少天
                String s1= String.valueOf(year)+'-'+String.valueOf(month)+'-'+String.valueOf(date);
                String s2 = String.valueOf(calendar.get(Calendar.YEAR))+'-'+String.valueOf(calendar.get(Calendar.MONTH))+'-'+String.valueOf(calendar.get(Calendar.DATE));
                DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                Date d1= null;
                try {
                    d1 = df.parse(s1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date d2= null;
                try {
                    d2 = df.parse(s2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDay=(d2.getTime()-d1.getTime())/(60*60*1000*24);
                mDay=-mDay;
                startRun(1);
            }

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Item item=listItem.get(position);
                    //跳转到详情页面
                    //传入事件标题、事件时间
                    Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                    intent.putExtra("title",item.getTitle());
                    intent.putExtra("year",item.getYear());
                    intent.putExtra("month",item.getMonth());
                    intent.putExtra("date",item.getDate());
                    intent.putExtra("image",item.getCoverResourceId());
                    intent.putExtra("position",pos);
                    startActivityForResult(intent, ITEM_DETAIL);
                }
            });
            listViewItems.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

            return view;
        }
    }

    private void startRun(final int passed) {
        //passed为0已过，为1未过
        if(passed==1) {
            TimerTask mTimerTask = new TimerTask() {

                public void run() {
                    Message message = Message.obtain();

                    message.what = passed;
                    timeHandlerleft.sendMessage(message);
                }
            };
            mTimer.schedule(mTimerTask,0,1000);
        }
        if(passed==0) {
            TimerTask mTimerTask = new TimerTask() {

                public void run() {
                    Message message = Message.obtain();

                    message.what = passed;
                    timeHandlerpassed.sendMessage(message);
                }
            };
            mTimer.schedule(mTimerTask,0,1000);
        }
    }

    public void OnButtonClick(View v) {
        //这里跳转到新建页面活动
        Intent intent=new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, NEW_ITEM_ADD);
    }

}
