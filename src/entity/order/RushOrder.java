package entity.order;

import entity.shipping.DeliveryInfo;

import java.util.List;
import java.util.Random;

import static views.screen.home.HomeScreenHandler.LOGGER;

public class RushOrder extends Order{
    private DeliveryInfo rushDeliveryInfo;
    public static final String TYPE = "ONLY_RUSH";

    public RushOrder(List<OrderMedia> lstOrderMedia, DeliveryInfo rushDeliveryInfo){
        this.lstOrderMedia = lstOrderMedia;
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
    public DeliveryInfo getRushDeliveryInfo() {
        return rushDeliveryInfo;
    }
}
