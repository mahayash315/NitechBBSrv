package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.ebean.Model;

@Entity
@Table(name="bb_user_cluster_vector")
public class UserClusterVector extends Model {

	@EmbeddedId
	public PK id;
	
	@ManyToOne
	@MapsId("clusterId")
	public UserCluster cluster;
	
	@Transient
	@MapsId("clazz")
	public Boolean clazz;
	
	@ManyToOne
	@MapsId("wordId")
	public Word word;
	
	@Column(name="value")
	public double value;
	
	@Embeddable
	public static class PK {
		@Column(name="cluster_id")
		public Long clusterId;
		
		@Column(name="class")
		public Boolean clazz;
		
		@Column(name="word_id")
		public Long wordId;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			result = prime * result
					+ ((clusterId == null) ? 0 : clusterId.hashCode());
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
			if (clazz == null) {
				if (other.clazz != null)
					return false;
			} else if (!clazz.equals(other.clazz))
				return false;
			if (clusterId == null) {
				if (other.clusterId != null)
					return false;
			} else if (!clusterId.equals(other.clusterId))
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
