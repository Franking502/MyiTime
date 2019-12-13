package com.example.myitime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {

    ImageButton editButton, deletButton, backButton;
    public static final int ITEM_EDIT = 903;
    public static final int ITEM_DEL = 904;
    private String title;
    private int year;
    private int month;
    private int date;
    private int image;
    private int index;
    private Timer mTimer;
    ImageView backGround;
    TextView titleText;
    TextView setTime;
    TextView mDays_Tv, mHours_Tv, mMinutes_Tv, mSeconds_Tv,remainText;
    public static final int ITEM_DETAIL = 902;

    //下面的具体时间通过系统时间获得，现在先初始化做倒计时
    Calendar calendar = Calendar.getInstance();
    private long mDay;// 天
    private long mHour;//小时,
    private long mMin;//分钟,
    private long mSecond;//秒
    private long setDay;// 天

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                if(mDay<0){
                    remainText.setText("已过");
                    mDays_Tv.setText((-mDay)+"");//天数不用补位
                }
                mHours_Tv.setText(getTv(mHour));
                mMinutes_Tv.setText(getTv(mMin));
                mSeconds_Tv.setText(getTv(mSecond));

            }
        }
    };

    private String getTv(long l){
        if(l>=10){
            return l+"";
        }else{
            return "0"+l;//小于10,前面补位一个"0"
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editButton=(ImageButton)findViewById(R.id.edit_image_button);//编辑按钮
        deletButton=(ImageButton)findViewById(R.id.delet_image_button);//删除按钮
        backButton=(ImageButton)findViewById(R.id.image_button_back);
        backGround=(ImageView)findViewById(R.id.image_back_ground);
        titleText=(TextView)findViewById(R.id.name_text_view);
        setTime=(TextView)findViewById(R.id.text_view_settime);

        mTimer = new Timer();
        mDays_Tv = (TextView) findViewById(R.id.day_left);
        mHours_Tv = (TextView) findViewById(R.id.hour_left);
        mMinutes_Tv = (TextView) findViewById(R.id.minute_left);
        mSeconds_Tv = (TextView) findViewById(R.id.second_left);
        remainText=(TextView)findViewById(R.id.remain_text);

        title=getIntent().getStringExtra("title");
        titleText.setText(title);
        year=getIntent().getIntExtra("year",2019);
        month=getIntent().getIntExtra("month",1);
        date=getIntent().getIntExtra("date",1);
        image=getIntent().getIntExtra("image",R.drawable.pic1);
        index=getIntent().getIntExtra("position",0);
        backGround.setImageResource(image);
        setTime.setText(String.valueOf(year)+'年'+String.valueOf(month)+'月'+String.valueOf(date)+'日');
        //获取当前时间，剩余天数由下面获得，24-当前小时为剩余小时，60-当前分为剩余分，60-当前秒为剩余秒
        mHour=24 - calendar.get(Calendar.HOUR_OF_DAY);
        mMin=60 - calendar.get(Calendar.MINUTE);
        mSecond=60 - calendar.get(Calendar.SECOND);

        //获取还有多少天
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
        mDay = (d1.getTime()-d2.getTime())/(60*60*1000*24);
        startRun();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到编辑页面

                Intent intent=new Intent(DetailActivity.this,EditActivity.class);
                intent.putExtra("title",titleText.getText());
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("date",date);
                intent.putExtra("image",image);
                startActivityForResult(intent, ITEM_EDIT);
            }
        });

        deletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //主界面点击时获取点击的时第几项index传到该页面
                //在这里返回index和删除的result代码，在主页面执行删除
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                intent.putExtra("position",index);
                intent.putExtra("ifdel",1);
                setResult(ITEM_DETAIL,intent);
                DetailActivity.this.finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                /*在这里获取当前剩余时间和设定的时间并返回*/
                intent.putExtra("leftday",mDay);
                intent.putExtra("lefthour",mHour);
                intent.putExtra("leftminute",mMin);
                intent.putExtra("leftsecond",mSecond);
                intent.putExtra("settime",setTime.getText());
                intent.putExtra("position",index);
                setResult(RESULT_CANCELED,intent);
                DetailActivity.this.finish();
            }
        });
    }

    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {

            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        //未过
        if(mDay>=0) {
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

                    }
                }
            }
        }
        //已过
        else if(mDay<0){
            mSecond++;
            if (mSecond > 60) {
                mMin++;
                mSecond = 0;
                if (mMin > 60) {
                    mMin = 0;
                    mHour++;
                    if (mHour > 24) {
                        // 倒计时结束
                        mHour = 0;
                        mDay++;

                    }
                }
            }
        }
    }
//获取从编辑页面传来的数据，修改year、month、date、backGround、title、setTime、


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){

            case RESULT_OK:
                String title=data.getStringExtra("title");
                this.year=data.getIntExtra("year",2019);
                this.month=data.getIntExtra("month",1);
                this.date=data.getIntExtra("date",1);
                this.image=data.getIntExtra("image",R.drawable.pic1);
                backGround.setImageResource(image);
                boolean stick=data.getBooleanExtra("stick",false);
                titleText.setText(title);
                setTime.setText(String.valueOf(this.year)+'年'+String.valueOf(this.month)+'月'+String.valueOf(this.date)+'日');

                //获取还有多少天
                String s1= String.valueOf(this.year)+'-'+String.valueOf(this.month)+'-'+String.valueOf(this.date);
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
                this.mDay = (d1.getTime()-d2.getTime())/(60*60*1000*24);
                startRun();
        }
    }
}