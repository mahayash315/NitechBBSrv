package models.entity.bb;

import java.util.List;

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
	
	
	public History() {
		
	}
	public History(NitechUser nitechUser) {
		this.nitechUser = nitechUser;
	}
	public History(NitechUser nitechUser, Post post, long timestamp) {
		this.nitechUser = nitechUser;
		this.post = post;
		this.timestamp = timestamp;
	}
	
	
	public History unique() {
		History o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		return null;
	}
	public History store() {
		return modelService.save(this);
	}
	
	/**
	 * リストを返す
	 * @param minTimestamp 最小のタイムスタンプ, null の場合は無視
	 * @return
	 */
	public List<History> findList(Long minTimestamp) {
		return modelService.findList(nitechUser, post, minTimestamp);
	}
	
	@Override
	public void save() {
		store();
	}
	@Override
	public void delete() {
		modelService.delete(this);
	}
	

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
