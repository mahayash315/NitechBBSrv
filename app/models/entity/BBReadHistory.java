package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "bb_read_history")
public class BBReadHistory extends Model {

	@Id
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@OneToOne
	@JoinColumn(name = "bb_item_head_id")
	BBItemHead item;
	
	@Column(name = "open_time")
	Long openTime;
	
	@Column(name = "read_time_length")
	Long readTimeLength;
	
	
	/* コンストラクタ */
	
	public BBReadHistory(User user, BBItemHead item, Long openTime,
			Long readTimeLength) {
		this.user = user;
		this.item = item;
		this.openTime = openTime;
		this.readTimeLength = readTimeLength;
	}

	
	/* getter, setter */
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BBItemHead getItem() {
		return item;
	}

	public void setItem(BBItemHead item) {
		this.item = item;
	}

	public Long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}

	public Long getReadTimeLength() {
		return readTimeLength;
	}

	public void setReadTimeLength(Long readTimeLength) {
		this.readTimeLength = readTimeLength;
	}
}
