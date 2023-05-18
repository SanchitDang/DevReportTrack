package com.sanapplications.devreporttrack.Models;


public class ReportWorkModel {

    private String dataTitle;
    private String dataDesc;
    private String dataLang;
    private String dataImage;
    private String key;

    public ReportWorkModel(){

    }

    public ReportWorkModel(String dataTitle, String dataDesc, String dataLang, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataImage = dataImage;
    }

    public String getKey() {
        return key;
    }

    public String getDataTitle() {
        return dataTitle;
    }

//    public String getLang() {
//        return dataLang;
//    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setKey(String key) {
        this.key = key;
    }

}