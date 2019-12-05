package com.example.myitime;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private List<EditItem> listEditItem = new ArrayList<EditItem>();
    private ImageButton buttonDone,buttonBack;
    private EditText editName,editRemark;
    private String Title;//活动名称
    private String Description;//活动描述
    private int year;//事件年份
    private int month;//事件月份
    private int date;//事件日期
    private ImageView Backgroundimg;

    //每次打开这个页面都要初始化list，添加日期、重复、图片、置顶四个功能
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        buttonDone=(ImageButton)findViewById(R.id.image_button_done);
        buttonDone.setOnClickListener(new Affirm());
        buttonBack=(ImageButton)findViewById(R.id.image_button_back);
        buttonBack.setOnClickListener(new Back());

        //获得活动标题
        editName=(EditText)findViewById(R.id.edit_name_text);
        //获得活动备注
        editRemark=(EditText)findViewById(R.id.edit_remark_text);
        //还要获得活动时间传回主页面进行处理

        Backgroundimg=(ImageView)findViewById(R.id.back_ground_image_view);

        init();
        final ItemArrayAdapter adapter=new ItemArrayAdapter(EditActivity.this,R.layout.edit_items,listEditItem);
        ListView listView=(ListView)findViewById(R.id.setting_item_list);
        listView.setAdapter(adapter);


    }
    class Affirm implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //点击确认后将该页面上设置的信息传回主页面
            Intent intent=new Intent();
            intent.putExtra("title",editName.getText().toString());
            intent.putExtra("year",listEditItem.get(0).getYear());
            intent.putExtra("month",listEditItem.get(0).getMonth());
            intent.putExtra("date",listEditItem.get(0).getDate());
            intent.putExtra("image",listEditItem.get(2).getImage());
            intent.putExtra("stick",listEditItem.get(3).isStick());

            setResult(RESULT_OK,intent);
            EditActivity.this.finish();
        }
    }
    class Back implements View.OnClickListener{
        public void onClick(View v){
            setResult(RESULT_CANCELED);
            EditActivity.this.finish();
        }
    }

    private void init() {
        //在这里面设置Date、Period、Image、Stick四项到List中并显示
        listEditItem.add(new EditItem("Date",android.R.drawable.ic_menu_recent_history, " "));
        listEditItem.add(new EditItem("Period",android.R.drawable.ic_menu_rotate, " "));
        listEditItem.add(new EditItem("Image",android.R.drawable.ic_menu_gallery, " "));
        listEditItem.add(new EditItem("Stick",android.R.drawable.ic_menu_upload, " "));
    }

    private class EditItem {//在这里面设置方法对活动类进行设置，显示

        private String Title;//活动名称
        private String Description;//活动描述
        private int year;//事件年份
        private int month;//事件月份
        private int date;//事件日期

        private int image;//事件图片

        private String period;//事件周期
        //不设秒，默认0秒
        private String Item;//项名称
        private int ItemIcon;//项图标id
        private boolean stick;//是否置顶



        private EditItem(String item,int icon, String description){
            this.setItem(item);
            this.setItemIcon(icon);
            this.setDescription(description);
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getPeriod() { return period; }

        public boolean isStick() {
            return stick;
        }

        public void setStick(boolean stick) {
            this.stick = stick;
        }
        public void setPeriod(String period) { this.period = period; }

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
            final EditItem item=getItem(position);
            View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            ((ImageView)view.findViewById(R.id.item_image_view)).setImageResource(item.getItemIcon());
            ((TextView)view.findViewById(R.id.item_name)).setText(item.getItem());
            ((TextView)view.findViewById(R.id.item_description)).setText("");
            TextView name=view.findViewById(R.id.item_name);
            final String tmp=name.getText().toString();
            final TextView description=view.findViewById(R.id.item_description);

            name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(EditActivity.this, tmp, Toast.LENGTH_SHORT).show();
                    switch (tmp) {
                        case "Date":
                            //这里弹出时间选择窗口
                            AlertDialog.Builder localBuilder = new AlertDialog.Builder(EditActivity.this);
                            localBuilder.setTitle("选择时间").setIcon(item.getItemIcon());
                            //
                            final LinearLayout layout_alert = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_dataselect, null);
                            localBuilder.setView(layout_alert);
                            localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                                    DatePicker datepicker1 = (DatePicker) layout_alert.findViewById(R.id.datepicker1);
                                    int year = datepicker1.getYear();
                                    int month = datepicker1.getMonth() + 1;
                                    int date = datepicker1.getDayOfMonth();
                                    item.setYear(year);
                                    item.setMonth(month);
                                    item.setDate(date);
                                    item.setDescription(Integer.toString(year) + '-' + month + '-' + date);//string加int则默认将int转string
                                    description.setText(Integer.toString(year) + '-' + month + '-' + date);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                                }
                            }).create().show();
                            break;

                        case "Period":
                            AlertDialog.Builder dialog_period = new AlertDialog.Builder(EditActivity.this);
                            dialog_period.setTitle("活动周期");
                            final String[] period = new String[]{"周", "月", "年", "不重复"};
                            dialog_period.setItems(period, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    item.setPeriod(period[which]);
                                    description.setText(period[which]);
                                }
                            });
                            dialog_period.show();
                            break;

                        case "Image":
                            //这里转到设定图片的逻辑
                            final String radioItems[] = new String[]{"默认图片1","默认图片2","默认图片3","默认图片4","默认图片5"};

                            AlertDialog.Builder radioDialog = new AlertDialog.Builder(EditActivity.this);
                            radioDialog.setTitle("图片设置");

                            radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch(which){
                                        case 0:
                                            item.setImage(R.drawable.pic1);
                                            Backgroundimg.setImageResource(R.drawable.pic1);
                                            Toast.makeText(EditActivity.this,"pic1",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1:
                                            item.setImage(R.drawable.pic2);
                                            Backgroundimg.setImageResource(R.drawable.pic2);
                                            Toast.makeText(EditActivity.this,"pic2",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            item.setImage(R.drawable.pic3);
                                            Backgroundimg.setImageResource(R.drawable.pic3);
                                            Toast.makeText(EditActivity.this,"pic3",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            item.setImage(R.drawable.pic4);
                                            Backgroundimg.setImageResource(R.drawable.pic4);
                                            Toast.makeText(EditActivity.this,"pic4",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 4:
                                            item.setImage(R.drawable.pic5);
                                            Backgroundimg.setImageResource(R.drawable.pic5);
                                            Toast.makeText(EditActivity.this,"pic5",Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                    }
                                }
                            });

                            //设置按钮
                            radioDialog.setPositiveButton("OK"
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });

                            radioDialog.create().show();
                            break;
                        case "Stick":
                            //这里转到设定置顶的逻辑
                            final String top[] = new String[]{"是","否"};

                            AlertDialog.Builder stickdiolog = new AlertDialog.Builder(EditActivity.this);
                            stickdiolog.setTitle("是否置顶");

                            stickdiolog.setSingleChoiceItems(top, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch(which){
                                        case 0:
                                            item.setStick(true);
                                            item.setDescription("置顶");
                                            Toast.makeText(EditActivity.this,"置顶",Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1:
                                            item.setStick(false);
                                            Toast.makeText(EditActivity.this,"取消置顶",Toast.LENGTH_SHORT).show();
                                            item.setDescription("");
                                            break;
                                        default:
                                    }
                                }
                            });
                            //设置按钮
                            stickdiolog.setPositiveButton("OK"
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });

                            stickdiolog.create().show();
                            break;
                    }
                }


            });
            return view;
        }
    }
}
