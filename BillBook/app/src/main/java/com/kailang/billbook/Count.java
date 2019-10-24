package com.kailang.billbook;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Count {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double money;
    private int type;
    private String date;
    private String describe;

    public Count() {

    }

//    public Count(Double money, String type, String date, String describe) {
//        this.money = money;
//        this.type = type;
//        this.date = date;
//        this.describe = describe;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
