package presentation.controller;

import business.MenuItem;
import business.Order;
import business.Restaurant;
import presentation.view.AddOrderPanel;
import presentation.view.WaiterGraphicalUserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class WaiterController {

    private int orderNumber;
    private final Restaurant model;
    private final WaiterGraphicalUserInterface view;

    public WaiterController(Restaurant restaurant, WaiterGraphicalUserInterface gui) {
        model = restaurant;
        view = gui;
        orderNumber = restaurant.getOrders().size();

        view.setAddOrderListener(new AddOrderListener());
        view.setComputePriceListener(new ComputePriceListener());
        view.setGenerateBillListener(new GenerateBillListener());
    }

    private class AddOrderListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddOrderPanel panel = new AddOrderPanel(model);
            panel.setSize(400,500);
            int option = JOptionPane.showConfirmDialog(null, panel, "Add order",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION) {
                Date date;
                int tableNumber;
                ArrayList<MenuItem> items;
                try {
                    date = panel.getDate();
                    tableNumber = panel.getTableNumber();
                    items = panel.getProducts();
                    if(items.size() == 0) throw new IllegalArgumentException("Empty order\n");
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Order order = model.createOrder(orderNumber, date, tableNumber, items);
                view.updateAddOrder(order.orderId);
                orderNumber++;
            }
        }
    }

    private class ComputePriceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getTable().getSelectedRow();
            if(row != -1) {
                Order order = model.getOrderById(row);
                JOptionPane.showMessageDialog(null, "Total: " + model.computePrice(order),
                        "Total price", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class GenerateBillListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getTable().getSelectedRow();
            if(row != -1) {
                try {
                    model.generateBill(model.getOrderById(row));
                    JOptionPane.showMessageDialog(null, "Bill generated successfully",
                            "Bill info", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(null, "Bill could not be generated: \n" + exception.getMessage(),
                            "Generate bill failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
