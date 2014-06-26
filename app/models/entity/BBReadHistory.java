package models.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbreadhistory.BBReadHistoryModelService;
import models.service.bbreadhistory.BBReadHistoryService;
import play.db.ebean.Model;
import utils.EntityUtil;

@Entity
@Table(name = "bb_read_history")
public class BBReadHistory extends Model {

	@Id
	Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	
	@OneToOne
	@JoinColumn(name = "bb_item_id")
	BBItem item;
	
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
		super();
	}
	public BBReadHistory(Long id) {
		super();
		this.id = id;
	}
	public BBReadHistory(User user, BBItem item, Long openTime, Long readTimeLength, String referer, String filter) {
		super();
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
//		BBReadHistory o = unique();
//		if (o == null) {
//			return bbReadHistoryModelService.save(this);
//		}
//		return bbReadHistoryModelService.update(this, o.getId());
		return bbReadHistoryModelService.save(this);
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
		if ((o = bbReadHistoryModelService.findByUserItemTime(user, item, openTime)) != null) {
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
	 * User に対する ReadHistory の一覧を List で取得する
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
	
	/**
	 * User に対する ReadHistory の一覧を Set で取得する
	 * @param user ユーザ, null の場合 this.user が入る
	 * @param minOpenTime 最小の openTime, 指定しない場合 null
	 * @param orderByClause 出力順の指定文, 指定しない場合 null
	 * @return
	 */
	public Set<BBReadHistory> findSetForUser(User user, Long minOpenTime) {
		if (user == null) {
			user = this.user;
		}
		return bbReadHistoryModelService.findSetForUser(user, minOpenTime);
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
	public BBItem getItem() {
		return item;
	}
	public void setItem(BBItem item) {
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
	
	/* CONST */
	public static final String ENTITY = EntityUtil.getTableName(BBReadHistory.class);
	public static class PROPERTY {
		public static final String ID = "id";
		public static final String USER = "user_id";
		public static final String ITEM = "bb_item_id";
	}
}
