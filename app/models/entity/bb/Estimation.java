package models.entity.bb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.entity.NitechUser;
import models.service.model.bb.EstimationModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_estimation")
public class Estimation extends Model {
	@EmbeddedId
	private PK id;

	@ManyToOne
	@MapsId("nitechUserId")
	private NitechUser nitechUser;
	
	@MapsId("depth")
	private Integer depth;
	
	@ManyToOne
	@MapsId("postId")
	private Post post;
	
	@Column(name="class", columnDefinition="tinyint default null")
	private Boolean clazz;
	
	@Column(name="liklihood")
	private Double liklihood;
	
	
	@Embeddable
	public static class PK {
		@Column(name="nitech_user_id")
		private Long nitechUserId;
		
		@Column(name="depth")
		private Integer depth;
		
		@Column(name="post_id")
		private Long postId;

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
	}
	
	
	@Transient
	protected EstimationModelService modelService = new EstimationModelService();
	
	
	public static Finder<Estimation.PK,Estimation> find = new Finder<Estimation.PK,Estimation>(Estimation.PK.class, Estimation.class);
	
	
	public Estimation() {
		
	}
	public Estimation(NitechUser nitechUser) {
		this.nitechUser = nitechUser;
	}
	
	
	public Estimation unique() {
		Estimation o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		return o;
	}
	public Estimation store() {
		return modelService.save(this);
	}
	
	@Override
	public void save() {
		modelService.save(this);
	}
	@Override
	public void delete() {
		modelService.delete(this);
	}
	
	/**
	 * おすすめ記事のエントリを返す
	 * @return
	 */
	public List<Estimation> findSuggestions() {
		return modelService.findSuggestions(nitechUser);
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
		this.nitechUser = nitechUser;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Boolean getClazz() {
		return clazz;
	}
	public void setClazz(Boolean clazz) {
		this.clazz = clazz;
	}
	public Double getLiklihood() {
		return liklihood;
	}
	public void setLiklihood(Double liklihood) {
		this.liklihood = liklihood;
	}
}
