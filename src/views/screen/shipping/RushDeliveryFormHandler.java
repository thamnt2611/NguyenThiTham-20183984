package views.screen.shipping;

import common.exception.InvalidFormInputException;
import common.exception.ProcessInvoiceException;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.shipping.DeliveryInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

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

	private DeliveryInfo rushDeliveryInfo;

	private DeliveryInfo initialDeliveryInfo;

	private String expectedDateInput;

	public RushDeliveryFormHandler(Stage stage, String screenPath, DeliveryInfo initialDeliveryInfo) throws IOException {
		super(stage, screenPath);
		this.initialDeliveryInfo = initialDeliveryInfo;
		this.setBController(new PlaceRushOrderController());
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
				expectedDateInput = date.toString();
				System.out.println(date.toString());
			}
		});
	}


	public void setDefaultInfo() {
		this.address.setText(initialDeliveryInfo.getAddress());
		// load order item that support rush delivery
		PlaceRushOrderController controller = (PlaceRushOrderController) this.getBController();
		List<CartMedia> medias = controller.getListCartMedia();
		List<CartMedia> rushMedias = controller.getSupportRushMedia(medias);
		for (CartMedia cartMedia : rushMedias){
			try {
				MediaRushHandler mis = new MediaRushHandler(Configs.MEDIA_RUSH_PATH);
				mis.setOrderMedia(cartMedia);
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
		rushDeliveryInfo = new DeliveryInfo(initialDeliveryInfo.getReceiverFullName(),
				initialDeliveryInfo.getPhoneNumber(),
				initialDeliveryInfo.getProvince(),
				address.getText(),
				deliveryInstruction.getText(),
				initialDeliveryInfo.getType());
		rushDeliveryInfo.setExpectedDate(expectedDateInput);
		PlaceRushOrderController controller = (PlaceRushOrderController) getBController();
		try {
			controller.validateRushDeliveryForm(rushDeliveryInfo);
			Order order = controller.createOrder(initialDeliveryInfo, rushDeliveryInfo);
			Invoice invoice = controller.createInvoiceForOrder(order);
			showInvoiceScreen(invoice);
		} catch (InvalidFormInputException e) {
			showDialog(e.getMessage());
		}
	}

	private void showInvoiceScreen(Invoice invoice) throws IOException {
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}
}
