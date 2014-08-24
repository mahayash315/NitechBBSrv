package models.entity.bb;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	@OneToMany(mappedBy="post",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private List<WordInPost> wordsInPost;
	
	@OneToMany(mappedBy="post",cascade={CascadeType.REMOVE},fetch=FetchType.LAZY)
	private List<Possession> possessions;
	
	
	
	@Transient
	private PostModelService modelService = new PostModelService();
	
	
	public static Finder<Long,Post> find = new Finder<Long,Post>(Long.class,Post.class);
	
	
	public Post() {
		
	}
	

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
	public List<WordInPost> getWordsInPost() {
		return wordsInPost;
	}
	public void setWordsInPost(List<WordInPost> wordsInPost) {
		this.wordsInPost = wordsInPost;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idDate == null) ? 0 : idDate.hashCode());
		result = prime * result + idIndex;
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
		Post other = (Post) obj;
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
		if (idIndex != other.idIndex)
			return false;
		return true;
	}
	
}
