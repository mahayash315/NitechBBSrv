package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.entity.NitechUser;
import models.service.model.bb.PossessionModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_possession")
public class Possession extends Model {
	@Embeddable
	public static class PK {
		private Long nitechUserId;
		
		private Long postId;
		
		private PK(NitechUser nitechUser, Post post) {
			this.nitechUserId = nitechUser.getId();
			this.postId = post.getId();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((nitechUserId == null) ? 0 : nitechUserId.hashCode());
			result = prime * result
					+ ((postId == null) ? 0 : postId.hashCode());
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
			PK other = (PK) obj;
			if (nitechUserId == null) {
				if (other.nitechUserId != null)
					return false;
			} else if (!nitechUserId.equals(other.nitechUserId))
				return false;
			if (postId == null) {
				if (other.postId != null)
					return false;
			} else if (!postId.equals(other.postId))
				return false;
			return true;
		}

		public Long getNitechUserId() {
			return nitechUserId;
		}

		public void setNitechUserId(Long nitechUserId) {
			this.nitechUserId = nitechUserId;
		}

		public Long getPostId() {
			return postId;
		}

		public void setPostId(Long postId) {
			this.postId = postId;
		}

	}
	
	@EmbeddedId
	private PK id;
	
	@ManyToOne
	@MapsId("id.nitechUserId")
	@JoinColumn(name="nitech_user_id", insertable=false, updatable=false)
	private NitechUser nitechUser;

	@ManyToOne
	@MapsId("id.postId")
	@JoinColumn(name="post_id", insertable=false, updatable=false)
	private Post post;
	
	@Column(name="is_favorite", columnDefinition="tinyint(1) default null")
	private Boolean isFavorite;
	
	@Column(name="class", columnDefinition="tinyint(1) default null")
	private Boolean clazz;
	
	@Transient
	protected PossessionModelService modelService = new PossessionModelService();
	
	public static Finder<Possession.PK,Possession> find = new Finder<Possession.PK,Possession>(Possession.PK.class,Possession.class);
	
	
	public Possession() {
		
	}
	public Possession(NitechUser nitechUser, Post post) {
		this.id = new PK(nitechUser, post);
		this.nitechUser = nitechUser;
		this.post = post;
	}
	
	public Possession unique() {
		Possession o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		return null;
	}
	public Possession store() {
		return modelService.save(this);
	}
	public Possession uniqueOrStore() {
		Possession o = unique();
		if (o == null) {
			o = store();
		}
		return o;
	}
	
	@Override
	public void save() {
		modelService.save(this);
	}
	@Override
	public void delete() {
		modelService.delete(this);
	}

	
	public PK getId() {
		return id;
	}
	public void setId(PK id) {
		this.id = id;
	}
	public NitechUser getNitechUser() {
		return nitechUser;
	}
	public void setNitechUser(NitechUser nitechUser) {
		this.id.nitechUserId = nitechUser.getId();
		this.nitechUser = nitechUser;
	}
	public Post getPost() {
		return post;
	}
	public Boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public void setPost(Post post) {
		this.id.postId = post.getId();
		this.post = post;
	}
	public Boolean getClazz() {
		return clazz;
	}
}
