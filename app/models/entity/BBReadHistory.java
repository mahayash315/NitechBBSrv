package models.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.BBReadHistory.BBReadHistoryModelService;
import models.service.BBReadHistory.BBReadHistoryService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_read_history")
public class BBReadHistory extends Model {

	@Id
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	
	@OneToOne
	@JoinColumn(name = "bb_item_head_id")
	BBItemHead item;
	
	@Column(name = "open_time")
	Long openTime;
	
	@Column(name = "read_time_length")
	Long readTimeLength;
	
	@Column(name = "referer")
	String referer;
	
	@Column(name = "filter")
	String filter;
	
	@Transient
	BBReadHistoryService bbReadHistoryService = new BBReadHistoryService();
	@Transient
	BBReadHistoryModelService bbReadHistoryModelService = new BBReadHistoryModelService();
	
	
	/* コンストラクタ */


	public BBReadHistory() {
	}
	
	public BBReadHistory(Long id) {
		this.id = id;
	}
	
	public BBReadHistory(User user, BBItemHead item, Long openTime, Long readTimeLength, String referer, String filter) {
		this.user = user;
		this.item = item;
		this.openTime = openTime;
		this.readTimeLength = readTimeLength;
		this.referer = referer;
		this.filter = filter;
	}
	
	
	/* finder */
	
	public static Finder<Long, BBReadHistory> find = new Finder<Long, BBReadHistory>(Long.class, BBReadHistory.class);
	
	
	/* メソッド */
	
	/**
	 * 結果を保存
	 * @return
	 */
	public BBReadHistory store() {
		BBReadHistory o = unique();
		if (o == null) {
			return bbReadHistoryModelService.save(this);
		}
		return bbReadHistoryModelService.update(this, o.getId());
	}
	
	/**
	 * id または User, BBItemHead, openTime, readTimeLength に該当するものを検索
	 * @return
	 */
	public BBReadHistory unique() {
		BBReadHistory o = null;
		if ((o = bbReadHistoryModelService.findById(id)) != null) {
			return o;
		}
		if ((o = bbReadHistoryModelService.findByUserHeadTime(user, item, openTime)) != null) {
			return o;
		}
		return null;
	}
	
	/**
	 * このエントリを削除する
	 */
	public void remove() {
		if (id != null) {
			bbReadHistoryModelService.delete(this);;
		}
	}
	
	/**
	 * User に対する ReadHistory の一覧を取得する
	 * @param user ユーザ, null の場合 this.user が入る
	 * @param minOpenTime 最小の openTime, 指定しない場合 null
	 * @param orderByClause 出力順の指定文, 指定しない場合 null
	 * @return
	 */
	public List<BBReadHistory> findListForUser(User user, Long minOpenTime, String orderByClause) {
		if (user == null) {
			user = this.user;
		}
		return bbReadHistoryModelService.findListForUser(user, minOpenTime, orderByClause);
	}
	
	/* getter, setter */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
