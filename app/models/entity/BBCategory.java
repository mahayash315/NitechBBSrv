package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.BBCategory.BBCategoryModelService;
import models.service.BBCategory.BBCategoryService;
import play.db.ebean.Model;

@Entity
@Table(
	name = "bb_category",
	uniqueConstraints = {
		@UniqueConstraint(columnNames={"user_id", "name"})
	}
)
public class BBCategory extends Model {

	@Id
	Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@Column(name = "name", length = 171)
	String name;
	
	
	@Transient
	BBCategoryService bbCategoryService = new BBCategoryService();
	@Transient
	BBCategoryModelService bbCategoryModelService = new BBCategoryModelService();
	
	/* finder */
	
	public static Finder<Long, BBCategory> find = new Finder<Long, BBCategory>(Long.class, BBCategory.class);
	
	/* コンストラクタ */
	
	public BBCategory() {
		super();
	}
	public BBCategory(User user, String name) {
		super();
		this.user = user;
		this.name = name;
	}
	
	/* インスタンスメソッド */
	
	public BBCategory store() {
		BBCategory o = unique();
		if (o == null) {
			return bbCategoryModelService.save(this);
		}
		return bbCategoryModelService.update(this, o.getId());
	}
	
	public BBCategory unique() {
		BBCategory o = null;
		if ((o = bbCategoryModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbCategoryModelService.findByUserName(user, name)) != null) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
