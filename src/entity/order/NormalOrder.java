package entity.order;

import entity.shipping.DeliveryInfo;

import java.util.List;
import java.util.Random;

import static views.screen.home.HomeScreenHandler.LOGGER;

public class NormalOrder extends Order{
    private DeliveryInfo normalDeliveryInfo;
    public static final String TYPE = "ONLY_NORMAL";

    public NormalOrder(List<OrderMedia> lstOrderMedia, DeliveryInfo normalDeliveryInfo){
        this.lstOrderMedia = lstOrderMedia;
        this.normalDeliveryInfo = normalDeliveryInfo;
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
        return normalDeliveryInfo;
    }

    @Override
    public DeliveryInfo getNormalDeliveryInfo() {
        return normalDeliveryInfo;
    }
}
