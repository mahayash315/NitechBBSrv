package models.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.entity.BBReadHistory;
import models.entity.User;
import play.db.DB;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

public class UserService {

	private Connection conn;
	
	private static class SQL_BB_READ_HISTORY {
		static final String SQL_SELECT_USER_VECTOR = "select "+BBReadHistory.PROPERTY.ITEM+", length("+BBReadHistory.PROPERTY.ID+")"
				+ " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+"=? group by "+BBReadHistory.PROPERTY.ITEM;
		
		private static class STATEMENT {
			static PreparedStatement selectUserVector(Connection conn, User user) throws SQLException {
				PreparedStatement st = conn.prepareStatement(SQL_SELECT_USER_VECTOR);
				st.setLong(1, user.getId());
				return st;
			}
		}
	}
	
	public static UserService use() {
		return new UserService();
	}
	
	/**
	 * ユーザベクトルを返す
	 * @param user
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public int[] getUserVector(User user, int size) throws SQLException {
		int[] vector = new int[size];
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = SQL_BB_READ_HISTORY.STATEMENT.selectUserVector(conn, user);
			rs = st.executeQuery();
			
			while(rs.next()) {
				int index = (int) rs.getLong(1) - 1;
				int count = rs.getInt(2);
				if (index < size) {
					vector[index] = count;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (st != null) {
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		
		return vector;
	}
	
	
	
	
	
	private UserService useConnection() {
		if (conn != null) {
			Transaction t = Ebean.currentTransaction();
			if (t != null) {
				conn = t.getConnection();
			} else {
				conn = DB.getConnection();
			}
		}
		return this;
	}
}
