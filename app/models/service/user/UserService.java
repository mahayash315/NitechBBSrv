package models.service.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.entity.BBReadHistory;
import models.entity.User;
import models.service.AbstractService;

public class UserService extends AbstractService {

	private static class SQL_BB_READ_HISTORY {
		static final String SQL_SELECT_USER_VECTOR = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
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
	public double[] getUserVector(User user, int size) throws SQLException {
		double[] vector = new double[size];
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = SQL_BB_READ_HISTORY.STATEMENT.selectUserVector(getConnection(), user);
			rs = st.executeQuery();
			
			while(rs.next()) {
				int index = (int) rs.getLong(1) - 1;
				int count = rs.getInt(2);
				if (index < size) {
					vector[index] = Double.valueOf(count);
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
			closeConnection();
		}
		
		return vector;
	}
	
}
