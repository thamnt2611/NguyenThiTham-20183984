package views.screen.shipping;

import entity.cart.CartMedia;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Utils;
import views.screen.FXMLScreenHandler;

import java.io.IOException;
import java.sql.SQLException;

public class MediaRushHandler extends FXMLScreenHandler {
    @FXML
    private HBox hboxMedia;

    @FXML
    private VBox imageLogoVbox;

    @FXML
    private ImageView image;

    @FXML
    private VBox description;

    @FXML
    private Label title;

    @FXML
    private Label numOfProd;

    @FXML
    private Label labelOutOfStock;

    @FXML
    private Label price;

    private CartMedia cartMedia;

    public MediaRushHandler(String screenPath) throws IOException {
        super(screenPath);
    }

    public void setOrderMedia(CartMedia cartMedia) throws SQLException {
        this.cartMedia = cartMedia;
        setMediaInfo();
    }

    public void setMediaInfo() throws SQLException{
        title.setText(cartMedia.getMedia().getTitle());
        price.setText(Utils.getCurrencyFormat(cartMedia.getPrice()));
        numOfProd.setText(String.valueOf(cartMedia.getQuantity()));
        setImage(image, cartMedia.getMedia().getImageURL());
        image.setPreserveRatio(false);
        image.setFitHeight(90);
        image.setFitWidth(83);
    }
}

