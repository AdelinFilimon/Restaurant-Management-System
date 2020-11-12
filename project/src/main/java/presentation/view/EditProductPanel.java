package presentation.view;

import business.CompositeProduct;
import business.MenuItem;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class EditProductPanel extends JPanel {

    private final JLabel prodNameLabel;
    private final JLabel prodPriceLabel;
    private final JLabel prodListLabel;
    private final JTextField prodNameTextField;
    private final JTextField prodPriceTextField;
    private final Hashtable<JCheckBox, MenuItem> checkBoxMap;
    private final JScrollPane itemsScrollPane;

    public EditProductPanel(int indexOfProduct, Restaurant restaurant) {

        prodNameLabel = new JLabel("Product name: ");
        prodPriceLabel = new JLabel("Product price: ");
        prodListLabel = new JLabel("Composed of: ");
        prodNameTextField = new JTextField(restaurant.getMenu().get(indexOfProduct).itemName);
        prodPriceTextField = new JTextField(Double.toString(restaurant.getMenu().get(indexOfProduct).computePrice()));

        checkBoxMap = new Hashtable<>();
        itemsScrollPane = new JScrollPane();

        if(restaurant.getMenu().get(indexOfProduct) instanceof CompositeProduct) {
            for (MenuItem menuItem : restaurant.getMenu()) {
                if(menuItem.itemName.equals(restaurant.getMenu().get(indexOfProduct).itemName)) continue;
                JCheckBox checkBox = new JCheckBox(menuItem.itemName);
                checkBoxMap.put(checkBox, menuItem);
                if(((CompositeProduct)restaurant.getMenu().get(indexOfProduct)).getComposition().contains(menuItem)) {
                    checkBox.setSelected(true);
                }
            }
            prodPriceTextField.setVisible(false);
            prodPriceLabel.setVisible(false);
        } else {
            prodListLabel.setVisible(false);
            itemsScrollPane.setVisible(false);
        }
        setGrid();
    }

    private void setGrid() {
        JPanel itemsPanel = new JPanel();
        setLayout(new MigLayout());
        prodNameTextField.setColumns(16);
        prodPriceTextField.setColumns(16);
        add(prodNameLabel, "span, split 2, gaptop 10%, gapleft 7%");
        add(prodNameTextField, "wrap");
        add(prodPriceLabel, "span, split 2, gapleft 7%");
        add(prodPriceTextField, "wrap, gapleft 2%");
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

    public String getProdName() throws IllegalArgumentException {
        String productName = prodNameTextField.getText();
        String message = "Invalid product name.\nProduct name must contain only letters and have a minimum of 3 letters.\n";
        if(productName == null || !productName.matches("[a-zA-Z ]+") || productName.length() < 3)
            throw new IllegalArgumentException(message);
        return productName;
    }

    public double getProdPrice() throws IllegalArgumentException {
        String productPriceString = prodPriceTextField.getText();
        String message = "Invalid product price.\nProduct price must be a positive number.\n";
        if(productPriceString == null) throw new IllegalArgumentException("The product must have a price\n");
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
