package entity.payment;

import entity.db.AIMSDB;
import entity.order.*;
import entity.shipping.DeliveryInfo;

import java.sql.*;

public class PaymentTransaction {
	private String errorCode;
	private CreditCard card;
	private String transactionId;
	private String transactionContent;
	private int amount;
	private String createdAt;
	
	public PaymentTransaction(String errorCode, CreditCard card, String transactionId, String transactionContent,
			int amount, String createdAt) {
		super();
		this.errorCode = errorCode;
		this.card = card;
		this.transactionId = transactionId;
		this.transactionContent = transactionContent;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	/**
	 * Save all information of successful order
	 * @param order
	 * @throws SQLException
	 */
	public void saveTransactionHistory(Order order) throws SQLException {
		int cardId = checkAvailableCard(card.getCardCode(), card.getOwner());
		Connection conn = AIMSDB.getConnection();
		try {
			PreparedStatement orderStat = prepareInsertOrderStatement(transactionId, order);
			orderStat.executeUpdate();
			ResultSet orderRs = orderStat.getGeneratedKeys();
			int orderId = -1;
			if(orderRs.next()){
				orderId = orderRs.getInt(1);
			}
			DeliveryInfo normalDeliveryInfo = null;
			DeliveryInfo rushDeliveryInfo = null;
			if (order instanceof MixedOrder || order instanceof NormalOrder){
				normalDeliveryInfo = order.getNormalDeliveryInfo();
				PreparedStatement normdlStat = prepareInsertDeliveryInfoStatement(orderId, normalDeliveryInfo);
				normdlStat.executeUpdate();
			}
			if (order instanceof MixedOrder || order instanceof RushOrder) {
				rushDeliveryInfo = order.getRushDeliveryInfo();
				PreparedStatement rushdlStat = prepareInsertDeliveryInfoStatement(orderId, rushDeliveryInfo);
				rushdlStat.executeUpdate();
			}
			if(cardId == -1){
				PreparedStatement cardStat = prepareInsertCardStatement(card);
				cardStat.executeUpdate();
				ResultSet cardRs = cardStat.getGeneratedKeys();
				if(cardRs.next()){
					cardId = cardRs.getInt(1);
				}
				else{
					throw new SQLException();
				}
			}
			PreparedStatement transStat = prepareInsertTransactionStatement(cardId, this);
			transStat.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	private PreparedStatement prepareInsertTransactionStatement(int cardId, PaymentTransaction transaction){
		String sql = "INSERT INTO 'Transaction'(createAt, content, amount, cardId, id) VALUES(?, ?, ?, ?, ?)";
		Connection conn = AIMSDB.getConnection();
		PreparedStatement prestat = null;
		try {
			prestat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prestat.setString(1, transaction.getCreatedAt());
			prestat.setString(2, transaction.getTransactionContent());
			prestat.setInt(3, transaction.getAmount());
			prestat.setInt(4, cardId);
			prestat.setString(5, transaction.getTransactionId());

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			return prestat;
		}
	}

	private PreparedStatement prepareInsertOrderStatement(String transactionId, Order order) throws SQLException {
		String sql = "INSERT INTO 'Order'(shippingFee,type,transactionId) VALUES(?,?,?)";
		Connection conn = AIMSDB.getConnection();
		PreparedStatement prestat = null;
		try {
			prestat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prestat.setInt(1, order.getShippingFee());
			prestat.setString(2, order.getClass().toString());
			prestat.setString(3, transactionId);
		} catch (SQLException throwables) {
			throw throwables;
		}
		finally {
			return prestat;
		}
	}

	private int checkAvailableCard(String cardCode, String owner){
		// require auto_commit = true
		int cardId = -1;
		String sql_1 = "SELECT id FROM Card WHERE cardNumber = ? and holderName = ?";
		Connection conn = AIMSDB.getConnection();
		PreparedStatement prestat = null;
		try {
			prestat = conn.prepareStatement(sql_1);
			prestat.setString(1, cardCode);
			prestat.setString(2, owner);
			ResultSet rs = prestat.executeQuery();
			if(rs.next()) {
				cardId = rs.getInt(1);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return cardId;
	}

	private PreparedStatement prepareInsertCardStatement(CreditCard card){
		String sql_2 = "INSERT INTO Card(cardNumber, holderName) VALUES(?, ?)";
		Connection conn = AIMSDB.getConnection();
		PreparedStatement prestat = null;
		try {
			prestat = conn.prepareStatement(sql_2, Statement.RETURN_GENERATED_KEYS);
			prestat.setString(1, card.getCardCode());
			prestat.setString(2, card.getOwner());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		finally {
			return prestat;
		}
	}

	private PreparedStatement prepareInsertDeliveryInfoStatement(Integer orderId, DeliveryInfo deliveryInfo){
		String sql = "INSERT INTO DeliveryInfo(province, address, phoneNumber, deliveryInstruction, type, orderId, expectedInterval) VALUES(?, ?, ?, ?, ?, ?, ?)";
		Connection conn = AIMSDB.getConnection();
		PreparedStatement prestat = null;
		try {
			prestat = conn.prepareStatement(sql);
			prestat.setString(1, deliveryInfo.getProvince());
			prestat.setString(2, deliveryInfo.getAddress());
			prestat.setString(3, deliveryInfo.getPhoneNumber());
			prestat.setString(4, deliveryInfo.getDeliveryInstruction());
			prestat.setString(5, deliveryInfo.getType());
			prestat.setInt(6, orderId);
			prestat.setString(7, deliveryInfo.getExpectedDate());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			return prestat;
		}
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public CreditCard getCard() {
		return card;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getTransactionContent() {
		return transactionContent;
	}

	public int getAmount() {
		return amount;
	}

	public String getCreatedAt() {
		return createdAt;
	}
}
