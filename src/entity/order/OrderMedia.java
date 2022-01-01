package entity.order;

import entity.cart.CartMedia;
import entity.db.AIMSDB;
import entity.media.Media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMedia {

    private Media media;

    private int quantity;

    private String deliveryType;

    private int price;

    public OrderMedia(Media media, int quantity, String deliveryType, int price) {
        this.media = media;
        this.quantity = quantity;
        this.deliveryType = deliveryType;
        this.price = price;
    }

    public OrderMedia(CartMedia cartMedia, String deliveryType) {
        this.media = cartMedia.getMedia();
        this.quantity = cartMedia.getQuantity();
        this.deliveryType = deliveryType;
        this.price = cartMedia.getPrice();
    }
    
    @Override
    public String toString() {
        return "{" +
            "  media='" + media + "'" +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            ", deliveryType='" + deliveryType + "'" +
            "}";
    }

    public void saveIntoOrder(Integer orderId){
        String sql = "INSERT INTO OrderMedia(orderID, mediaID, price, quantity) VALUES(?, ?, ?, ?)";
        Connection conn = AIMSDB.getConnection();
        PreparedStatement prestat = null;
        try {
            prestat = conn.prepareStatement(sql);
            prestat.setInt(1, orderId);
            prestat.setInt(2, this.media.getId());
            prestat.setInt(3, this.price);
            prestat.setInt(4, this.quantity);
            boolean isSuccess = prestat.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
