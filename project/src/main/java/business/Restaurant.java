package business;


import data.FileWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Restaurant class defines methods used for basic processing of orders and menu items
 */
public class Restaurant extends Observable implements IRestaurantProcessing, Serializable {
    private static final long serialVersionUID = -8891381204293894310L;

    /**
     * The map stores the orders processed by waiter
     */
    private final HashMap<Order, Collection<MenuItem>> orders;
    /**
     * The list with menu items of the restaurant. This list is processed by administrator
     */
    private final ArrayList<MenuItem> menu;

    /**
     * Counter used for creating names for generated bills
     */
    private int billCount;

    /**
     * Constructor used for initializing a Restaurant object
     * @pre The orders and menu are not null
     * @param orders The orders of the restaurant
     * @param menu The menu of the restaurant
     */
    public Restaurant(HashMap<Order,Collection<MenuItem>> orders, ArrayList<MenuItem> menu) {
        assert orders != null && menu != null;
        this.orders = orders;
        this.menu = menu;
        billCount = 0;
    }

    /**
     * Constructor used for creating a new object with no menu items and no orders
     */
    public Restaurant() {
        this(new HashMap<>(), new ArrayList<>());
    }

    /**
     * @return Returns an ArrayList of MenuItems representing the menu of the restaurant
     */
    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    /**
     * @return Returns an Hashmap with key Order and value a collection of MenuItems representing the orders of the restaurant
     */
    public HashMap<Order, Collection<MenuItem>> getOrders() {
        return orders;
    }

    /**
     * Method used for creating and inserting a new MenuItem to the menu
     * @param itemName The name of the item
     * @param price The price of the item
     * @param composition The composition of the item (in case it is a CompositeProduct)
     * @return Returns the created MenuItem
     * @pre Checks if itemName is not null and price is greater than 0
     * @post Checks if size of the menu was incremented
     * @inv Checks if the method maintains the class invariant (see isWellFormed method)
     * */
    @Override
    public MenuItem createMenuItem(String itemName, double price, ArrayList<MenuItem> composition) {
        assert isWellFormed();
        assert itemName != null && price > 0;
        int previousSize = menu.size();

        MenuItem menuItem;
        if(composition == null) menuItem = new BaseProduct(price, itemName);
        else menuItem = new CompositeProduct(itemName, composition);
        menu.add(menuItem);

        assert menu.size() == previousSize + 1;
        assert isWellFormed();
        return menuItem;
    }

    /**
     * Method used for removing a MenuItem from the menu of the restaurant
     * @param menuItem The MenuItem object to be deleted
     * @pre Checks if the given MenuItem is in the menu
     * @post Checks if size of the menu was decremented
     * @inv Checks if the method maintains the class invariant (see isWellFormed method)
     */
    @Override
    public void deleteMenuItem(MenuItem menuItem) {
        assert isWellFormed();
        assert menu.size() > 0 && menuItem != null && menu.contains(menuItem);
        int previousSize = menu.size();

        menu.remove(menuItem);
        assert menu.size() == previousSize - 1;
        assert isWellFormed();
    }

    /**
     * Method used for editing an existing MenuItem from the Menu.
     * @param menuItem The MenuItem to be changed
     * @param itemName The new name of the MenuItem, use null value for not changing it
     * @param price The new price of the MenuItem, use -1 value for not changing it
     * @param composition The new composition of the MenuItem, use null for not changing it
     * @pre Checks if the given MenuItem is in the menu
     * @pre Checks if price is greater than 0, if it is not -1
     * @inv Checks if the method maintains the class invariant (see isWellFormed method)
     */
    @Override
    public void editMenuItem(MenuItem menuItem, String itemName, double price, ArrayList<MenuItem> composition) {
        assert isWellFormed();
        assert menuItem != null && menu.contains(menuItem);
        assert price > 0 || price == -1;

        if(itemName != null) menuItem.setItemName(itemName);
        if(price != -1) menuItem.setPrice(price);
        if(composition != null && menuItem instanceof CompositeProduct) {
            ((CompositeProduct) menuItem).setComposition(composition);
        }
        assert isWellFormed();
    }

