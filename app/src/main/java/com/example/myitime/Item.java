package com.example.myitime;

public class Item {

    private String Title;//活动名称
    private int CoverResourceId;//图片id

    public Item(String title,int coverResourceId){
        setTitle(title);
        setCoverResourceId(coverResourceId);
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
}
