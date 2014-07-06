package models.service;

import java.sql.Connection;
import java.sql.SQLException;

import play.db.DB;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

public abstract class AbstractService {

	Connection conn;
	
	protected Connection getConnection() {
		if (conn == null) {
			Transaction t = Ebean.currentTransaction();
			if (t != null) {
				conn = t.getConnection();
			} else {
				conn = DB.getConnection();
			}
		}
		return conn;
	}
	
	protected void closeConnection() throws SQLException {
		if (conn != null) {
			if (Ebean.currentTransaction() == null) {
				conn.close();
				conn = null;
			}
		}
	}
}
