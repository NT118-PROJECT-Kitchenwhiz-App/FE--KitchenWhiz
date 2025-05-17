package com.example.kitchenwhiz.Model;

public class Ingredients {
    private String name;
    private long amount;
    private String unit;

    public Ingredients(String name, long amount, String unit){
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName(){
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void setAmount(long amount){
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
