package models.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.User.UserModelService;
import models.service.User.UserService;
import play.db.ebean.Model;

@Entity
@Table(name = "user")
public class User extends Model {

	@Id
	Long id;
	
	@Column(name = "nitech_id", length = 191)
	String nitechId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	List<BBItemHead> items;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	List<BBReadHistory> readHistories;
	
	
	@Transient
	UserService userService = new UserService();
	@Transient
	UserModelService userModelService = new UserModelService();

	
	/* コンストラクタ */

	public User() {
	}
	
	public User(Long id) {
		this.id = id;
	}
	
	public User(String nitechId) {
		this.nitechId = nitechId;
	}
	
	
	/* finder */
	
	public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);
	
	
	/* メソッド */

	/**
	 * 結果を保存 (insert または update)
	 * @return
	 */
	public User store() {
		if (unique() == null) {
			return userModelService.save(this);
		}
		return userModelService.update(this);
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
		if ((o = userModelService.findByNitechId(nitechId)) != null) {
			return o;
		}
		return null;
	}
	
	
	/* getter, setter */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNitechId() {
		return nitechId;
	}

	public void setNitechId(String nitechId) {
		this.nitechId = nitechId;
	}

	public List<BBItemHead> getItems() {
		return items;
	}

	public void setItems(List<BBItemHead> items) {
		this.items = items;
	}

	public List<BBReadHistory> getReadHistory() {
		return readHistories;
	}

	public void setReadHistory(List<BBReadHistory> readHistory) {
		this.readHistories = readHistory;
	}
}
