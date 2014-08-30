package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="bb_post_distance")
public class PostDistance extends Model {
	
	@EmbeddedId
	private PK id;
	
	@ManyToOne
	@MapsId("fromPostId")
	private Post fromPost;
	
	@ManyToOne
	@MapsId("toPostId")
	private Post toPost;
	
	@Column(name="distance")
	private Double distance;
	
	@Embeddable
	public static class PK {
		@Column(name="from_post_id")
		private Long fromPostId;
		
		@Column(name="to_post_id")
		private Long toPostId;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((fromPostId == null) ? 0 : fromPostId.hashCode());
			result = prime * result
					+ ((toPostId == null) ? 0 : toPostId.hashCode());
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
			if (fromPostId == null) {
				if (other.fromPostId != null)
					return false;
			} else if (!fromPostId.equals(other.fromPostId))
				return false;
			if (toPostId == null) {
				if (other.toPostId != null)
					return false;
			} else if (!toPostId.equals(other.toPostId))
				return false;
			return true;
		}
	}
	
	public static Finder<PostDistance.PK,PostDistance> find = new Finder<PostDistance.PK,PostDistance>(PostDistance.PK.class,PostDistance.class);
	
	
	public PostDistance() {
		
	}


	public Post getFromPost() {
		return fromPost;
	}
	public Post getToPost() {
		return toPost;
	}
	public Double getDistance() {
		return distance;
	}

}
