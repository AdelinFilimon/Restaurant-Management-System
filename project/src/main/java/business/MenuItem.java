package business;

import java.io.Serializable;
import java.util.Objects;

public abstract class MenuItem implements Serializable {
    private static final long serialVersionUID = 2193317803898401363L;
    public String itemName;
    protected double price;

    public MenuItem(double price, String itemName) {
        this.price = price;
        this.itemName = itemName;
    }

    public abstract double computePrice();

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, price);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof MenuItem)) return false;
        MenuItem o1 = (MenuItem) o;
        return (itemName.equals(o1.itemName) && price == o1.price);
    }
}
