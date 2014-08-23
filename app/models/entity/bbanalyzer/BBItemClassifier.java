package models.entity.bbanalyzer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbanalyzer.BBItemClassifierService;
import models.service.model.bbanalyzer.BBItemClassifierModelService;
import play.db.ebean.Model;
import utils.bbanalyzer.GsonUtil;

import com.google.common.reflect.TypeToken;

@Entity
@Table(name="bb_item_classifier")
public class BBItemClassifier extends Model {
	@Id
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bb_user_cluster_id")
	BBUserCluster bbUserCluster;
	
	@Column(name="class_number")
	int classNumber;
	
	@Column(name="prob_prior")
	double probPrior;
	
	@Lob
	@Column(name="prob_cond")
	String jsonProbCond;
	
	@Transient
	Map<BBWord, Double> probCond;

	@Column(name="training_count")
	int trainingCount;
	
	@Column(name="training_data_count")
	int trainintDataCount;
	
	@Transient
	BBItemClassifierService bbItemClassifierService = new BBItemClassifierService();
	@Transient
	BBItemClassifierModelService bbItemClassifierModelService = new BBItemClassifierModelService();
	
	/* finder */
	public static Finder<Long, BBItemClassifier> find = new Finder<Long, BBItemClassifier>(Long.class, BBItemClassifier.class);

	/* コンストラクタ */
	public BBItemClassifier() {
		
	}
	public BBItemClassifier(BBUserCluster bbUserCluster, int classNumber) {
		this.bbUserCluster = bbUserCluster;
		this.classNumber = classNumber;
	}
	
	/* インスタンスメソッド */
	public BBItemClassifier store() {
		return bbItemClassifierModelService.save(this);
	}
	public BBItemClassifier unique() {
		BBItemClassifier o = null;
		if ((o = bbItemClassifierModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbItemClassifierModelService.findByBBUserClusterClassNumber(bbUserCluster, classNumber)) != null) {
			return o;
		}
		return null;
	}
	public BBItemClassifier uniqueOrStore() {
		BBItemClassifier o = null;
		if ((o = unique()) != null) {
			return o;
		} else {
			return store();
		}
	}
	public void remove() {
		bbItemClassifierModelService.delete(this);
	}
	
	public Set<BBItemClassifier> findSetByBBUserCluster(BBUserCluster bbUserCluster) {
		return bbItemClassifierModelService.findSetByBBUserCluster(bbUserCluster);
	}
	
	/* hashCode, equals */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((bbUserCluster == null) ? 0 : bbUserCluster.hashCode());
		result = prime * result + classNumber;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BBItemClassifier other = (BBItemClassifier) obj;
		if (bbUserCluster == null) {
			if (other.bbUserCluster != null)
				return false;
		} else if (!bbUserCluster.equals(other.bbUserCluster))
			return false;
		if (classNumber != other.classNumber)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
	public BBUserCluster getBbUserCluster() {
		return bbUserCluster;
	}
	public void setBbUserCluster(BBUserCluster bbUserCluster) {
		this.bbUserCluster = bbUserCluster;
	}
	public int getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(int classNumber) {
		this.classNumber = classNumber;
	}
	public double getProbPrior() {
		return probPrior;
	}
	public void setProbPrior(double probPrior) {
		this.probPrior = probPrior;
	}
	public String getJsonProbCond() {
		prepareJsonProbCond();
		return jsonProbCond;
	}
	public void setJsonProbCond(String jsonProbCond) {
		this.jsonProbCond = jsonProbCond;
		probCond = null;
		prepareProbCond();
	}
	public Map<BBWord, Double> getProbCond() {
		prepareProbCond();
		return probCond;
	}
	public void setProbCond(Map<BBWord, Double> probCond) {
		this.probCond = probCond;
		jsonProbCond = null;
		prepareJsonProbCond();
	}
	private void prepareJsonProbCond() {
		if (jsonProbCond == null) {
			jsonProbCond = GsonUtil.use().toJson(probCond, TYPE_PROB_COND);
		}
	}
	private void prepareProbCond() {
		if (probCond == null) {
			probCond = GsonUtil.use().fromJson(jsonProbCond, TYPE_PROB_COND);
		}
	}
	public int getTrainingCount() {
		return trainingCount;
	}
	public void setTrainingCount(int trainingCount) {
		this.trainingCount = trainingCount;
	}
	public int getTrainintDataCount() {
		return trainintDataCount;
	}
	public void setTrainintDataCount(int trainintDataCount) {
		this.trainintDataCount = trainintDataCount;
	}
	
	
	private static final Type TYPE_PROB_COND = new TypeToken<Map<BBWord,Double>>(){}.getType();
}
