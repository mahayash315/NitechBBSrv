package models.entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.user.UserModelService;
import models.service.user.UserService;
import play.db.ebean.Model;

@Entity
@Table(name = "user")
public class User extends Model {

	@Id
	Long id;
	
	@Column(name = "hashed_nitech_id", length = 191)
	String hashedNitechId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	List<BBReadHistory> readHistories;
	
	@Transient
	UserService userService = new UserService();
	@Transient
	UserModelService userModelService = new UserModelService();

	
	/* コンストラクタ */

	public User() {
		super();
	}
	
	public User(Long id) {
		super();
		this.id = id;
	}
	
	public User(String hashedNitechId) {
		super();
		this.hashedNitechId = hashedNitechId;
	}
	
	
	/* finder */
	
	public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);
	
	
	/* メソッド */
	/**
	 * 結果を保存 (insert または update)
	 * @return
	 */
	public User store() {
//		User o = unique();
//		if (o == null) {
//			return userModelService.save(this);
//		}
//		return userModelService.update(this, o.getId());
		return userModelService.save(this);
	}
	
	/**
	 * id または nitechId に該当するものを検索
	 * @return
	 */
	public User unique() {
		User o = null;
		if ((o = userModelService.findById(id)) != null) {
			return o;
		}
		if ((o = userModelService.findByNitechId(hashedNitechId)) != null) {
			return o;
		}
		return null;
	}
	
	/**
	 * このエントリを削除する
	 */
	public void remove() {
		if (id != null) {
			userModelService.delete(this);
		}
	}
	
	/**
	 * ユーザ一覧を取得する
	 * @return
	 */
	public Set<User> findSet() {
		return userModelService.findSet();
	}
	
	/**
	 * ユーザベクトルを取得する
	 * @return
	 */
//	@Deprecated
//	public double[] getUserVector(int size) throws SQLException {
//		return userService.getUserVector(this, size);
//	}
	
	/**
	 * ユーザ特徴を取得する
	 * @return
	 */
	public Map<Long, Double> getUserFeature() throws SQLException {
		return userService.getUserFeature(this);
	}
	
	/* toString */
	@Override
	public String toString() {
		return "nitechId="+hashedNitechId;
	}
	
	/* getter, setter */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHashedNitechId() {
		return hashedNitechId;
	}
	public void setHashedNitechId(String hashedNitechId) {
		this.hashedNitechId = hashedNitechId;
	}
	public List<BBReadHistory> getReadHistory() {
		return readHistories;
	}
	public void setReadHistory(List<BBReadHistory> readHistory) {
		this.readHistories = readHistory;
	}
}
