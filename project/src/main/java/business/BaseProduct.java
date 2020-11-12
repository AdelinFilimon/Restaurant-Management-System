package business;

public class BaseProduct extends MenuItem {
    private static final long serialVersionUID = 6663969389437924617L;

    public BaseProduct(double price, String itemName) {
        super(price, itemName);
    }

    @Override
    public double computePrice() {
        return price;
    }
}
