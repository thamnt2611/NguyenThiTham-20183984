package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
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
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(),
                                                   "Not Set",
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }
    
    /**
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public static void validateDeliveryInfo(DeliveryInfo info) throws InterruptedException, IOException{
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
    
    public static boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber == null) return false;
    	if(!phoneNumber.startsWith("0")) return false;
    	if(phoneNumber.strip().length()!=10) return false;
    	if(!phoneNumber.matches("[0-9]+")) return false;

    	return true;
    }
    
    public static boolean validateName(String name) {
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
    
    public static boolean validateAddress(String address) {
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
    public static boolean validateProvince(String province) {
        if(province == null) return false;
        return province_set.contains(province);
    }
    

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public static int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        if(order.getType() == Order.RUSH || order.getType() == Order.RUSH_AND_NORMAL){
            fees += 10000;
        }
        return fees;
    }
}
