package presentation.controller;

import business.Restaurant;
import com.formdev.flatlaf.FlatIntelliJLaf;
import data.RestaurantSerializator;
import presentation.view.AdministratorGraphicalUserInterface;
import presentation.view.ChefGraphicalUserInterface;
import presentation.view.WaiterGraphicalUserInterface;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ApplicationFrame extends JFrame {
    private final Restaurant restaurant;

    public ApplicationFrame(Restaurant restaurant) {
        FlatIntelliJLaf.install();
        this.restaurant = restaurant;
        AdministratorGraphicalUserInterface gui1 = new AdministratorGraphicalUserInterface(restaurant);
        WaiterGraphicalUserInterface gui2 = new WaiterGraphicalUserInterface(restaurant);
        ChefGraphicalUserInterface gui3 = new ChefGraphicalUserInterface(restaurant);

        restaurant.addObserver(gui3);

        new AdministratorController(restaurant, gui1);
        new WaiterController(restaurant, gui2);

        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Administrator", gui1);
        pane.addTab("Waiter", gui2);
        pane.addTab("Chef", gui3);
        add(pane);
        setTitle("Restaurant Management System");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setWindowClosing();

    }

    private void setWindowClosing() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int option = JOptionPane.showConfirmDialog(null, "Save the changes?",
                        "Save", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.OK_OPTION) {
                    RestaurantSerializator.getInstance().serialize("restaurant.ser", restaurant);
                }
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
    }

    public static void main(String[] args) {


        Restaurant restaurant;
        if(args.length == 0) {
            restaurant = new Restaurant();
        } else {
            restaurant = RestaurantSerializator.getInstance().deserialize(args[0]);
        }

        new ApplicationFrame(restaurant);

    }
}


