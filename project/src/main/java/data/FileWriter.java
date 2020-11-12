package data;


import business.MenuItem;
import business.Order;

import java.io.IOException;
import java.util.Collection;

public final class FileWriter {

    private static final FileWriter singleObject = new FileWriter();

    private FileWriter() {}

    public static FileWriter getInstance() {
        return singleObject;
    }

    public void writeBill(String filename, Order order, Collection<MenuItem> items, double total) throws IOException {
        java.io.FileWriter fl = new java.io.FileWriter(filename);
        fl.append("Bill\n");
        fl.append("Date: ").append(order.date.toString()).append("\n");
        fl.append("Order: ").append(String.valueOf(order.orderId)).append(" table: ").append(String.valueOf(order.tableNumber)).append("\n");
        for(MenuItem menuItem : items) {
            fl.append(menuItem.itemName).append(": ").append(String.valueOf(menuItem.computePrice())).append("lei\n");
        }
        fl.append("Total: ").append(String.valueOf(total)).append("lei\n");
        fl.close();
    }
}
