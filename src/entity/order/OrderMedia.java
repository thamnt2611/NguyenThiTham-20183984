package entity.order;

import entity.media.Media;

public class OrderMedia {

    private Media media;

    private int quantity;

    private String deliveryType;

    private long price;

    public OrderMedia(Media media, int quantity, String deliveryType, long price) {
        this.media = media;
        this.quantity = quantity;
        this.deliveryType = deliveryType;
        this.price = price;
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

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
