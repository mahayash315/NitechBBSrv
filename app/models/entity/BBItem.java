package models.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.bbitem.BBItemModelService;
import models.service.bbitem.BBItemService;
import play.data.format.Formats.DateTime;
import play.db.ebean.Model;
import utils.EntityUtil;

@Entity
@Table(name = "bb_item", uniqueConstraints={
		@UniqueConstraint(columnNames={"id_date", "id_index"})
})
public class BBItem extends Model {

	@Id
	private Long id;
	
	@Column(name = "id_date", length=10, nullable = false)
	String idDate;
	
	@Column(name = "id_index", length=3, nullable = false)
	String idIndex;
	
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
	
	@Lob
	@Column(name = "body")
	String body;
	
	@DateTime(pattern="YYYY/MM/DD")
	@Column(name = "last_update")
	Date lastUpdate;
	
	@Transient
	BBItemService bbItemService = new BBItemService();
	@Transient
	BBItemModelService bbItemModelService = new BBItemModelService();
	
	
	/* finder */
	public static Finder<Long, BBItem> find = new Finder<Long, BBItem>(Long.class, BBItem.class);
	
	/* コンストラクタ */
	public BBItem() {
	}
	public BBItem(Long id) {
		this.id = id;
	}
	public BBItem(String idDate, String idIndex) {
		this.idDate = idDate;
		this.idIndex = idIndex;
	}
	
	/* インスタンスメソッド */
	public BBItem store() {
		lastUpdate = new Date();
		return bbItemModelService.save(this);
	}
	public BBItem unique() {
		BBItem o = null;
		if ((o = bbItemModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbItemModelService.findByDateIndex(idDate, idIndex)) != null) {
			return o;
		}
		return null;
	}
	
	public Set<BBItem> getAllItems() {
		return bbItemModelService.getAllItems();
	}
	
	/* toString */
	@Override
	public String toString() {
		return "("+getIdDate()+","+getIdIndex()+")"+getAuthor()+"-"+getTitle();
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/* CONST */
	public static final String ENTITY = EntityUtil.getTableName(BBItem.class);
	public static class PROPERTY {
		public static final String ID = "id";
	}
}
