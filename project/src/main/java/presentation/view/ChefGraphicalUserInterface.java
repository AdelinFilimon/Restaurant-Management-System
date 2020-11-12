package presentation.view;

import business.CompositeProduct;
import business.MenuItem;
import business.Order;
import business.Restaurant;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ChefGraphicalUserInterface extends JPanel implements Observer {

    private final Restaurant model;
    private final JPanel labelsPanel;
    private final ArrayList<JLabel> labels;

    public ChefGraphicalUserInterface(Restaurant model) {
        this.model = model;
        setLayout(new MigLayout());

        labelsPanel = new JPanel();
        labelsPanel.setLayout(new MigLayout("wrap 1"));
        JScrollPane scrollPane = new JScrollPane(labelsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        labels = new ArrayList<>();
        add(scrollPane, "gaptop: 10%, gapleft: 30%");
    }

    @Override
    public void update(Observable o, Object arg) {
        for(JLabel label : labels) {
            label.setForeground(Color.DARK_GRAY);
        }

        JLabel newOrderLabel = new JLabel();
        newOrderLabel.setFont(new Font("Serif", Font.BOLD, 18));
        Order order = (Order) arg;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE! OrderID: ").append(order.orderId).append("\n").append(" Prepare: ");
        for(MenuItem menuItem : model.getOrders().get(order)) {
            if(menuItem instanceof CompositeProduct) {
                stringBuilder.append(menuItem.itemName).append(", ");
            }
        }
        stringBuilder.delete(stringBuilder.length() -2, stringBuilder.length() -1);
        newOrderLabel.setForeground(Color.RED);
        newOrderLabel.setText(stringBuilder.toString());
        labels.add(newOrderLabel);
        labelsPanel.add(newOrderLabel);
    }
}
