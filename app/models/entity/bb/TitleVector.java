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
@Table(name="bb_title_vector")
public class TitleVector extends Model {

	@EmbeddedId
	public PK id;
	
	@ManyToOne
	@MapsId("post_id")
	public Post post;
	
	@ManyToOne
	@MapsId("word_id")
	public Word word;
	
	@Column(name="value")
	public boolean value;
	
	@Embeddable
	public static class PK {
		@Column(name="post_id")
		public Long postId;
		
		@Column(name="word_id")
		public Long wordId;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((postId == null) ? 0 : postId.hashCode());
			result = prime * result
					+ ((wordId == null) ? 0 : wordId.hashCode());
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
			if (postId == null) {
				if (other.postId != null)
					return false;
			} else if (!postId.equals(other.postId))
				return false;
			if (wordId == null) {
				if (other.wordId != null)
					return false;
			} else if (!wordId.equals(other.wordId))
				return false;
			return true;
		}
	}
	
}
