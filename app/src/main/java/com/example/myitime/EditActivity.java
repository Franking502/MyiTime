package com.example.myitime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.myitime.DetailActivity.ITEM_EDIT;

public class EditActivity extends AppCompatActivity {

    int year;
    int month;
    int date;
    int image;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case ITEM_EDIT:
                String title=data.getStringExtra("title");
                int year=data.getIntExtra("year",2019);
                int month=data.getIntExtra("month",1);
                int date=data.getIntExtra("date",1);
                int image=data.getIntExtra("image",R.drawable.pic1);
        }
    }
}
