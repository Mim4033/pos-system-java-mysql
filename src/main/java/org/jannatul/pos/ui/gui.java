package org.jannatul.pos.ui;
import org.jannatul.pos.DB.*;
import org.jannatul.pos.model.*;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class gui {
    private JPanel panel1;
    private JPanel panelRight;
    private JPanel panelLeft;
    private JPanel panelTitle;
    private JPanel panelRightTitle;
    private JPanel panelLeftTitle;
    private JTextArea resultTextArea;
    private JTextArea receiptArea;
    private JPanel buttonPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton addButton;
    private JButton deletebutton;
    private JButton editbutton;
    private JButton payButton;
    private JPanel buttonsPanel;
    private JButton uploadButton;
    private double totalamount = 0.0;
    private double totalVAT25 = 0.0;
    private double totalVAT12 = 0.0;
    private double totalProductVAT = 0.0;
    private double ProductVAT12 = 0.0;
    private double ProductVAT25 = 0.0;
    private double totalBrutto12 = 0.0;
    private double totalBrutto25 = 0.0;

    private int receiptCounter = 1;
    private List<productsInRecipt> reciptItem = new ArrayList<>();



    private ProductDAO productDAO = new ProductDAO();
    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public gui () {
        receiptArea.append("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨\n");
        receiptArea.append("☕️🍩   WELCOME TO   ☕️🍰\n");
        receiptArea.append("🎉  JF BREWS & BITES  🎉\n");
        receiptArea.append("🥐☕️  GOOD VIBES & GREAT COFFEE  ☕️🍪\n");
        receiptArea.append("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨\n");
        receiptArea.append("🕒 " + getCurrentDateTime() + "\n");
        receiptArea.append("====================================\n");


                for (Products products : productDAO.getAllProducts()) {
                    JButton button = new JButton(products.getName());
                    buttonsPanel.add(button);

                    button.addActionListener(e -> {
                        textField1.setText(products.getName());

                    });
                }
                addButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedProductName = textField1.getText();
                        textField1.setText("");
                        int productAmount = 1;


                        try {
                            productAmount = Integer.parseInt(textField2.getText());
                        } catch (NumberFormatException ex) {
                        }
                        ProductDAO productDAO = new ProductDAO();


                        Products selectedProduct = null;
                        for (Products product : productDAO.getAllProducts()) {
                            if (product.getName().equals(selectedProductName)) {
                                selectedProduct = product;
                                break;
                            }
                        }
                        double totalProductAmountPrice = 0;
                        if (selectedProduct != null) {
                            totalProductAmountPrice = selectedProduct.getPrice() * productAmount;

                            int vatRate = selectedProduct.getVatRate();
                            switch (vatRate) {
                                case 12:
                                    // Calculate Brutto for 12% VAT products
                                    totalBrutto12 += totalProductAmountPrice;

                                    // Calculate VAT (Moms) for 12% VAT products
                                    double productVAT12 = totalProductAmountPrice * vatRate / 100.0;
                                    totalVAT12 += productVAT12;

                                    break;

                                case 25:
                                    // Calculate Brutto for 25% VAT products
                                    totalBrutto25 += totalProductAmountPrice;

                                    // Calculate VAT (Moms) for 25% VAT products
                                    double productVAT25 = totalProductAmountPrice * vatRate / 100.0;
                                    totalVAT25 += productVAT25;

                                    break;

                                default:
                                    System.out.println("Unknown VAT rate: " + vatRate);
                                    break;
                            }

                        }


                        receiptArea.append(String.format("%-15s %2d x %.2f kr = %.2f kr\n",
                                selectedProduct.getName(), productAmount, selectedProduct.getPrice(), totalProductAmountPrice));
                        totalamount += totalProductAmountPrice;


                        textField2.setText("");
                        productsInRecipt item = new productsInRecipt(selectedProduct.getReciptId(), selectedProduct.getName(), productAmount, selectedProduct.getPrice(), totalProductAmountPrice, selectedProduct.getVatRate());
                        reciptItem.add(item);

                    }


                });

                payButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LocalDateTime now = LocalDateTime.now();
                        String dateTime = getCurrentDateTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        String receiptId = String.format("REC" + now.format(formatter));
                        double totalNetto12 = totalBrutto12 - totalVAT12;
                        double totalNetto25 = totalBrutto25 - totalVAT25;

                        receiptArea.append("------------------------------------\n");

                        receiptArea.append(String.format("\nTotal: %.2f kr\n", totalamount));
                        receiptArea.append("\n");
                        receiptArea.append("Receipt ID: " + receiptId + "\n");
                        receiptArea.append("Date: " + dateTime + "\n");
                        receiptArea.append("------------------------------------\n");
                        receiptArea.append("-------------------------------\n");
                        receiptArea.append(String.format("%-8s %-8s %-8s %-8s\n", "Moms%", "Moms", "Netto", "Brutto"));
                        receiptArea.append("-------------------------------\n");
                        receiptArea.append(String.format("%-8s %-8.2f %-8.2f %-8.2f\n", "12%", totalVAT12, totalNetto12, totalBrutto12));
                        receiptArea.append(String.format("%-8s %-8.2f %-8.2f %-8.2f\n", "25%", totalVAT25, totalNetto25, totalBrutto25));
                        receiptArea.append("-------------------------------\n");
                        receiptArea.append("Hungry again? 🍽️ We’ll be here! See you soon! 😄\n");
                        ReceiptDAO receiptDAO = new ReceiptDAO();
                        receiptDAO.saveReceipt(receiptId,dateTime,totalamount,reciptItem);

                        Timer clearReceiptTimer = new Timer(10000, evt ->
                                receiptArea.setText(
                                        "✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨\n" +
                                                "☕️🍩   WELCOME TO   ☕️🍰\n" +
                                                "🎉  JF BREWS & BITES  🎉\n" +
                                                "🥐☕️  GOOD VIBES & GREAT COFFEE  ☕️🍪\n" +
                                                "✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨\n" +
                                                "🕒 " + getCurrentDateTime() + "\n" +
                                                "====================================\n"
                                )
                        );
                        clearReceiptTimer.setRepeats(false);
                        clearReceiptTimer.start();
                        totalamount = 0.0;
                        totalVAT12 = 0.0;
                        totalVAT25 = 0.0;
                        totalBrutto12 = 0.0;
                        totalBrutto25 = 0.0;
                        reciptItem.clear();
                        receiptCounter++;
                    }

                });

                uploadButton.addActionListener(e -> {
                    try {
                // 1️⃣ Get today's receipts from the database
                ReceiptDAO receiptDAO = new ReceiptDAO();
                List<productsInRecipt> todaysReceipts = receiptDAO.getTodaysReceipts();  // You need to implement this query!

                // 2️⃣ Generate XML string from receipts
                String dailySalesXML = xmlGenerator.generateDailySalesXML(todaysReceipts);

                // 3️⃣ Upload XML to S3
                S3Connection s3Uploader = new S3Connection(dailySalesXML);

                // 4️⃣ Provide feedback to the user
                JOptionPane.showMessageDialog(null, "✅ Daily sales uploaded to S3 successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "❌ Upload failed. Check logs for details.");
            }
        });



    }

            public void run() {

                JFrame frame = new JFrame("JF Brews & Bites POS");
                frame.setContentPane(new gui().panel1);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setSize(1000, 850);
            }


            private void createUIComponents() {

            }


        }
