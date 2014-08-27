package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="post_distance")
public class PostDistance extends Model {
	
	@ManyToOne
	@MapsId("from_post_id")
	private Post from;
	
	@ManyToOne
	@MapsId("to_post_id")
	private Post to;
	
	@Column(name="distance")
	private Double distance;
	
	@Embeddable
	public static class PK {
		@Column(name="from_post_id")
		public Long fromPostId;
		
		@Column(name="to_post_id")
		public Long toPostId;
	}
	
	public PostDistance() {
		
	}

	public Double getDistance() {
		return distance;
	}

}
