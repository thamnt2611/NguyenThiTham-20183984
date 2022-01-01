package controller;

import common.exception.InvalidFormInputException;
import common.exception.NotSatisfiedRushConditionException;
import entity.cart.CartMedia;
import entity.order.MixedOrder;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.order.RushOrder;
import entity.shipping.DeliveryInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PlaceRushOrderController} performs the internal services
 * relating to place rush order process
 *
 * @author thamnt
 */
public class PlaceRushOrderController extends PlaceOrderController {

	/**
	 * get all items that support rush delivery in the order being processed
	 * @returns List\<OrderMedia\>
	 */
	public List<CartMedia> getSupportRushMedia(List<CartMedia> cartMediaList) {
		List<CartMedia> rushMedias = new ArrayList<>();
		for(CartMedia cartMedia : cartMediaList){
			if(cartMedia.getMedia().doSupportRushDelivery()){
				rushMedias.add(cartMedia);
			}
		}
		return rushMedias;
	}

	/**
	 * Create Rush Order for current cart
	 * @param initialDeliveryInfo
	 * @param rushDeliveryInfo
	 * @return
	 */
	public Order createOrder(DeliveryInfo initialDeliveryInfo, DeliveryInfo rushDeliveryInfo){
		List<CartMedia> cartMediaList = this.getListCartMedia();
		List<CartMedia> supportRushMedias = getSupportRushMedia(cartMediaList);
		List<OrderMedia> orderMediaList = new ArrayList<>();
		for(CartMedia cartMedia : cartMediaList){
			if(cartMedia.getMedia().doSupportRushDelivery()){
				OrderMedia o = new OrderMedia(cartMedia, DeliveryInfo.RUSH_TYPE);
				orderMediaList.add(o);
			}
			else{
				OrderMedia o = new OrderMedia(cartMedia, DeliveryInfo.NORMAL_TYPE);
				orderMediaList.add(o);
			}
		}
		Order order;
		if(orderMediaList.size() == supportRushMedias.size()){
			order = new RushOrder(orderMediaList, rushDeliveryInfo);
		}
		else{
			order = new MixedOrder(orderMediaList, initialDeliveryInfo, rushDeliveryInfo);
		}
		return order;
	}

	/**
	 * validate if user enters all required field in form and their inputs are valid
	 *
	 * @param info - the user input got from form
	 * @returns 1 if all fields are filled with valid input
	 * @throws InvalidFormInputException if at least one field in form has invalid input
	 */
	public void validateRushDeliveryForm(DeliveryInfo info){
		String address = info.getAddress();
		String dateInput = info.getExpectedDate();
		if(!validateAddress(address)){
			throw new InvalidFormInputException("Please enter valid address");
		}
		if(!validateDateInput(dateInput)){
			throw new InvalidFormInputException("Please enter valid date");
		}
	}

	/**
	 * {@code PlaceRushOrderController.validateDateInput} only returns true
	 * if date string follow the format of JavaFx DatePicker value ("uuuu-MM-dd")
	 * and the input date must be after the date when user places order
	 * @param dateInput - user input got from form
	 * @return
	 */
	public boolean validateDateInput(String dateInput){
		if(dateInput == null) return false;
		DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		LocalDate date;
		try{
			date = LocalDate.parse(dateInput, dateFormater);
		}
		catch (DateTimeParseException e){
			return false;
		}
		if(date.isBefore(LocalDate.now())) return false;

		return true;
	}
}
