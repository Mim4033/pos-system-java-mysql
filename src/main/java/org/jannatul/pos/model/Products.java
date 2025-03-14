package org.jannatul.pos.model;

public class Products {
    private int id;
    private String reciptId;
    private String name;
    private double price;
    private int vatRate;
    public Products(int id, String name, double price, int vatRate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.vatRate = vatRate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getVatRate() {
        return vatRate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVatRate(int vatRate) {
        this.vatRate = vatRate;
    }
    public String getReciptId() {
        return reciptId;
    }
}
