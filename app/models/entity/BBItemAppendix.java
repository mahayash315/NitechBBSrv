package models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbitemappendix.BBItemAppendixModelService;
import models.service.bbitemappendix.BBItemAppendixService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_item_appendix")
public class BBItemAppendix extends Model {

	@Id
	Long id;
	
	@OneToOne(mappedBy = "appendix")
	BBItemHead head;
	
	@ManyToOne
	@JoinColumn(name = "bb_category_id")
	BBCategory category;
	
	
	@Transient
	BBItemAppendixService bbItemAppendixService = new BBItemAppendixService(); 
	@Transient
	BBItemAppendixModelService bbItemAppendixModelService = new BBItemAppendixModelService();
	
	/* finder */
	
	public static Finder<Long, BBItemAppendix> find = new Finder<Long, BBItemAppendix>(Long.class, BBItemAppendix.class);
	
	/* コンストラクタ */
	
	public BBItemAppendix() {
		super();
	}
	public BBItemAppendix(BBItemHead head, BBCategory category) {
		super();
		this.head = head;
		this.category = category;
	}

	/* インスタンスメソッド */

	public BBItemAppendix store() {
//		BBItemAppendix o = unique();
//		if (o == null) {
//			return bbItemAppendixModelService.save(this);
//		}
//		return bbItemAppendixModelService.update(this, o.getId());
		return bbItemAppendixModelService.save(this);
	}
	
	public BBItemAppendix unique() {
		BBItemAppendix o = null;
		if ((o = bbItemAppendixModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbItemAppendixModelService.findByHead(head)) != null) {
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

	public BBItemHead getHead() {
		return head;
	}

	public void setHead(BBItemHead head) {
		this.head = head;
	}

	public BBCategory getCategory() {
		return category;
	}

	public void setCategory(BBCategory category) {
		this.category = category;
	}
	
}
