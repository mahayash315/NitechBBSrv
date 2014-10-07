package models.entity.bb;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.entity.NitechUser;
import models.service.model.bb.UserClusterModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_user_cluster",uniqueConstraints={
	@UniqueConstraint(columnNames={"nitech_user_id"})
})
public class UserCluster extends Model {

	@Id
	private Long id;
	
	@OneToOne(optional=true)
	@JoinColumn(name="nitech_user_id", nullable=true)
	private NitechUser nitechUser;
	
	@Column(name="depth")
	private Integer depth;
	
	@Column(name="weight")
	private long weight;
	
	@Column(name="prior_1")
	private Double prior1;
	
	@Column(name="prior_0")
	private Double prior0;
	
	@ManyToOne
	@Column(name="parent_cluster_id")
	private UserCluster parent; 

	@OneToMany(mappedBy="cluster",cascade=CascadeType.ALL)
	private List<UserClusterVector> vector;
	
	
	@Transient
	private UserClusterModelService modelService = new UserClusterModelService();
	
	public static Finder<Long,UserCluster> find = new Finder<Long,UserCluster>(Long.class,UserCluster.class);
	
	public UserCluster() {
		
	}
	public UserCluster(int depth) {
		this.depth = Integer.valueOf(depth);
	}
	
	public UserCluster unique() {
		UserCluster o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		if ((o = modelService.findByNitechUser(nitechUser)) != null) {
			return o;
		}
		return o;
	}
	public UserCluster store() {
		return modelService.save(this);
	}
	public List<UserCluster> findList() {
		return modelService.findList(depth);
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
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public long getWeight() {
		return weight;
	}
	public void setWeight(long weight) {
		this.weight = weight;
	}
	public UserCluster getParent() {
		return parent;
	}
	public void setParent(UserCluster parent) {
		this.parent = parent;
	}
	public List<UserClusterVector> getVector() {
		return vector;
	}
	public void setVector(List<UserClusterVector> vector) {
		this.vector = vector;
	}
	
}
