package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.entity.NitechUser;
import models.service.model.bb.HistoryModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_history")
public class History extends Model {

	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="nitech_user_id")
	private NitechUser nitechUser;
	
	@ManyToOne
	@JoinColumn(name="post_id")
	private Post post;
	
	@Column(name="timestamp")
	private long timestamp;
	
	
	@Transient
	private HistoryModelService modelService = new HistoryModelService();
	
	
	public static Finder<Long,History> find = new Finder<Long,History>(Long.class,History.class);
	
	
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public NitechUser getNitechUser() {
		return nitechUser;
	}
	public void setNitechUser(NitechUser nitechUser) {
		this.nitechUser = nitechUser;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
