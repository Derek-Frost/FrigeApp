package com.example.frigeapp;

public class Product {
    private String name;
    private String count;
    private String weight;
    private String data;
    private String weight_1_piece;
    private String latitude;
    private String longtitude;
    private String refrigerator_id;
    public Product(String name,String count,String weight,String data)
    {
        this.name = name;
        this.count = count;
        this.weight = weight;
        this.data = data;
        if(!count.equals("-")) this.weight_1_piece = String.valueOf((Double.parseDouble(weight)/Double.parseDouble(count)));
        else this.weight_1_piece = "0";
    }

    public Product(String name,String count,String weight)
    {
        this.name = name;
        this.count = count;
        this.weight = weight;

    }

    public void setRefrigerator_id(String refrigerator_id) {
        this.refrigerator_id = refrigerator_id;
    }

    public String getRefrigerator_id() {
        return refrigerator_id;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setWeight_1_piece() {
        this.weight_1_piece = String.valueOf((Double.parseDouble(weight)/Double.parseDouble(count)));
    }

    public Product(String name, String count, String weight, String latitude, String longtitude) {
        this.name = name;
        this.count = count;
        this.weight = weight;
        if(count != null && !count.equals("-"))
            this.weight_1_piece = String.valueOf((Double.parseDouble(weight)/Double.parseDouble(count)));
        else this.weight_1_piece = "0";
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setCount(String count) {
        this.count = count;
        this.weight = String.valueOf((Double.parseDouble(this.weight) - Double.parseDouble(this.weight_1_piece)));
    }
    public void setCountCustom(String count) {
        this.count = count;
    }
    public String getWeight_1_piece() {
        return weight_1_piece;
    }

    public String getData() {
        return data;
    }

    public String getWeight() {
        return weight;
    }

    public String getCount() {
        return count;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }
}
