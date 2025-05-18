package com.example.kitchenwhiz.Model;

public class Ingredients {
    private String name;
    private double amount;
    private String unit;

    public Ingredients(String name, double amount, String unit){
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName(){
        return name;
    }

    public double getAmount() {
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
