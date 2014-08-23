package models.entity.bb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.model.bb.PostModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_post", uniqueConstraints={
	@UniqueConstraint(columnNames={"id_date", "id_index"})
})
public class Post extends Model {
	
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="id_date", length=10)
	private String idDate;
	
	@Column(name="id_index")
	private int idIndex;
	
	@OneToMany(mappedBy="post")
	private List<AuthorVector> authorVector;
	
	@OneToMany(mappedBy="post")
	private List<TitleVector> titleVector;
	
	
	
	@Transient
	private PostModelService modelService = new PostModelService();
	
	
	public static Finder<Long,Post> find = new Finder<Long,Post>(Long.class,Post.class);
	
	
	
	
	
	

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
	public int getIdIndex() {
		return idIndex;
	}
	public void setIdIndex(int idIndex) {
		this.idIndex = idIndex;
	}
	public List<AuthorVector> getAuthorVector() {
		return authorVector;
	}
	public void setAuthorVector(List<AuthorVector> authorVector) {
		this.authorVector = authorVector;
	}
	public List<TitleVector> getTitleVector() {
		return titleVector;
	}
	public void setTitleVector(List<TitleVector> titleVector) {
		this.titleVector = titleVector;
	}
	
	
	
}
