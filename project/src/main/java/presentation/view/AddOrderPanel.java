package presentation.view;

import business.MenuItem;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

public class AddOrderPanel extends JPanel {


    private final Restaurant model;
    private final JLabel dateLabel;
    private final JLabel tableNrLabel;
    private final JLabel menuLabel;
    private final JLabel orderItemsLabel;
    private final JList<String> productsList;
    private final DefaultListModel<String> productsListModel;
    private final JList<String> menuList;

    private final JSpinner dateSpinner;
    private final JTextField tableNrTextField;

    public AddOrderPanel(Restaurant restaurant) {
        this.model = restaurant;
        dateLabel = new JLabel("Date: ");
        tableNrLabel = new JLabel("Table number: ");
        menuLabel = new JLabel("Menu");
        orderItemsLabel = new JLabel("Order items");
        productsListModel = new DefaultListModel<>();

        productsList = new JList<>(productsListModel);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        tableNrTextField = new JTextField();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(MenuItem menuItem : model.getMenu()) {
            listModel.addElement(menuItem.itemName);
        }
        menuList = new JList<>(listModel);

        setAddProductListener();
        setGrid();
    }

    private void setGrid() {
        JScrollPane productsPane = new JScrollPane(productsList);
        JScrollPane menuPane = new JScrollPane(menuList);
        setLayout(new MigLayout());

        Component spinnerEditor = dateSpinner.getEditor();
        JFormattedTextField textField =  ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
        textField.setColumns(10);
        tableNrTextField.setColumns(10);
        productsPane.setPreferredSize(new Dimension(190,300));
        menuPane.setPreferredSize(new Dimension(190, 300));

        add(dateLabel, "span, split 2, gapleft 15%");
        add(dateSpinner,"wrap");
        add(tableNrLabel, "span, split 2");
        add(tableNrTextField, "wrap");
        add(menuLabel, "gaptop 10%, gapleft 20%");
        add(orderItemsLabel, "wrap, gapleft 35%");
        add(menuPane, "span, split 2");
        add(productsPane, "wrap");
    }

    private void setAddProductListener() {
        menuList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(menuList.getSelectedIndex() != -1 && e.getClickCount() == 2) {
                    productsListModel.addElement(menuList.getSelectedValue());
                }
            }
        });
    }

    public Date getDate() {
        return (Date) dateSpinner.getValue();
    }

    public int getTableNumber() throws IllegalArgumentException {
        String tableNumberString = tableNrTextField.getText();
        String message = "Invalid table number.\n";
        if(tableNumberString == null) throw new IllegalArgumentException("Please provide a table number.\n");
        int tableNumber = Integer.parseInt(tableNumberString);
        if(tableNumber <= 0) throw new IllegalArgumentException(message);
        return tableNumber;
    }

    public ArrayList<MenuItem> getProducts() {
        ArrayList<MenuItem> items = new ArrayList<>();
        for(int i = 0; i < productsListModel.size(); i++) {
            MenuItem item = model.getMenuItemByName(productsListModel.get(i));
            items.add(item);
        }
        return items;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,500);
    }
}
