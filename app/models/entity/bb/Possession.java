package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import models.entity.NitechUser;
import play.db.ebean.Model;

@Entity
@Table(name="bb_possession")
public class Possession extends Model {
	@EmbeddedId
	private PK id;
	
	@ManyToOne
	@MapsId("nitechUserId")
	private NitechUser nitechUser;
	
	@ManyToOne
	@MapsId("postId")
	private Post post;
	
	@Column(name="is_interesting")
	private boolean isInteresting;
	
	@Embeddable
	public static class PK {
		@Column(name="nitech_user_id")
		private Long nitechUserId;
		
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
	
	public static Finder<Possession.PK,Possession> find = new Finder<Possession.PK,Possession>(Possession.PK.class,Possession.class);

	public boolean getIsInteresting() {
		return isInteresting;
	}
}