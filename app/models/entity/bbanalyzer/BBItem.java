package models.entity.bbanalyzer;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.bbanalyzer.BBItemService;
import models.service.model.bbanalyzer.BBItemModelService;
import play.data.format.Formats.DateTime;
import play.db.ebean.Model;
import utils.EntityUtil;

@Entity
@Table(name = "bba_item", uniqueConstraints={
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
	
	public List<BBItem> getAllItemsAsList() {
		return bbItemModelService.getAllItemsAsList();
	}
	
	public Set<BBItem> getAllItems() {
		return bbItemModelService.getAllItems();
	}
	
	/* toString */
	@Override
	public String toString() {
		return "("+getIdDate()+","+getIdIndex()+")"+getAuthor()+"-"+getTitle();
	}
	
	/* hashCode, equals */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result
				+ ((dateExec == null) ? 0 : dateExec.hashCode());
		result = prime * result
				+ ((dateShow == null) ? 0 : dateShow.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idDate == null) ? 0 : idDate.hashCode());
		result = prime * result + ((idIndex == null) ? 0 : idIndex.hashCode());
		result = prime * result
				+ ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BBItem other = (BBItem) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (dateExec == null) {
			if (other.dateExec != null)
				return false;
		} else if (!dateExec.equals(other.dateExec))
			return false;
		if (dateShow == null) {
			if (other.dateShow != null)
				return false;
		} else if (!dateShow.equals(other.dateShow))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idDate == null) {
			if (other.idDate != null)
				return false;
		} else if (!idDate.equals(other.idDate))
			return false;
		if (idIndex == null) {
			if (other.idIndex != null)
				return false;
		} else if (!idIndex.equals(other.idIndex))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
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
