package presentation.controller;

import business.*;
import presentation.view.AddProductPanel;
import presentation.view.AdministratorGraphicalUserInterface;
import presentation.view.EditProductPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdministratorController {

    private final Restaurant model;
    private final AdministratorGraphicalUserInterface view;

    public AdministratorController(Restaurant restaurant, AdministratorGraphicalUserInterface gui) {
        model = restaurant;
        view = gui;

        view.setAddMenuListener(new AddMenuListener());
        view.setDeleteMenuListener(new DeleteMenuListener());
        view.setEditMenuListener(new EditMenuListener());

    }

    private class DeleteMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] rows = view.getTable().getSelectedRows();
            if(rows.length != 0) {
                ArrayList<MenuItem> toBeDeleted = new ArrayList<>();
                for(int row : rows) {
                    MenuItem item = model.getMenu().get(row);
                    toBeDeleted.add(model.getMenu().get(row));
                    for(MenuItem menuItem : model.getMenu()) {
                        if(menuItem instanceof CompositeProduct && ((CompositeProduct) menuItem).getComposition().contains(item)) {
                            toBeDeleted.add(menuItem);
                        }
                    }
                }

                for(MenuItem menuItem : toBeDeleted) {
                    model.deleteMenuItem(menuItem);
                }
                view.updateAll();
            }
        }
    }

    private class AddMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddProductPanel panel = new AddProductPanel(model);
            int option = JOptionPane.showConfirmDialog(null, panel, "Add product",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION) {
                String prodName;
                double prodPrice;
                ArrayList<MenuItem> menuItems;

                try {
                    prodName = panel.getProdName();
                    prodPrice = panel.getProdPrice();
                    menuItems = panel.getSelectedProd();
                    if(model.productExist(prodName)) throw new IllegalArgumentException("The product already exists.");
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MenuItem item;
                if(panel.baseProductChecked()) {
                    item = model.createMenuItem(prodName, prodPrice, null);
                } else if(panel.compositeProductChecked()) {
                    if(menuItems.size() == 0){
                        JOptionPane.showMessageDialog(null, "Select the base products of the composite product.\n",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    item = model.createMenuItem(prodName, prodPrice, menuItems);
                } else {
                    JOptionPane.showMessageDialog(null, "Select base product/composite product.\n",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int row = model.getMenu().indexOf(item);
                view.updateAddMenuItem(row);
            }
        }
    }

    private class EditMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = view.getTable().getSelectedRow();
            if(row == -1) return;
            EditProductPanel panel = new EditProductPanel(row, model);
            int option = JOptionPane.showConfirmDialog(null, panel, "Edit product",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.OK_OPTION) {
                String prodName;
                double prodPrice;
                ArrayList<MenuItem> menuItems;

                try {
                    prodName = panel.getProdName();
                    prodPrice = panel.getProdPrice();
                    menuItems = panel.getSelectedProd();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(model.getMenu().get(row) instanceof BaseProduct) {
                    model.editMenuItem(model.getMenu().get(row), prodName, prodPrice, null);
                } else {
                    if(menuItems.size() == 0) {
                        JOptionPane.showMessageDialog(null, "The composite product must contain minimum one another product.\n",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    model.editMenuItem(model.getMenu().get(row), prodName, prodPrice, menuItems);
                }
                view.updateAll();
            }
        }
    }
}
