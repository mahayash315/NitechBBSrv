package models.entity.bb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.entity.User;
import play.db.ebean.Model;

@Entity
@Table(name="bb_title_cluster")
public class TitleCluster extends Model {

	@Id
	public Long id;
	
	@OneToOne(optional=true)
	@JoinColumn(name="user_id")
	public User user;
	
	@Column(name="depth")
	public int depth;
	
	@ManyToOne
	@Column(name="parent_cluster_id")
	public TitleCluster parent; 
	
	@OneToMany(mappedBy="cluster")
	public List<TitleClusterVector> center;
	
}
