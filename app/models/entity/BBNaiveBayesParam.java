package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.BBNaiveBayesParam.BBNaiveBayesParamModelService;
import models.service.BBNaiveBayesParam.BBNaiveBayesParamService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_naive_bayes_param")
public class BBNaiveBayesParam extends Model {

	@Id
	Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	User user;
	
	@OneToOne
	@JoinColumn(name = "bb_word_id")
	BBWord bbWord;
	
	@Column(name = "p1")
	double p1;
	
	
	@Transient
	BBNaiveBayesParamService bbNaiveBayesParamService = new BBNaiveBayesParamService();
	@Transient
	BBNaiveBayesParamModelService bbNaiveBayesParamModelService = new BBNaiveBayesParamModelService();
	
	/* finder */
	
	public static Finder<Long, BBNaiveBayesParam> find = new Finder<Long, BBNaiveBayesParam>(Long.class, BBNaiveBayesParam.class);
	
	/* コンストラクタ */
	
	public BBNaiveBayesParam(User user, BBWord bbWord, double p1) {
		this.user = user;
		this.bbWord = bbWord;
		this.p1 = p1;
	}
	
	/* インスタンスメソッド */
	
	public BBNaiveBayesParam store() {
		BBNaiveBayesParam o = unique();
		if (o == null) {
			return bbNaiveBayesParamModelService.save(this);
		}
		return bbNaiveBayesParamModelService.update(this, id);
	}
	
	public BBNaiveBayesParam unique() {
		BBNaiveBayesParam o = null;
		if ((o = bbNaiveBayesParamModelService.findById(id)) != null) {
			return o;
		}
		return null;
	}
	
	/* getter, setter */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BBWord getBBWord() {
		return bbWord;
	}

	public void setBBWord(BBWord bbWord) {
		this.bbWord = bbWord;
	}

	public double getP1() {
		return p1;
	}

	public void setP1(double p1) {
		this.p1 = p1;
	}
}
