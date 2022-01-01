package entity.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.sql.Connection;

import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
import entity.shipping.DeliveryInfo;
import utils.*;

public class AIMSDB {

	private static Logger LOGGER = Utils.getLogger(Connection.class.getName());
	private static Connection connect;

    public static Connection getConnection() {
        if (connect != null) return connect;
        try {
			Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:assets/db/aims.db";
            connect = DriverManager.getConnection(url);
            LOGGER.info("Connect database successfully");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        } 
        return connect;
    }
    

    public static void main(String[] args) throws SQLException {
//        Media media = new Media(44, "book4", "story", 57, 10, "book");
//        OrderMedia om = new OrderMedia(media, 2, DeliveryInfo.NORMAL_TYPE, 57);
//
//
//        DeliveryInfo norm = new DeliveryInfo("N.T Tham", "0987654321", "Thai Binh", "QS", "No");
//        norm.setType(DeliveryInfo.NORMAL_TYPE);
//        DeliveryInfo rush = new DeliveryInfo("N.T Tham", "0987654321", "Thai Binh", "QS", "No");
//        rush.setType(DeliveryInfo.RUSH_TYPE);
//        List<OrderMedia> omList = new ArrayList<>();
//        omList.add(om);
//        Order order = new Order(0, omList, norm, rush, Order.RUSH_AND_NORMAL);
//        CreditCard card = new CreditCard("0000000000", "tham", 234, "1234");
//        PaymentTransaction trans = new PaymentTransaction("0", card, "adfdsf", "pay order",
//        123, new Date().toString());
//        trans.saveTransactionHistory(order);
    }
}
