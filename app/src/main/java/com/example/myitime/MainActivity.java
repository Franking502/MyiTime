package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

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
    ItemAdapter adapter;
    TextView TimeOnPic;
    ItemSaver itemSaver;
    private long mDay;// 天
    Timer mTimer = new Timer();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemSaver.save();
    }

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };
    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {

            public void run() {
                Message message = Message.obtain();
                refreshHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemSaver=new ItemSaver(this);
        listItem=itemSaver.load();
        listViewItems=this.findViewById(R.id.list_view_item);

        adapter = new ItemAdapter(
                MainActivity.this, R.layout.list_view_item, listItem);
        listViewItems.setAdapter(adapter);
        startRun();
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view); //nav_view是drawer中的内容
        ActionBar actionBar=getSupportActionBar();

        //下面逐个处理侧拉菜单中的各个选项：
        navView.setCheckedItem(R.id.nav_count);//设置默认选项为计时
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getTitle().equals("计时")) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
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
            break;
            case ITEM_DETAIL:
                //在这里接收是否删除和是否是返回的指示码
                int ifdel=data.getIntExtra("ifdel",0);
                int ifback=data.getIntExtra("back",0);
                if(ifdel == 1) {
                    int index = data.getIntExtra("position", -1);
                    if (index >= 0) {
                        listItem.remove(index);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                }
                if(ifback==111){
                    //返回处理，接收数据
                    String title=data.getStringExtra("name");
                    //返回设定的年月日，更改list中的item的年月日属性，不能只改剩余时间
                    int year=data.getIntExtra("year",0);
                    int month=data.getIntExtra("month",0);
                    int date=data.getIntExtra("date",0);

                    int index=data.getIntExtra("position",0);
                    int image=data.getIntExtra("image",R.drawable.pic1);
                    //改变index位置上的数据！！！！
                    TextView titletext=listViewItems.getChildAt(index).findViewById(R.id.item_name);
                    titletext.setText(title);
                    ImageView imageview=listViewItems.getChildAt(index).findViewById(R.id.item_image_view);
                    imageview.setImageResource(image);
                    getListItem().get(index).setCoverResourceId(image);
                    getListItem().get(index).setYear(year);
                    getListItem().get(index).setMonth(month);
                    getListItem().get(index).setDate(date);
                    getListItem().get(index).setTitle(title);
                    adapter.notifyDataSetChanged();
                }
            break;
            case RESULT_CANCELED:
                //在这里接收剩余时间并进行设置
                int position=data.getIntExtra("position",0);
                int image=data.getIntExtra("image",0);
                String setTime=data.getStringExtra("settime");
                TextView setTimeText=listViewItems.getChildAt(position).findViewById(R.id.item_description);
                ImageView coverimage=listViewItems.getChildAt(position).findViewById(R.id.item_image_view);
                setTimeText.setText(setTime);
                //在这里设置相应位置上的item的图片！！
                listItem.get(position).setCoverResourceId(image);
                coverimage.setImageResource(image);
                adapter.notifyDataSetChanged();
            break;
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
        int position;

        public ItemAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos=position;
            this.position=position;
            final Item item = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.item_image_view)).setImageResource(item.getCoverResourceId());
            ((TextView) view.findViewById(R.id.item_name)).setText(item.getTitle());
            ((TextView) view.findViewById(R.id.item_description)).setText(Integer.toString(item.getYear())+'年'+Integer.toString(item.getMonth())+'月'+Integer.toString(item.getDate())+'日');

            //下面处理首页图片上的时间显示
            TimeOnPic=view.findViewById(R.id.text_view_lefttime);
            int year=item.getYear();
            int month=item.getMonth();
            int date=item.getDate();
            Calendar calendar = Calendar.getInstance();
            int yearpassed=year-calendar.get(Calendar.YEAR);
            int monthpassed=month-calendar.get(Calendar.MONTH)-1;
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
                    if(daypssed<=0){
                        //处于设定日期当天也算已过
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
                String s2 = String.valueOf(calendar.get(Calendar.YEAR))+'-'+String.valueOf(calendar.get(Calendar.MONTH)+1)+'-'+String.valueOf(calendar.get(Calendar.DATE));
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
                if((mDay=(d2.getTime()-d1.getTime())/(60*60*1000*24)) != 0){
                    TimeOnPic.setText("已经"+String.valueOf(mDay)+"天"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"小时");
                }
                else if(calendar.get(Calendar.HOUR_OF_DAY) != 0){
                    //已过天数小于一天，显示已过小时数
                    TimeOnPic.setText("已经"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+"小时"+String.valueOf(calendar.get(Calendar.MINUTE))+"分钟"+String.valueOf(calendar.get(Calendar.SECOND))+"秒");
                }
                else if(calendar.get(Calendar.MINUTE) != 0){
                    TimeOnPic.setText("已经"+String.valueOf(calendar.get(Calendar.MINUTE))+"分钟"+String.valueOf(calendar.get(Calendar.SECOND))+"秒");
                }
                else if(calendar.get(Calendar.SECOND) != 0){
                    TimeOnPic.setText("已经"+String.valueOf(calendar.get(Calendar.SECOND))+"秒");
                }
            }
            else{
                //日期未过，计算还有多少天
                String s1= String.valueOf(year)+'-'+String.valueOf(month)+'-'+String.valueOf(date);
                String s2 = String.valueOf(calendar.get(Calendar.YEAR))+'-'+String.valueOf(calendar.get(Calendar.MONTH)+1)+'-'+String.valueOf(calendar.get(Calendar.DATE));
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
                if((mDay=(d1.getTime()-d2.getTime())/(60*60*1000*24))-1 != 0){
                    TimeOnPic.setText("还有"+String.valueOf(mDay)+"天"+String.valueOf(24-calendar.get(Calendar.HOUR_OF_DAY))+"小时");
                }
                else if(24-calendar.get(Calendar.HOUR_OF_DAY) != 0){
                    //距离天数小于一天，显示距离小时数
                    TimeOnPic.setText("还有"+String.valueOf(24-calendar.get(Calendar.HOUR_OF_DAY))+"小时"+String.valueOf(60-calendar.get(Calendar.MINUTE))+"分钟");
                }
                else if(60-calendar.get(Calendar.MINUTE) != 0){
                    TimeOnPic.setText("还有"+String.valueOf(60-calendar.get(Calendar.MINUTE))+"分钟"+String.valueOf(60-calendar.get(Calendar.SECOND))+"秒");
                }
                else if(60-calendar.get(Calendar.SECOND) != 0){
                    TimeOnPic.setText("还有"+String.valueOf(60-calendar.get(Calendar.SECOND))+"秒");
                }
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


    public void OnButtonClick(View v) {
        //这里跳转到新建页面活动
        Intent intent=new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent, NEW_ITEM_ADD);
    }

}
