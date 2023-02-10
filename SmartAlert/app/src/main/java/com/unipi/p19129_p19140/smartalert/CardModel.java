package com.unipi.p19129_p19140.smartalert;

public class CardModel {
    private String text;
    private int imgid;
    public CardModel(String text,int imgid){
        this.text=text;
        this.imgid=imgid;
    }
    public String getTitle(){
        return text;
    }
    public void setText(String text){
        this.text=text;
    }
    public int getImgid(){
        return imgid;
    }
    public void setImgid(int imgid){
        this.imgid=imgid;
    }
}
