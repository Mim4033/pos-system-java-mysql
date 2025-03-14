package org.jannatul.pos.DB;

import org.jannatul.pos.model.productsInRecipt;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptDAO {
    public void saveReceipt(String receiptId, String dateTime, double totalAmount, List<productsInRecipt> items) {
        Connection conn = null;
        try {
            conn = db_connection.getConnection();  // Use your existing connection class

            // Insert receipt into receipts table
            String receiptSql = "INSERT INTO receipts (receipt_id, date_time, total_amount) VALUES (?, ?, ?)";
            PreparedStatement receiptStmt = conn.prepareStatement(receiptSql);
            receiptStmt.setString(1, receiptId);
            receiptStmt.setTimestamp(2, Timestamp.valueOf(dateTime));  // convert your string to timestamp if needed
            receiptStmt.setDouble(3, totalAmount);


            receiptStmt.executeUpdate();

            // Insert each item into receipt_items table
            String itemSql = "INSERT INTO receipt_items (receipt_id, product_name, quantity, price, subtotal, subtotal_vat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);

            for (productsInRecipt item : items) {
                itemStmt.setString(1, receiptId);
                itemStmt.setString(2, item.getProductName());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.setDouble(5, item.getSubtotal());
                itemStmt.setDouble(6, item.getSubtotal_vat());

                itemStmt.addBatch(); // For better performance
            }

            itemStmt.executeBatch(); // Execute all batched inserts

            System.out.println("Receipt and items saved successfully!");

            // Clean up
            receiptStmt.close();
            itemStmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace(); // Show error in console/logs
        } finally {
            try {
                if (conn != null) conn.close(); // Always close connection!
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public Map<String, List<productsInRecipt>> groupByReceiptId(List<productsInRecipt> products) {
        Map<String, List<productsInRecipt>> receiptMap = new HashMap<>();

        for (productsInRecipt item : products) {
            String receiptId = item.getReceiptId();

            if (!receiptMap.containsKey(receiptId)) {
                receiptMap.put(receiptId, new ArrayList<>());
            }
            receiptMap.get(receiptId).add(item);
        }

        return receiptMap;
    }
    public List<productsInRecipt> getTodaysReceipts() {
        List<productsInRecipt> items = new ArrayList<>();

        String todayDatePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String query = "SELECT * FROM receipt_items WHERE receipt_id LIKE ?";

        try (Connection connection = db_connection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "REC" + todayDatePrefix + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String receiptId = rs.getString("receipt_id");
                    String productName = rs.getString("product_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    double subtotal = rs.getDouble("subtotal");
                    double subtotalVat = rs.getDouble("subtotal_vat");

                    productsInRecipt item = new productsInRecipt(receiptId, productName, quantity, price, subtotal, subtotalVat);
                    items.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

}
