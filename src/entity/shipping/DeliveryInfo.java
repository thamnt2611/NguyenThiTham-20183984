package entity.shipping;

import java.util.Date;

public class DeliveryInfo {

	public static final String NORMAL_TYPE = "Normal";
	public static final String RUSH_TYPE = "Rush";

	private String receiverFullName;

	private String phoneNumber;

	private String province;

	private String address;

	private String deliveryInstruction;

	private String type;

	private String expectedDeliveryDate;

	public DeliveryInfo DeliveryInfo() {
		return null;
	}

	public DeliveryInfo(String receiverFullName, String phoneNumber, String province, String address, String deliveryInstruction) {
		this.receiverFullName = receiverFullName;
		this.phoneNumber = phoneNumber;
		this.province = province;
		this.address = address;
		this.deliveryInstruction = deliveryInstruction;
	}

	public DeliveryInfo(){

	}

	public void save() {

	}

	public String getReceiverFullName() {
		return receiverFullName;
	}

	public void setReceiverFullName(String receiverFullName) {
		this.receiverFullName = receiverFullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeliveryInstruction() {
		return deliveryInstruction;
	}

	public void setDeliveryInstruction(String deliveryInstruction) {
		this.deliveryInstruction = deliveryInstruction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(String expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}
}
