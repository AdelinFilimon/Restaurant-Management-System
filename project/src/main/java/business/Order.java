package business;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private static final long serialVersionUID = -7739193239035868455L;
    public int orderId;
    public Date date;
    public int tableNumber;

    public Order(int orderId, Date date, int tableNumber) {
        this.orderId = orderId;
        this.date = date;
        this.tableNumber = tableNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, date, tableNumber);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;

        if(!(o instanceof Order)) return false;

        Order o1 = (Order) o;

        return (orderId == o1.orderId && date.equals(o1.date) && tableNumber == o1.tableNumber);
    }
}
