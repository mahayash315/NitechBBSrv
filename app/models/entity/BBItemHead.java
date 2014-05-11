package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.BBItemHead.BBItemHeadModelService;
import models.service.BBItemHead.BBItemHeadService;
import play.db.ebean.Model;

@Entity
@Table(
	name = "bb_item_head",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "id_date", "id_index"})
	}
)
public class BBItemHead extends Model {

	@Id
	Long id;
	
	@Column(name = "id_date", length=191, nullable = false)
	String idDate;
	
	@Column(name = "id_index", length=191, nullable = false)
	String idIndex;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	
	@Column(name = "date_show", length=191, nullable = true)
	String dateShow;
	
	@Column(name = "date_exec", length=191, nullable = true)
	String dateExec;

	@Lob
	@Column(name = "author")
	String author;
	
	@Lob
	@Column(name = "title")
	String title;

	
	@Transient
	BBItemHeadService bbItemHeadService = new BBItemHeadService();
	@Transient
	BBItemHeadModelService bbItemHeadModelService = new BBItemHeadModelService();
	
	
	/* コンストラクタ */

	public BBItemHead() {
	}
	
	public BBItemHead(Long id) {
		this.id = id;
	}
	
	public BBItemHead(String idDate, String idIndex, String dateShow, String dateExec, String title) {
		this.idDate = idDate;
		this.idIndex = idIndex;
		this.dateShow = dateShow;
		this.dateExec = dateExec;
		this.title = title;
	}
	
	
	/* finder */
	
	public static Finder<Long, BBItemHead> find = new Finder<Long, BBItemHead>(Long.class, BBItemHead.class);
	
	
	/* メソッド */
	
	/**
	 * 結果を保存
	 * @return
	 */
	public BBItemHead store() {
		if (unique() == null) {
			return bbItemHeadModelService.save(this);
		}
		return bbItemHeadModelService.update(this);
	}
	
	/**
	 * id または user, idDate, idIndex に該当するものを検索
	 * @return
	 */
	public BBItemHead unique() {
		BBItemHead o = null;
		if ((o = bbItemHeadModelService.findById(id)) != null) {
			return o;
		}
		if ((o = bbItemHeadModelService.findByUserDateIndex(user, idDate, idIndex)) != null) {
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

	public String getIdDate() {
		return idDate;
	}

	public void setIdDate(String idDate) {
		this.idDate = idDate;
	}

	public String getIdIndex() {
		return idIndex;
	}

	public void setIdIndex(String idIndex) {
		this.idIndex = idIndex;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDateShow() {
		return dateShow;
	}

	public void setDateShow(String dateShow) {
		this.dateShow = dateShow;
	}

	public String getDateExec() {
		return dateExec;
	}

	public void setDateExec(String dateExec) {
		this.dateExec = dateExec;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
