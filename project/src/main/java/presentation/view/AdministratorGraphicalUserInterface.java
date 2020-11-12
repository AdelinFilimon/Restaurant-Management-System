package presentation.view;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdministratorGraphicalUserInterface extends JPanel {
    private final JButton addMenuItemBtn;
    private final JButton editMenuItemsBtn;
    private final JButton deleteMenuItemsBtn;
    private final MenuTableModel tableModel;
    private final JTable menuItemsTable;

    public AdministratorGraphicalUserInterface(Restaurant model) {
        addMenuItemBtn = new JButton("ADD PRODUCT");
        editMenuItemsBtn = new JButton("EDIT PRODUCT");
        deleteMenuItemsBtn = new JButton("DELETE PRODUCT");
        this.tableModel = new MenuTableModel(model);
        menuItemsTable = new JTable(tableModel);
        menuItemsTable.getTableHeader().setReorderingAllowed(false);
        menuItemsTable.setRowHeight(30);

        menuItemsTable.getColumnModel().getColumn(0).setMinWidth(250);
        menuItemsTable.getColumnModel().getColumn(1).setMinWidth(122);
        menuItemsTable.getColumnModel().getColumn(2).setMinWidth(400);

        menuItemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tablePane = new JScrollPane(menuItemsTable);
        tablePane.setMinimumSize(new Dimension(780,700));
        setLayout(new MigLayout());
        add(tablePane, "span, wrap");
        add(addMenuItemBtn, "split 3, gapleft 150, gaptop 25");
        add(editMenuItemsBtn);
        add(deleteMenuItemsBtn);
    }

    public void setAddMenuListener(ActionListener addMenuListener) {
        addMenuItemBtn.addActionListener(addMenuListener);
    }

    public void setEditMenuListener(ActionListener editMenuListener) {
        editMenuItemsBtn.addActionListener(editMenuListener);
    }

    public void setDeleteMenuListener(ActionListener deleteMenuListener) {
        deleteMenuItemsBtn.addActionListener(deleteMenuListener);
    }

    public void updateAddMenuItem(int row) {
        tableModel.fireTableRowsInserted(row, row);
    }

    public void updateAll() {
        tableModel.fireTableDataChanged();
    }

    public JTable getTable() {
        return menuItemsTable;
    }

}
