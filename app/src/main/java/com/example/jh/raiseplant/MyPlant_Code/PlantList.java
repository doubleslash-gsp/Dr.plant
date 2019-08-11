package com.example.jh.raiseplant.MyPlant_Code;

public class PlantList {

    private int num;
    private String pic;
    private String name;
    private String kind;
    private String water;
    private int humidity;
    private int temper;
    private String lastwater;
    private String firstday;
    private String kind_eng;
    private String qr_code;

    public PlantList(){

    }

    public PlantList(int num, String name, String kind, String water, String qr_code, int humidity, int temper) {
        this.num = num;
        this.name = name;
        this.kind = kind;
        this.water = water;
        this.qr_code = qr_code;
        this.humidity = humidity;
        this.temper = temper;
    }

    public PlantList(int num, String name, String kind, String water, int humidity, int temper, String lastwater, String firstday, String kind_eng) {
        this.num = num;
        this.name = name;
        this.kind = kind;
        this.water = water;
        this.humidity = humidity;
        this.temper = temper;
        this.lastwater = lastwater;
        this.firstday = firstday;
        this.kind_eng = kind_eng;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public int getTemper() {
        return temper;
    }

    public void setTemper(int temper) {
        this.temper = temper;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLastwater() {
        return lastwater;
    }

    public void setLastwater(String lastwater) {
        this.lastwater = lastwater;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getFirstday() {
        return firstday;
    }

    public void setFirstday(String firstday) {
        this.firstday = firstday;
    }

    public String getKind_eng() {
        return kind_eng;
    }

    public void setKind_eng(String kind_eng) {
        this.kind_eng = kind_eng;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
