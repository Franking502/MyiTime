package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_ITEM_ADD = 901;
    ListView listViewItems;
    private List<Item> listItem = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    FloatingActionButton addButton;
    ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
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

        //设置floatbutton的功能
        addButton=(FloatingActionButton) findViewById(R.id.floating_action_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里跳转到新建页面活动
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                startActivityForResult(intent, NEW_ITEM_ADD);
            }
        });
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

    private void init() {
        //listItem.add(new Item("test1",R.drawable.pic1));
        //listItem.add(new Item("test2",R.drawable.pic2));
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
            Item item = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.item_image_view)).setImageResource(item.getCoverResourceId());
            ((TextView) view.findViewById(R.id.item_name)).setText(item.getTitle());
            ((TextView)view.findViewById(R.id.item_description)).setText(Integer.toString(item.getYear())+'/'+Integer.toString(item.getMonth())+'/'+Integer.toString(item.getDate()));
            return view;
        }
    }
}