    /**
     * Method used for creating and inserting a new Order to the orders of the restaurant. This method notifies the Chef
     * in case there are CompositeProducts in the list
     * @param orderId The id of the Order
     * @param date The date of the Order
     * @param tableNumber The number of the table of the Order
     * @param items The list of ordered items
     * @return Returns the created order
     * @pre Checks if id is greater or equal than 0
     * @pre Checks if date and items are not null
     * @pre Checks if table number and size of the item`s list is greater than 0
     * @post Checks if size of the orders was incremented
     * @inv Checks if the method maintains the class invariant (see isWellFormed method)
     */
    @Override
    public Order createOrder(int orderId, Date date, int tableNumber, List<MenuItem> items) {
        assert isWellFormed();
        assert orderId >= 0 && date != null && tableNumber >= 0 && items != null && items.size() > 0;
        int previousSize = orders.size();

        Order order = new Order(orderId, date, tableNumber);
        orders.put(order, items);
        for(MenuItem menuItem : items) {
            if(menuItem instanceof CompositeProduct) {
                setChanged();
                notifyObservers(order);
                break;
            }
        }

        assert orders.size() == previousSize + 1;
        assert isWellFormed();
        return order;
    }

    /**
     * Method used for computing the total price for an Order
     * @param order The order used
     * @return Returns a double value representing the total price
     * @pre Checks if the given order is in the orders list
     * @inv Checks if the method maintains the class invariant (see isWellFormed method)
     */
    @Override
    public double computePrice(Order order) {
        assert isWellFormed();
        assert order != null && orders.containsKey(order);

        Collection<MenuItem> items = orders.get(order);

        double price = 0;
        for(MenuItem menuItem : items) {
            price += menuItem.computePrice();
        }

        assert isWellFormed();
        return price;
    }

    /**
     * Method used for generating bills for an order
     * @param order The used order
     * @throws IOException Throws IOException in case something happened with the IO connection
     * @pre Checks if the given order is in the orders list
     */
    @Override
    public void generateBill(Order order) throws IOException {
        assert order != null && orders.containsKey(order);

        Collection<MenuItem> items = orders.get(order);
        String filename = "bill" + billCount + ".txt";
        FileWriter.getInstance().writeBill(filename, order, items, computePrice(order));
        billCount++;
    }


    /**
     * Method used for checking if a MenuItem with the specified name already exists in the menu
     * @param itemName The name of the MenuItem to be checked
     * @return Returns true if the MenuItem with the specified name is in the menu, otherwise false
     * @pre Checks if the given name is not null
     */
    public boolean productExist(String itemName) {
        assert itemName != null;
        for(MenuItem menuItem : menu) {
            if(menuItem.itemName.equals(itemName)) return true;
        }
        return false;
    }

    /**
     * Method used for querying an order from the orders list based on the order id
     * @param id The id of the order to be searched
     * @return Returns the founded order, null if the order was not found
     * @pre Checks if id is greater than 0
     */
    public Order getOrderById(int id) {
        assert id >= 0;
        for(Order order : orders.keySet()) {
            if(id == order.orderId) return order;
        }
        return null;
    }

    /**
     * Method used for querying a MenuItem from the menu based on the MenuItem`s name
     * @param itemName The name of the MenuItem to be searched
     * @return Returns the founded MenuItem, null if the MenuItem was not found
     * @pre Checks if the MenuItem`s name is not null
     */
    public MenuItem getMenuItemByName(String itemName) {
        assert itemName != null;
        for(MenuItem menuItem : menu) {
            if(menuItem.itemName.equals(itemName)) return menuItem;
        }
        return null;
    }

    /**
     * Method used for checking the invariant of the class
     * @return Returns true if the invariant of the class is maintained, false otherwise
     */
    private boolean isWellFormed() {
        for(Order order : orders.keySet()) {
            if(order == null || orders.get(order) == null || orders.get(order).size() == 0) return false;
        }
        for(MenuItem item : menu) {
            if(item == null || item.price <= 0) return false;
            if(item instanceof  CompositeProduct) {
                CompositeProduct compositeItem = (CompositeProduct) item;
                if(compositeItem.getComposition() == null || compositeItem.getComposition().size() < 1) return false;
                double checkPrice = 0;
                for(MenuItem subItem : compositeItem.getComposition()) {
                    checkPrice += subItem.computePrice();
                }
                if(checkPrice != compositeItem.computePrice()) return false;
            }
        }
        return true;
    }
}
