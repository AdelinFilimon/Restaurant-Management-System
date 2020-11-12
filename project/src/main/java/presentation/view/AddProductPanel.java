package presentation.view;

import business.MenuItem;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Hashtable;

public class AddProductPanel extends JPanel{

    private final JRadioButton baseProductBtn;
    private final JRadioButton compProductBtn;
    private final JTextField productNameTextField;
    private final JTextField productPriceTextField;
    private final JLabel prodNameLabel;
    private final JLabel prodPriceLabel;
    private final JLabel prodListLabel;
    private final JScrollPane itemsScrollPane;
    private final Hashtable<JCheckBox, MenuItem> checkBoxMap;

    public AddProductPanel(Restaurant restaurant) {
        baseProductBtn = new JRadioButton("Base product");
        compProductBtn = new JRadioButton("Composite product");
        productNameTextField = new JTextField();
        productPriceTextField = new JTextField();
        prodNameLabel = new JLabel("Product name: ");
        prodPriceLabel = new JLabel("Product price: ");
        prodListLabel = new JLabel("Composed of: ");
        checkBoxMap = new Hashtable<>();
        itemsScrollPane = new JScrollPane();

        if(restaurant.getMenu().size() == 0) {
            compProductBtn.setEnabled(false);
        }
        prodListLabel.setVisible(false);
        itemsScrollPane.setVisible(false);

        for(MenuItem menuItem : restaurant.getMenu()) {
            JCheckBox checkBox = new JCheckBox(menuItem.itemName);
            checkBoxMap.put(checkBox, menuItem);
        }

        setCompProductBtnListener();
        setBaseProductBtnListener();
        setGrid();
    }

    private void setGrid() {
        ButtonGroup btnGroup = new ButtonGroup();
        JPanel itemsPanel = new JPanel();

        productNameTextField.setColumns(16);
        productPriceTextField.setColumns(16);

        btnGroup.add(baseProductBtn);
        btnGroup.add(compProductBtn);

        setLayout(new MigLayout());
        add(baseProductBtn, "gapleft 15%, gaptop 5%");
        add(compProductBtn, "wrap");
        add(prodNameLabel, "span, split 2, gapleft 7%");
        add(productNameTextField, "wrap");
        add(prodPriceLabel, "span, split 2, gapleft 7%");
        add(productPriceTextField, "gapleft 8, wrap");
        add(prodListLabel,"span, wrap, gapleft 35%");

        itemsPanel.setLayout(new MigLayout("wrap 3"));
        for(JCheckBox checkBox : checkBoxMap.keySet()) {
            itemsPanel.add(checkBox, "gapleft 20");
        }

        itemsScrollPane.setViewportView(itemsPanel);
        itemsScrollPane.setPreferredSize(new Dimension(400,300));
        itemsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(itemsScrollPane, "span, wrap");
    }

    private void setCompProductBtnListener() {
        compProductBtn.addActionListener((ActionEvent e) -> {
                    itemsScrollPane.setVisible(true);
                    prodPriceLabel.setVisible(false);
                    productPriceTextField.setVisible(false);
                    prodListLabel.setVisible(true);
                }
        );
    }

    private void setBaseProductBtnListener() {
        baseProductBtn.addActionListener((ActionEvent e) -> {
                    itemsScrollPane.setVisible(false);
                    prodPriceLabel.setVisible(true);
                    productPriceTextField.setVisible(true);
                    prodListLabel.setVisible(false);
                }
        );
    }

    public boolean baseProductChecked() {
        return baseProductBtn.isSelected();
    }

    public boolean compositeProductChecked() {
        return compProductBtn.isSelected();
    }

    public String getProdName() throws IllegalArgumentException {
        String productName = productNameTextField.getText();
        String message = "Invalid product name.\nProduct name must contain only letters and have a minimum of 3 letters.\n";
        if(productName == null || !productName.matches("[a-zA-Z ]+") || productName.length() < 3)
            throw new IllegalArgumentException(message);
        return productName;
    }

    public double getProdPrice() throws IllegalArgumentException {
        if(compProductBtn.isSelected()) return -1;
        String productPriceString = productPriceTextField.getText();
        String message = "Invalid product price.\nProduct price must be a positive number.\n";
        if(productPriceString == null) throw new IllegalArgumentException("Please provide a price for the product.\n");
        double productPrice = Double.parseDouble(productPriceString);
        if(productPrice <= 0) throw new IllegalArgumentException(message);
        return productPrice;
    }

    public ArrayList<MenuItem> getSelectedProd() {
        ArrayList<MenuItem> items = new ArrayList<>();
        for(JCheckBox checkBox : checkBoxMap.keySet()) {
            if(checkBox.isSelected()) {
                items.add(checkBoxMap.get(checkBox));
            }
        }
        return items;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,500);
    }
}
