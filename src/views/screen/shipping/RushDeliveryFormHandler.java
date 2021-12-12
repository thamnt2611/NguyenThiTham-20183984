package views.screen.shipping;

import common.exception.InvalidFormInputException;
import common.exception.ProcessInvoiceException;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.shipping.DeliveryInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * form the behaviour of RushDeliveryForm
 */
public class RushDeliveryFormHandler extends BaseScreenHandler {
	@FXML
	private TextField address;

	@FXML
	private DatePicker expectedDeliveryDate;

	@FXML
	private TextField deliveryInstruction;

	@FXML
	private VBox vboxItems;

	@FXML
	private Button confirmBtn;

	private Invoice invoice;

	private DeliveryInfo rushDeliveryInfo;

	public RushDeliveryFormHandler(Stage stage, String screenPath, Invoice initialInvoice) throws IOException {
		super(stage, screenPath);
		this.invoice = initialInvoice;
		setDefaultInfo();
		confirmBtn.setOnMouseClicked(e -> {
			try {
				confirmRushDelivery();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		});

		expectedDeliveryDate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				LocalDate date = expectedDeliveryDate.getValue();
				rushDeliveryInfo.setExpectedDeliveryDate(date.toString());
				System.out.println(date.toString());
			}
		});
	}


	public void setDefaultInfo() {
		rushDeliveryInfo = new DeliveryInfo();
		DeliveryInfo initialDeliveryInfo = invoice.getOrder().getNormalDeliveryInfo();
		rushDeliveryInfo.setProvince(initialDeliveryInfo.getProvince());
		rushDeliveryInfo.setReceiverFullName(initialDeliveryInfo.getReceiverFullName());
		rushDeliveryInfo.setPhoneNumber(initialDeliveryInfo.getPhoneNumber());
		rushDeliveryInfo.setType(DeliveryInfo.RUSH_TYPE);

		this.address.setText(initialDeliveryInfo.getAddress());
		// load order item that support rush delivery
		PlaceRushOrderController controller = (PlaceRushOrderController) getBController();
		List<OrderMedia> rushMedias = controller.getSupportRushMedias(invoice.getOrder());
		for (OrderMedia orderMedia : rushMedias){
			try {
				MediaRushScreenHandler mis = new MediaRushScreenHandler(Configs.MEDIA_RUSH_PATH);
				mis.setOrderMedia(orderMedia);
				vboxItems.getChildren().add(mis.getContent());
			} catch (IOException | SQLException e) {
				System.err.println("errors: " + e.getMessage());
				throw new ProcessInvoiceException(e.getMessage());
			}
		}
	}



	/**
	 * handle click event of the Confirm Rush Delivery button
	 * @throws IOException
	 */
	public void confirmRushDelivery() throws IOException {
		rushDeliveryInfo.setAddress(address.getText());
		rushDeliveryInfo.setDeliveryInstruction(deliveryInstruction.getText());
		PlaceRushOrderController controller = (PlaceRushOrderController) getBController();
		try {
			controller.validateRushDeliveryForm(rushDeliveryInfo);
			invoice.getOrder().setRushDeliveryInfo(rushDeliveryInfo);
			for(OrderMedia orderMedia : invoice.getOrder().getlstOrderMedia()){
				if(orderMedia.getMedia().doSupportRushDelivery()){
					orderMedia.setDeliveryType(DeliveryInfo.RUSH_TYPE);
				}
				else{
					orderMedia.setDeliveryType(DeliveryInfo.NORMAL_TYPE);
				}
			}
			int shippingFees = PlaceOrderController.calculateShippingFee(invoice.getOrder());
			invoice.getOrder().setShippingFees(shippingFees);
			showInvoiceScreen();
		} catch (InvalidFormInputException e) {
			showDialog(e.getMessage());
		}
	}

	private void showInvoiceScreen() throws IOException {
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}
}
