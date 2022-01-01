package entity.order;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.shipping.DeliveryInfo;

import java.util.List;
import java.util.Random;

import static views.screen.home.HomeScreenHandler.LOGGER;

public class MixedOrder extends Order{

    private DeliveryInfo normalDeliveryInfo;
    private DeliveryInfo rushDeliveryInfo;
    public static final String TYPE = "RUSH_AND_NORMAL";

    public MixedOrder(List<OrderMedia> lstOrderMedia, DeliveryInfo normalDeliveryInfo, DeliveryInfo rushDeliveryInfo){
        this.lstOrderMedia = lstOrderMedia;
        this.normalDeliveryInfo = normalDeliveryInfo;
        this.rushDeliveryInfo = rushDeliveryInfo;
    }

    @Override
    public int getShippingFee(){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * this.getAmount() );
        LOGGER.info("Order Amount: " + this.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }

    @Override
    public DeliveryInfo getDeliveryInfo(){
        return rushDeliveryInfo;
    }

    @Override
    public DeliveryInfo getNormalDeliveryInfo() {
        return normalDeliveryInfo;
    }

    @Override
    public DeliveryInfo getRushDeliveryInfo() {
        return rushDeliveryInfo;
    }
}
