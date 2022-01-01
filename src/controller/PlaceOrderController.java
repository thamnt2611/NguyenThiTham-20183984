package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.exception.NotSatisfiedRushConditionException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.*;
import entity.shipping.DeliveryInfo;
import utils.Configs;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());
    private static HashSet<String> province_set = new HashSet<>(Arrays.asList(Configs.PROVINCES));

    /**
     * This method checks the availability of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void checkCartAvailability() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    public Invoice createInvoiceForOrder(Order order){
        return new Invoice(order);
    }

    /**
     * check if the order being processed satisfies rush condition
     * @param  - the order being processed
     * @returns boolean - true if order satisfies and false for the other case
     * @param
     * @throw NotSatisfiedRushConditionException
     */
    public void checkRushOrderCondition(List<CartMedia> cartMediaList, DeliveryInfo initialDeliveryInfo) {
        if(!checkRushDeliveryInfoCondition(initialDeliveryInfo)){
            throw new NotSatisfiedRushConditionException("We do not support rush delivery to this delivery address");
        }
        if(!doesContainRushMedia(cartMediaList)){
            throw new NotSatisfiedRushConditionException("Your order does not contain any item supporting rush delivery");
        }
    }

    /**
     * check if order has any item that supports rush delivery
     * @param cartMediaList
     * @return
     */
    protected boolean doesContainRushMedia(List<CartMedia> cartMediaList){
        for(CartMedia o : cartMediaList){
            if(o.getMedia().doSupportRushDelivery()) return true;
        }
        return false;
    }

    /**
     * Check if delivery information satisfies rush condition, i.e province is "Hà Nội"
     * @param info
     * @return
     */
    protected boolean checkRushDeliveryInfoCondition(DeliveryInfo info) {
        if(info.getProvince().equals("Hà Nội")) return true;
        return false;
    }

    /**
     * Create Normal Order for current cart and delivery info
     * @param initialDeliveryInfo
     * @return
     */
    public Order createOrder(DeliveryInfo initialDeliveryInfo){
        List<CartMedia> cartMediaList = this.getListCartMedia();
        List<OrderMedia> orderMediaList = new ArrayList<>();
        for(CartMedia cartMedia : cartMediaList){
            OrderMedia o = new OrderMedia(cartMedia, DeliveryInfo.NORMAL_TYPE);
            orderMediaList.add(o);
        }
        Order order;
        order = new NormalOrder(orderMediaList, initialDeliveryInfo);
        return order;
    }
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(DeliveryInfo info){
        StringBuilder message = new StringBuilder();
        message.append("Please enter valid value for ");
        boolean isValid = true;
        if(!validatePhoneNumber(info.getPhoneNumber())){
            message.append("-- phone number");
            isValid = false;
        }
        if(!validateAddress(info.getAddress())){
            message.append("-- address");
            isValid = false;
        }
        if(!validateName(info.getReceiverFullName())){
            message.append("-- receiver name");
            isValid = false;
        }
        if(!validateProvince(info.getProvince())){
            message.append("-- province");
            isValid = false;
        }
        if(!isValid) throw new InvalidDeliveryInfoException(message.toString());
    }
    
    protected boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber == null) return false;
    	if(!phoneNumber.startsWith("0")) return false;
    	if(phoneNumber.strip().length()!=10) return false;
    	if(!phoneNumber.matches("[0-9]+")) return false;

    	return true;
    }
    
    protected boolean validateName(String name) {
        if(name == null) return false;
        name = Normalizer.normalize(name, Normalizer.Form.NFKD).strip();
        name = name.replaceAll("\\p{M}", "").replaceAll("đ", "d").replaceAll("Đ", "D");
        Pattern empty_pat = Pattern.compile("([\\s]+)");
        Matcher empty_match = empty_pat.matcher(name);
        if(empty_match.matches()) return false;
        Pattern words_pat = Pattern.compile("[A-Za-z\\s]+");
        Matcher words_match= words_pat.matcher(name);
        if(words_match.matches()) return true;

        return false;
    }
    
    protected boolean validateAddress(String address) {
        if(address == null) return false;
        address = Normalizer.normalize(address, Normalizer.Form.NFKD);
        address = address.replaceAll("\\p{M}", "").replaceAll("đ", "d").replaceAll("Đ", "D");
        Pattern empty_pat = Pattern.compile("([\\s]+)");
        Matcher empty_match = empty_pat.matcher(address);
        if(empty_match.matches()){
            return false;
        }
        Pattern words_pat = Pattern.compile("[/A-Za-z0-9,\\.;\\s]+");
        Matcher words_match= words_pat.matcher(address);
        if(words_match.matches())
            return true;
        return false;
    }

    /**
     * Make sure that province in message is got from the predefined province list
     * @param province
     * @return
     */
    protected boolean validateProvince(String province) {
        if(province == null) return false;
        return province_set.contains(province);
    }

    public int calculateShippingFee(Order order){
        return order.getShippingFee();
    }
}
