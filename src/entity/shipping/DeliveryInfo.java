package entity.shipping;

import entity.db.AIMSDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeliveryInfo {

	public static final String NORMAL_TYPE = "Normal";
	public static final String RUSH_TYPE = "Rush";

	private String receiverFullName;

	private String phoneNumber;

	private String province;

	private String address;

	private String deliveryInstruction;

	private String type;

	private String expectedDate = "";

	public DeliveryInfo DeliveryInfo() {
		return null;
	}

	public DeliveryInfo(String receiverFullName, String phoneNumber, String province, String address,
						String deliveryInstruction, String type) {
		this.receiverFullName = receiverFullName;
		this.phoneNumber = phoneNumber;
		this.province = province;
		this.address = address;
		this.deliveryInstruction = deliveryInstruction;
		this.type = type;
	}

	public DeliveryInfo(){

	}

	public String getReceiverFullName() {
		return receiverFullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getProvince() {
		return province;
	}

	public String getAddress() {
		return address;
	}

	public String getDeliveryInstruction() {
		return deliveryInstruction;
	}

	public String getType() {
		return type;
	}

	public String getExpectedDate(){
		return expectedDate;
	}

	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}

	public void setProvince(String province) {
	}
}
