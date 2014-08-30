package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name="bb_word_in_post")
public class WordInPost extends Model {
	@Embeddable
	public static class PK {
		@Column(name="post_id")
		private Long postId;
		
		@Column(name="word_id")
		private Long wordId;

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

	@EmbeddedId
	private PK id;
	
	@ManyToOne
	@MapsId("postId")
	@JoinColumn(name="post_id", insertable=false, updatable=false)
	private Post post;
	
	@ManyToOne
	@MapsId("wordId")
	@JoinColumn(name="word_id", insertable=false, updatable=false)
	private Word word;
	
	@Column(name="value")
	private boolean value;

	public PK getId() {
		return id;
	}
	public void setId(PK id) {
		this.id = id;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Word getWord() {
		return word;
	}
	public void setWord(Word word) {
		this.word = word;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	
}
