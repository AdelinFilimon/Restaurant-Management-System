package business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IRestaurantProcessing {

    MenuItem createMenuItem(String menuItemName, double price, ArrayList<MenuItem> menuItems);
    void deleteMenuItem(MenuItem menuItem);
    void editMenuItem(MenuItem menuItem, String menuItemName, double price, ArrayList<MenuItem> items);
    Order createOrder(int orderId, Date date, int table, List<MenuItem> menuItems);
    double computePrice(Order order);
    void generateBill(Order order) throws IOException;
}
