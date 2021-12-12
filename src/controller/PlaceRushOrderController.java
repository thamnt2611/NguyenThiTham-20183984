package controller;

import common.exception.InvalidFormInputException;
import common.exception.NotSatisfiedRushConditionException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
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
public class PlaceRushOrderController extends BaseController {

	/**
	 * get all items that support rush delivery in the order being processed
	 * @returns List\<OrderMedia\>
	 */
	public static List<OrderMedia> getSupportRushMedias(Order order) {
		List<OrderMedia> rushMedias = new ArrayList<>();
		for(OrderMedia orderMedia : order.getlstOrderMedia()){
			if(orderMedia.getMedia().doSupportRushDelivery()){
				rushMedias.add(orderMedia);
			}
		}
		return rushMedias;
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
		String dateInput = info.getExpectedDeliveryDate();
		if(!PlaceOrderController.validateAddress(address)){
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

	/**
	 * check if the order being processed satisfies rush condition
	 * @param order - the order being processed
	 * @returns boolean - true if order satisfies and false for the other case
	 * @param order
	 * @throw NotSatisfiedRushConditionException
	 */
	public static void checkRushOrderCondition(Order order) {
		DeliveryInfo initialDeliveryInfo = order.getNormalDeliveryInfo();
		if(!checkRushDeliveryInfoCondition(initialDeliveryInfo)){
			throw new NotSatisfiedRushConditionException("We do not support rush delivery to this delivery address");
		}
		if(!doesContainRushMedia(order)){
			throw new NotSatisfiedRushConditionException("Your order does not contain any item supporting rush delivery");
		}
	}

	/**
	 * check if order has any item that supports rush delivery
	 * @param order
	 * @return
	 */
	public static boolean doesContainRushMedia(Order order){
		List<OrderMedia> orderMediaList = order.getlstOrderMedia();
		for(OrderMedia o : orderMediaList){
			if(o.getMedia().doSupportRushDelivery()) return true;
		}
		return false;
	}

	/**
	 * Check if delivery information satisfies rush condition, i.e province is "Hà Nội"
	 * @param info
	 * @return
	 */
	public static boolean checkRushDeliveryInfoCondition(DeliveryInfo info) {
		if(info.getProvince().equals("Hà Nội")) return true;
		return false;
	}
}
