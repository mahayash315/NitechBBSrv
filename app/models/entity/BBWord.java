package models.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.BBWord.BBWordModelService;
import models.service.BBWord.BBWordService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_word")
public class BBWord extends Model {
	@Id
	Long id;
	
	@Column(name = "surface", length = 191)
	String surface;
	
	@Transient
	List<String> features;
	
	@Column(name="json_features")
	@Lob
	String jsonFeatures;
	
	@Column(name = "is_known")
	boolean isKnown;
	
	@Transient
	BBWordService bbWordService = new BBWordService();
	@Transient
	BBWordModelService bbWordModelService = new BBWordModelService();
	
	/* finder */
	
	public static Finder<Long, BBWord> find = new Finder<Long, BBWord>(Long.class, BBWord.class);
	
	/* コンストラクタ */
	
	public BBWord(String surface) {
		this.surface = surface;
	}
	
	public BBWord(String surface, String[] features, boolean isKnown) {
		this.surface = surface;
		this.features = new ArrayList<String>();
		for(String feature : features) {
			this.features.add(feature);
		}
		this.isKnown = isKnown;
	}
	
	/* インスタンスメソッド */
	
	public BBWord store() {
		BBWord o = unique();
		if (o == null) {
			return bbWordModelService.save(this);
		}
		return bbWordModelService.update(this, id);
	}
	
	public BBWord unique() {
		BBWord o = null;
		if ((o = bbWordModelService.findById(id)) != null) {
			return o;
		} else if((o = bbWordModelService.findBySurface(surface)) != null) {
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

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public String getJsonFeatures() {
		return jsonFeatures;
	}

	public void setJsonFeatures(String jsonFeatures) {
		this.jsonFeatures = jsonFeatures;
	}

	public boolean isKnown() {
		return isKnown;
	}

	public void setKnown(boolean isKnown) {
		this.isKnown = isKnown;
	}
}
