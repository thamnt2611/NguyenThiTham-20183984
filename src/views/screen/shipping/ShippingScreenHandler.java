package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.jdi.ObjectReference;
import common.exception.NotSatisfiedRushConditionException;
import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.shipping.DeliveryInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

	@FXML
	private Label screenTitle;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField instructions;

	@FXML
	private ComboBox<String> province;

	@FXML
	private ToggleGroup deliveryTypeGroup;

	@FXML
	private RadioButton normalDeliveryRadioBtn;

	@FXML
	private RadioButton rushDeliveryRadioBtn;


	private Order order;

	public ShippingScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath);
		this.order = order;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
		normalDeliveryRadioBtn.setSelected(true);
	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {
		// add info to messages
		DeliveryInfo messages = new DeliveryInfo(
				name.getText(),
				phone.getText(),
				province.getValue(),
				address.getText(),
				instructions.getText());

		RadioButton selectedBtn = (RadioButton) deliveryTypeGroup.getSelectedToggle();
		order.setNormalDeliveryInfo(messages);
		if(selectedBtn.getText().equals(new String("Normal"))){
			try{
				PlaceOrderController.validateDeliveryInfo(messages);
				messages.setType(DeliveryInfo.NORMAL_TYPE);
				order.setType(Order.NORMAL);
				// calculate shipping fees
				int shippingFees = getBController().calculateShippingFee(order);
				order.setShippingFees(shippingFees);
				// create invoice screen
				Invoice invoice = getBController().createInvoice(order);
				showInvoiceScreen(invoice);
			}catch (InvalidDeliveryInfoException e){
				showDialog(e.getMessage());
			}
		}
		else{
			// create rush delivery screen
			try {
				PlaceRushOrderController.checkRushOrderCondition(order);
				messages.setType(DeliveryInfo.RUSH_TYPE);
				List<OrderMedia> rushMedias = PlaceRushOrderController.getSupportRushMedias(order);
				if(rushMedias.size() == order.getlstOrderMedia().size()){
					order.setType(Order.RUSH);
				}
				else{
					order.setType(Order.RUSH_AND_NORMAL);
				}
				Invoice invoice = getBController().createInvoice(order);
				showRushDeliveryForm(invoice);
			}
			catch (NotSatisfiedRushConditionException e){
				showDialog(e.getMessage());
			}
		}
	}

	private void showRushDeliveryForm(Invoice invoice) throws IOException {
		BaseScreenHandler RushDeliveryFormHandler = new RushDeliveryFormHandler(this.stage, Configs.RUSH_DELIVERY_FORM_PATH, invoice);
		RushDeliveryFormHandler.setPreviousScreen(this);
		RushDeliveryFormHandler.setHomeScreenHandler(homeScreenHandler);
		RushDeliveryFormHandler.setScreenTitle("Rush Delivery Screen");
		RushDeliveryFormHandler.setBController(new PlaceRushOrderController());
		RushDeliveryFormHandler.show();
	}

	private void showInvoiceScreen(Invoice invoice) throws IOException {
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}
}
