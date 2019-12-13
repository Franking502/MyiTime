package com.example.myitime;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ItemSaver {
    Context context;//用于读写内部文件
    ArrayList<Item> items=new ArrayList<Item>();
    public ItemSaver(Context context) {
        this.context = context;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void save(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt" , Context.MODE_PRIVATE));
            outputStream.writeObject(items);
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Item> load(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            items = (ArrayList<Item>) inputStream.readObject();
            inputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }
}
