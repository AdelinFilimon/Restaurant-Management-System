package presentation.view;


import business.MenuItem;
import business.Order;
import business.Restaurant;
import javax.swing.table.AbstractTableModel;
import java.util.Date;

public class OrderTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 3773259104679655882L;
    private final Restaurant model;

    public OrderTableModel(Restaurant restaurant) {
        model = restaurant;
    }

    @Override
    public int getRowCount() {
        return model.getOrders().size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = model.getOrderById(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.orderId;
            case 1:
                return order.date;
            case 2:
                return order.tableNumber;
            case 3:
                StringBuilder res = new StringBuilder();
                for(MenuItem item : model.getOrders().get(order)) {
                    res.append(item.itemName).append(", ");
                }
                res.delete(res.length() - 2, res.length() - 1);
                return res;
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
                return Integer.class;
            case 1: return Date.class;
            case 3: return String.class;
            default: return Object.class;
        }
    }


    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Order ID";
            case 1: return "Order Date";
            case 2: return "Table number";
            case 3: return "Products";
            default: return null;
        }
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
