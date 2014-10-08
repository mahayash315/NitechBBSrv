package models.entity.bb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import models.entity.NitechUser;
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
	
	@Column(name="author")
	private String author;
	
	@Column(name="title")
	private String title;
	
	@OneToMany(mappedBy="post",cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	private List<WordInPost> wordsInPost;
	
	@OneToMany(mappedBy="post",cascade={CascadeType.REMOVE},fetch=FetchType.LAZY)
	private List<Possession> possessions;
	
	@OneToMany(mappedBy="post",cascade={CascadeType.REMOVE},fetch=FetchType.LAZY)
	private List<History> histories;
	
	@Column(name="last_sampled")
	private Date lastSampled;
	
	@Version
	private Date lastModified;
	
	
	@Transient
	private PostModelService modelService = new PostModelService();
	
	
	public static Finder<Long,Post> find = new Finder<Long,Post>(Long.class,Post.class);
	
	
	public Post() {
		
	}
	public Post(Long id) {
		this.id = id;
	}
	public Post(String idDate, int idIndex) {
		this.idDate = idDate;
		this.idIndex = idIndex;
	}
	
	public Post unique() {
		Post o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		if ((o = modelService.findByIdDateIdIndex(idDate, idIndex)) != null) {
			return o;
		}
		return null;
	}
	public Post store() {
		modelService.save(this);
		if (getId() != null) {
			return this;
		}
		return null;
	}
	public Post uniqueOrStore() {
		Post o = unique();
		if (o == null) {
			o = store();
		}
		return o;
	}
	
	@Override
	public void save() {
		modelService.save(this);
	}
	public void delete() {
		modelService.delete(this);
	}
	
	public List<Post> findList() {
		return modelService.findList();
	}
	public List<Word> findWordsInPost() {
		return modelService.findWordsInPost(this);
	}
	public Map<Post,Long> findPopularPosts(NitechUser nitechUser, Long threshold, Integer limit) {
		return modelService.findPopularPosts(nitechUser, threshold, limit);
	}
	public Map<Post,Double> findRelevants(Double threshold, Integer limit) {
		return modelService.findRelevants(this, threshold, limit);
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
	public List<WordInPost> getWordsInPost() {
		return wordsInPost;
	}
	public void setWordsInPost(List<WordInPost> wordsInPost) {
		this.wordsInPost = wordsInPost;
	}
	public Date getLastSampled() {
		return lastSampled;
	}
	public void setLastSampled(Date lastSampled) {
		this.lastSampled = lastSampled;
	}
	public Date getLastModified() {
		return lastModified;
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
