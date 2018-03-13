package com.example.mr_ja.loginfirebase;

/**
 * Created by Mr_Ja on 3/13/2018.
 */

public class ViewSingleItem {
    //the same name eith the database
    private String Image_URL,Image_Title;

    public ViewSingleItem(String image_URL, String image_Title){
        Image_URL = image_URL;
        Image_Title = image_Title;

    }


    public ViewSingleItem(){

    }

    public void setImage_URL(String image_URL){
        Image_URL = image_URL;
    }

    public String getImage_URL(){
        return Image_URL;
    }

    public void setImage_Title(String image_Title){

        Image_Title = image_Title;
    }

    public String getImage_Title(){
        return  Image_Title;
    }

































}
