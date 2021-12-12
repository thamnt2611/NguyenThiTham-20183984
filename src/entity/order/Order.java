package entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.shipping.DeliveryInfo;
import utils.Configs;

public class Order {
    
    private int shippingFees;
    private List<OrderMedia> lstOrderMedia;
    private DeliveryInfo normalDeliveryInfo;
    private DeliveryInfo rushDeliveryInfo;
    private String type;

    public static final String RUSH = "ONLY RUSH";
    public static final String NORMAL = "ONLY NORMAL";
    public static final String RUSH_AND_NORMAL = "RUSH_AND_NORMAL";

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List<OrderMedia> lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

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

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public DeliveryInfo getNormalDeliveryInfo() {
        return normalDeliveryInfo;
    }

    public void setNormalDeliveryInfo(DeliveryInfo normalDeliveryInfo) {
        this.normalDeliveryInfo = normalDeliveryInfo;
    }

    public DeliveryInfo getRushDeliveryInfo() {
        return rushDeliveryInfo;
    }

    public void setRushDeliveryInfo(DeliveryInfo rushDeliveryInfo) {
        this.rushDeliveryInfo = rushDeliveryInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }

}
