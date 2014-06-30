package models.entity;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbanalyzer.UserCluster;
import models.service.bbusercluster.BBUserClusterModelService;
import models.service.bbusercluster.BBUserClusterService;
import play.db.ebean.Model;
import utils.bbanalyzer.GsonUtil;

import com.google.gson.reflect.TypeToken;

@Entity
@Table(name = "bb_user_cluster")
public class BBUserCluster extends Model {

	@Id
	Long id;
	
	@Column(name = "cluster_depth")
	long clusterDepth;
	
	@Column(name = "cluster_id")
	long clusterId;
	
	@Lob
	@Column(name = "feature")
	String jsonFeature;
	
	@Transient
	Map<Long,Double> feature;
	
	@ManyToOne(cascade={})
	@Column(name = "parent")
	BBUserCluster parent;
	
	@OneToMany(mappedBy = "parent",cascade={})
	Set<BBUserCluster> children;
	
	@Column(name = "distance_from_parent")
	double distanceFromParent;
	
	
	@Transient
	BBUserClusterService bbUserClusterService = new BBUserClusterService();
	@Transient
	BBUserClusterModelService bbUserClusterModelService = new BBUserClusterModelService();
	
	/* finder */
	public static Finder<Long, BBUserCluster> find = new Finder<Long, BBUserCluster>(Long.class, BBUserCluster.class);
	
	/* コンストラクタ */
	public BBUserCluster() {
		
	}
	public BBUserCluster(Long id) {
		this.id = id;
	}
	public BBUserCluster(long clusterDepth, long clusterId) {
		this.clusterDepth = clusterDepth;
		this.clusterId = clusterId;
	}
	public BBUserCluster(UserCluster userCluster) {
		if (userCluster != null) {
			this.clusterDepth = userCluster.depth;
			this.clusterId = userCluster.id;
		}
	}
	
	/* インスタンスメソッド */
	public BBUserCluster store() {
		return bbUserClusterModelService.save(this);
	}
	
	public BBUserCluster unique() {
		BBUserCluster o = null;
		if ((o = bbUserClusterModelService.findById(id)) != null) {
			return o;
		}
		if ((o = bbUserClusterModelService.findByClusterDepthId(clusterDepth, clusterId)) != null) {
			return o;
		}
		return null;
	}
	
	public BBUserCluster uniqueOrStore() {
		BBUserCluster o = null;
		if ((o = unique()) != null) {
			return o;
		} else {
			return store();
		}
	}
	
	public void remove() {
		bbUserClusterModelService.delete(this);
	}
	
	public Set<BBUserCluster> findSetByDepth(long depth) {
		return bbUserClusterModelService.findSetByClusterDepth(depth);
	}
	
	public UserCluster convertToUserCluster() throws SQLException {
		return bbUserClusterService.convertBBUserClusterToUserCluster(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (clusterDepth ^ (clusterDepth >>> 32));
		result = prime * result + (int) (clusterId ^ (clusterId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BBUserCluster other = (BBUserCluster) obj;
		if (clusterDepth != other.clusterDepth)
			return false;
		if (clusterId != other.clusterId)
			return false;
		return true;
	}
	
	/* getter, setter */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getClusterDepth() {
		return clusterDepth;
	}

	public void setClusterDepth(long clusterDepth) {
		this.clusterDepth = clusterDepth;
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public String getJsonFeature() {
		return jsonFeature;
	}

	public void setJsonFeature(String jsonFeature) {
		this.jsonFeature = jsonFeature;
	}
	
	public Map<Long, Double> getFeature() {
		prepareFeature();
		return feature;
	}

	public void setFeature(Map<Long, Double> feature) {
		this.feature = feature;
		jsonFeature = null;
		prepareJsonFeature();
	}
	
	private void prepareFeature() {
		if (feature == null) {
			feature = GsonUtil.use().fromJson(jsonFeature, TYPE_OF_FEATURE);
		}
	}
	
	private void prepareJsonFeature() {
		if (jsonFeature == null) {
			jsonFeature = GsonUtil.use().toJson(feature, TYPE_OF_FEATURE);
		}
	}
	
	public BBUserCluster getParent() {
		return parent;
	}
	
	public void setParent(BBUserCluster parent) {
		this.parent = parent;
	}

	public Set<BBUserCluster> getChildren() {
		return children;
	}
	
	public void setChildren(Set<BBUserCluster> children) {
		this.children = children;
	}
	
	public double getDistanceFromParent() {
		return distanceFromParent;
	}
	
	public void setDistanceFromParent(double distanceFromParent) {
		this.distanceFromParent = distanceFromParent;
	}

	
	private static final Type TYPE_OF_FEATURE = new TypeToken<Map<Long,Double>>(){}.getType();
}
