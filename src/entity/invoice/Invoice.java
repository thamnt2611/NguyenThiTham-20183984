package entity.invoice;

import entity.order.Order;
import utils.Utils;

public class Invoice {

    private Order order;
    private int subtotal;
    private int total;
    private int shippingFee;

    public Invoice(Order order){
        this.order = order;
        this.subtotal = order.getAmount();
        this.shippingFee = order.getShippingFee();
        this.total = this.subtotal + this.shippingFee;
    }

    public Order getOrder() {
        return order;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public int getTotal() {
        return total;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void saveInvoice(){
        
    }
}
