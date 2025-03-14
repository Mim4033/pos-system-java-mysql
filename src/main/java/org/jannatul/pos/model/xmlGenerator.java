package org.jannatul.pos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class xmlGenerator {
    public static String generateDailySalesXML(List<productsInRecipt> dailyReceipts) {
        StringBuilder xmlBuilder = new StringBuilder();

        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<DailySales>\n");

        // Group receipts by receiptId
        Map<String, List<productsInRecipt>> receiptMap = new HashMap<>();

        for (productsInRecipt item : dailyReceipts) {
            String receiptId = item.getReceiptId();

            receiptMap.computeIfAbsent(receiptId, k -> new ArrayList<>()).add(item);
        }

        for (String receiptId : receiptMap.keySet()) {
            List<productsInRecipt> items = receiptMap.get(receiptId);

            double totalAmount = items.stream()
                    .mapToDouble(productsInRecipt::getSubtotal)
                    .sum();

            xmlBuilder.append("  <Receipt>\n");
            xmlBuilder.append("    <Id>").append(receiptId).append("</Id>\n");
            xmlBuilder.append("    <TotalAmount>").append(totalAmount).append("</TotalAmount>\n");
            xmlBuilder.append("    <Products>\n");

            for (productsInRecipt product : items) {
                xmlBuilder.append("      <Product>\n");
                xmlBuilder.append("        <ProductName>").append(product.getProductName()).append("</ProductName>\n");
                xmlBuilder.append("        <Quantity>").append(product.getQuantity()).append("</Quantity>\n");
                xmlBuilder.append("        <Price>").append(product.getPrice()).append("</Price>\n");
                xmlBuilder.append("        <Subtotal>").append(product.getSubtotal()).append("</Subtotal>\n");
                xmlBuilder.append("        <SubtotalVAT>").append(product.getSubtotal_vat()).append("</SubtotalVAT>\n");
                xmlBuilder.append("      </Product>\n");
            }

            xmlBuilder.append("    </Products>\n");
            xmlBuilder.append("  </Receipt>\n");
        }

        xmlBuilder.append("</DailySales>");

        return xmlBuilder.toString();
    }
}

