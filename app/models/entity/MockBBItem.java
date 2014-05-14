package models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import models.service.MockBBItem.MockBBItemModelService;
import models.service.MockBBItem.MockBBItemService;
import play.data.format.Formats.DateTime;
import play.db.ebean.Model;

@Entity
@Table(name="mock_bb_item")
public class MockBBItem extends Model {

	@EmbeddedId
	MockBBItemPK id;

	@Column(name = "date_show")
	@DateTime(pattern="yyyy/MM/dd")
	Date dateShow;
	
	@Column(name = "date_exec")
	@DateTime(pattern="yyyy/MM/dd")
	Date dateExec;
	
	@Column(name = "author", length = 191)
	String author;
	
	@Column(name = "title", length = 191)
	String title;
	
	@Column(name = "is_read")
	boolean isRead;

	@Column(name = "is_reference")
	boolean isReference;

	@Column(name = "is_flagged")
	boolean isFragged;
	
	@Lob
	@Column(name = "body")
	String body;
	
	
	MockBBItemService mockBBItemService = new MockBBItemService();
	MockBBItemModelService mockBBItemModelService = new MockBBItemModelService();
	
	
	/* finder */
	
	public static Finder<MockBBItemPK, MockBBItem> find = new Finder<MockBBItemPK, MockBBItem>(MockBBItemPK.class, MockBBItem.class);
	
	
	/* コンストラクタ */
	
	public MockBBItem() {
		
	}
	
	
	/* メソッド */
	
	public MockBBItem store() {
		MockBBItem o = unique();
		if (o == null) {
			return mockBBItemModelService.save(this);
		}
		return mockBBItemModelService.update(this, o.getId());
	}
	
	public MockBBItem unique() {
		MockBBItem o = null;
		if ((o = mockBBItemModelService.findById(id)) != null) {
			return o;
		}
		return null;
	}
	
	public List<MockBBItem> findList(String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		return mockBBItemModelService.findList(orderByClause, filter, hideRead, hideReference, onlyFragged);
	}
	
	
	/* getter, setter */

	public MockBBItemPK getId() {
		return id;
	}
	
	public void setId(MockBBItemPK id) {
		this.id = id;
	}

	public Date getDateShow() {
		return dateShow;
	}


	public void setDateShow(Date dateShow) {
		this.dateShow = dateShow;
	}


	public Date getDateExec() {
		return dateExec;
	}


	public void setDateExec(Date dateExec) {
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


	public boolean isRead() {
		return isRead;
	}


	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}


	public boolean isReference() {
		return isReference;
	}


	public void setReference(boolean isReference) {
		this.isReference = isReference;
	}


	public boolean isFragged() {
		return isFragged;
	}


	public void setFragged(boolean isFragged) {
		this.isFragged = isFragged;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}
	
	
	
	@Embeddable
	public static class MockBBItemPK implements Serializable {

		@Column(name = "id_date", length = 10)
		String idDate;
		
		@Column(name = "id_index", length = 3)
		String idIndex;

		public MockBBItemPK() {
			
		}
		public MockBBItemPK(String idDate, String idIndex) {
			this.idDate = idDate;
			this.idIndex = idIndex;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((idDate == null) ? 0 : idDate.hashCode());
			result = prime * result
					+ ((idIndex == null) ? 0 : idIndex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MockBBItemPK other = (MockBBItemPK) obj;
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
			return true;
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
	}
}
