package entity.order;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.db.AIMSDB;
import entity.shipping.DeliveryInfo;
import utils.Configs;

public abstract class Order {
    protected List<OrderMedia> lstOrderMedia;


    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List<OrderMedia> getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List<OrderMedia> lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }

    public int getShippingFee(){
        return 0;
    }

    public DeliveryInfo getDeliveryInfo(){
        return null;
    }

    public DeliveryInfo getNormalDeliveryInfo() {
        return null;
    }

    public DeliveryInfo getRushDeliveryInfo() {
        return null;
    }
}
