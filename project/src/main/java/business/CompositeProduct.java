package business;


import java.util.ArrayList;

public class CompositeProduct extends MenuItem {

    private static final long serialVersionUID = 876912265430990661L;
    private ArrayList<MenuItem> composition;
    public CompositeProduct(String itemName, ArrayList<MenuItem> composition) {
        super(0, itemName);
        this.composition = composition;
    }

    public void setComposition(ArrayList<MenuItem> composition) {
        this.composition = composition;
    }

    public ArrayList<MenuItem> getComposition() {
        return composition;
    }

    @Override
    public double computePrice() {
        price = 0;
        for(MenuItem menuItem : composition) {
            price += menuItem.computePrice();
        }
        return price;
    }
}
