package models.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.entity.User;
import models.entity.bbanalyzer.BBReadHistory;

public class UserService extends AbstractService {

	private static class SQL_BB_READ_HISTORY {
		static final String SQL_SELECT_USER_VECTOR =
				"select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
						+ " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+"=? group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_USER_VECTOR_WITH_MIN_OPENTIME =
				"select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
						+ " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+"=?"
								+ " and "+BBReadHistory.PROPERTY.OPEN_TIME+" >= ?"
									+ " group by "+BBReadHistory.PROPERTY.ITEM;
		
		private static class STATEMENT {
			static PreparedStatement selectUserVector(Connection conn, User user, Long minOpenTime) throws SQLException {
				PreparedStatement st = null;
				if (minOpenTime == null) {
					st = conn.prepareStatement(SQL_SELECT_USER_VECTOR);
					st.setLong(1, user.getId());
				} else {
					st = conn.prepareStatement(SQL_SELECT_USER_VECTOR_WITH_MIN_OPENTIME);
					st.setLong(1, user.getId());
					st.setLong(2, minOpenTime);
				}
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
			st = SQL_BB_READ_HISTORY.STATEMENT.selectUserVector(getConnection(), user, null);
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
	
	/**
	 * ユーザ特徴を返す
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public Map<Long,Double> getUserFeature(User user) throws SQLException {
		Map<Long,Double> feature = new HashMap<Long,Double>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = SQL_BB_READ_HISTORY.STATEMENT.selectUserVector(getConnection(), user, null);
			rs = st.executeQuery();
			
			while(rs.next()) {
				long id = rs.getLong(1);
				int count = rs.getInt(2);
				feature.put(Long.valueOf(id), Double.valueOf(count));
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
		
		return feature;
	}
	
}
