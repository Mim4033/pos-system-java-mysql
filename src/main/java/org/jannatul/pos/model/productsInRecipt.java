package org.jannatul.pos.model;

public class productsInRecipt {

    private String receiptId;
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;
    private double subtotal_vat;

    public productsInRecipt(String receiptId, String productName, int quantity, double price, double subtotal, double subtotal_vat) {
        this.receiptId = receiptId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price * quantity;
        this.subtotal_vat = subtotal_vat;
    }
    public String getReceiptId() {
        return receiptId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return quantity * price;
    }

    public double getSubtotal_vat() {
        return subtotal_vat;
    }
}
