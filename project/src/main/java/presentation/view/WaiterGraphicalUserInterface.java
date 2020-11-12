package presentation.view;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WaiterGraphicalUserInterface extends JPanel {
    private final JButton addOrderBtn;
    private final JButton computePriceBtn;
    private final JButton generateBillBtn;
    private final JTable ordersTable;
    private final OrderTableModel tableModel;

    public WaiterGraphicalUserInterface(Restaurant model) {
        addOrderBtn = new JButton("ADD ORDER");
        computePriceBtn = new JButton("COMPUTE PRICE");
        generateBillBtn = new JButton("GENERATE BILL");

        this.tableModel = new OrderTableModel(model);
        ordersTable = new JTable(tableModel);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTable.setRowHeight(30);


        ordersTable.getColumnModel().getColumn(0).setMinWidth(100);
        ordersTable.getColumnModel().getColumn(1).setMinWidth(150);
        ordersTable.getColumnModel().getColumn(2).setMinWidth(150);
        ordersTable.getColumnModel().getColumn(3).setMinWidth(370);

        ordersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane tablePane = new JScrollPane(ordersTable);
        tablePane.setMinimumSize(new Dimension(780,700));

        setLayout(new MigLayout());
        add(tablePane, "span, wrap");
        add(addOrderBtn, "split 3, gapleft 180, gaptop 25");
        add(computePriceBtn);
        add(generateBillBtn);
    }

    public void setAddOrderListener(ActionListener addOrderListener) {
        addOrderBtn.addActionListener(addOrderListener);
    }

    public void setComputePriceListener(ActionListener computePriceListener) {
        computePriceBtn.addActionListener(computePriceListener);
    }

    public void setGenerateBillListener(ActionListener generateBillListener) {
        generateBillBtn.addActionListener(generateBillListener);
    }

    public void updateAddOrder(int row) {
        tableModel.fireTableRowsInserted(row, row);
    }


    public JTable getTable() {
        return ordersTable;
    }


}
