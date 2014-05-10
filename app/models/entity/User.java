package models.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "user")
public class User extends Model {

	@Id
	Long id;
	
	@Column(name = "nitech_id", length = 191)
	String nitechId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	List<BBReadHistory> readHistories;

	/* コンストラクタ */
	
	public User(String nitechId) {
		this.nitechId = nitechId;
	}
	
	
	/* getter, setter */
	
	public String getNitechId() {
		return nitechId;
	}

	public void setNitechId(String nitechId) {
		this.nitechId = nitechId;
	}

	public List<BBReadHistory> getReadHistory() {
		return readHistories;
	}

	public void setReadHistory(List<BBReadHistory> readHistory) {
		this.readHistories = readHistory;
	}
}
