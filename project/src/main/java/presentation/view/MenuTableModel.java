package presentation.view;

import business.CompositeProduct;
import business.MenuItem;
import business.Restaurant;

import javax.swing.table.AbstractTableModel;

public class MenuTableModel extends AbstractTableModel {

    private final Restaurant model;

    public MenuTableModel(Restaurant model) {
        this.model = model;
    }

    @Override
    public int getRowCount() {
        return model.getMenu().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MenuItem menuItem = model.getMenu().get(rowIndex);
        switch (columnIndex) {
            case 0 : return menuItem.itemName;
            case 1 : return menuItem.computePrice();
            case 2 : if(menuItem instanceof CompositeProduct) {
                CompositeProduct cp = (CompositeProduct) menuItem;
                StringBuilder string = new StringBuilder();
                for(MenuItem submenu : cp.getComposition()) {
                    string.append(submenu.itemName).append(", ");
                }
                string.delete(string.length() - 2, string.length() - 1);
                return string;
            }
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
                return String.class;
            case 1: return Double.class;
            default: return Object.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Product name";
            case 1: return "Price";
            case 2: return "Composed of";
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        MenuItem item = model.getMenu().get(row);
        switch (col) {
            case 0:
                if(validateName((String) value)) model.editMenuItem(item, value.toString(), -1, null);
            break;
            case 1:
                if(validatePrice((double)value))model.editMenuItem(item, null, (double) value, null);
            break;
            default: break;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if(col == 2) return false;
        return col != 1 || !(model.getMenu().get(row) instanceof CompositeProduct);
    }

    private boolean validateName(String name) {
        return name != null && name.matches("[a-zA-Z ]+") && name.length() >= 3 && !model.productExist(name);
    }

    private boolean validatePrice(double price) {
        return !(price <= 0);
    }
}
