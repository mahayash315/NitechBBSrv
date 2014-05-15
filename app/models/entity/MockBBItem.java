package models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.request.mockbb.admin.EditItemRequest;
import models.service.MockBBItem.MockBBItemModelService;
import models.service.MockBBItem.MockBBItemService;
import play.data.format.Formats.DateTime;
import play.db.ebean.Model;
import play.mvc.PathBindable;

import com.avaje.ebean.Page;

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
	boolean isFlagged;
	
	@Lob
	@Column(name = "body")
	String body;
	
	
	@Transient
	MockBBItemService mockBBItemService = new MockBBItemService();
	@Transient
	MockBBItemModelService mockBBItemModelService = new MockBBItemModelService();
	
	
	/* finder */
	
	public static Finder<MockBBItemPK, MockBBItem> find = new Finder<MockBBItemPK, MockBBItem>(MockBBItemPK.class, MockBBItem.class);
	
	
	/* コンストラクタ */
	
	public MockBBItem() {
		
	}
	public MockBBItem(MockBBItemPK id) {
		this.id = id;
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
	
	public Page<MockBBItem> findPage(Integer pageSource, String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		return mockBBItemModelService.findPage(pageSource, orderByClause, filter, hideRead, hideReference, onlyFragged);
	}
	
	public EditItemRequest getEditItemRequest() {
		return mockBBItemService.itemToEditItemRequest(this);
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


	public boolean isFlagged() {
		return isFlagged;
	}


	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}
	
	
	
	@Embeddable
	public static class MockBBItemPK implements Serializable, PathBindable<MockBBItemPK> {

		@Column(name = "id_date", length = 10, nullable = false)
		String idDate;
		
		@GeneratedValue(strategy = GenerationType. AUTO)
		@Column(name = "id_index", nullable = false)
		Integer idIndex;

		public MockBBItemPK() {
			
		}
		public MockBBItemPK(String idDate, Integer idIndex) {
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
		
		@Override
		public MockBBItemPK bind(String key, String txt) {
			String[] sp = txt.split("_",2);
			if (sp.length == 2) {
				return new MockBBItemPK(sp[0], Integer.valueOf(sp[1]));
			}
			return null;
		}
		
		@Override
		public String unbind(String key) {
			return idDate+"_"+idIndex;
		}
		
		@Override
		public String javascriptUnbind() {
			return null;
		}
		
		@Override
		public String toString() {
			return idDate+"_"+idIndex;
		}
		
		public String getIdDate() {
			return idDate;
		}

		public void setIdDate(String idDate) {
			this.idDate = idDate;
		}

		public Integer getIdIndex() {
			return idIndex;
		}

		public void setIdIndex(Integer idIndex) {
			this.idIndex = idIndex;
		}
	}
}
