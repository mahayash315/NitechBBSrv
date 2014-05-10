package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.db.ebean.Model;

@Entity
@Table(
	name = "bb_item_head",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id_date", "id_index"})
	}
)
public class BBItemHead extends Model {

	@Id
	Long id;
	
	@Column(name = "id_date", length=191, nullable = false)
	String idDate;
	
	@Column(name = "id_index", length=191, nullable = false)
	String idIndex;
	
	@Column(name = "date_show", length=191, nullable = true)
	String dateShow;
	
	@Column(name = "date_exec", length=191, nullable = true)
	String dateExec;
	
	@Lob
	@Column(name = "title")
	String title;

	
	/* コンストラクタ */
	
	public BBItemHead(String idDate, String idIndex, String dateShow, String dateExec, String title) {
		this.idDate = idDate;
		this.idIndex = idIndex;
		this.dateShow = dateShow;
		this.dateExec = dateExec;
		this.title = title;
	}
	
	
	/* getter, setter */

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
