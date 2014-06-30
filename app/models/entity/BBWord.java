package models.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbword.BBWordModelService;
import models.service.bbword.BBWordService;
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
	
//	@Column(name="json_features")
//	@Lob
//	String jsonFeatures;
	
	@Column(name = "is_known")
	boolean isKnown;
	
	@Transient
	BBWordService bbWordService = new BBWordService();
	@Transient
	BBWordModelService bbWordModelService = new BBWordModelService();
	
	/* finder */
	
	public static Finder<Long, BBWord> find = new Finder<Long, BBWord>(Long.class, BBWord.class);
	
	/* コンストラクタ */
	
	public BBWord() {
		super();
	}
	public BBWord(long id) {
		this();
		this.id = Long.valueOf(id);
	}
	public BBWord(String surface) {
		this();
		this.surface = surface;
	}
	public BBWord(String surface, boolean isKnown) {
		this();
		this.surface = surface;
		this.isKnown = isKnown;
	}
	
//	public BBWord(String surface, String[] features, boolean isKnown) {
//		super();
//		this.surface = surface;
//		this.features = new ArrayList<String>();
//		for(String feature : features) {
//			this.features.add(feature);
//		}
//		this.isKnown = isKnown;
//	}
	
	/* インスタンスメソッド */
	
	public BBWord store() {
//		BBWord o = unique();
//		if (o == null) {
//			return bbWordModelService.save(this);
//		}
//		return bbWordModelService.update(this, o.getId());
		return bbWordModelService.save(this);
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
	
	public BBWord uniqueOrStore() {
		BBWord o = unique();
		if (o == null) {
			o = bbWordModelService.save(this);
		}
		return o;
	}
	
	/* toString */
	@Override
	public String toString() {
		return "("+getId()+")"+getSurface();
	}
	
	/* hashCode, equals */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isKnown ? 1231 : 1237);
		result = prime * result + ((surface == null) ? 0 : surface.hashCode());
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
		BBWord other = (BBWord) obj;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isKnown != other.isKnown)
			return false;
		if (surface == null) {
			if (other.surface != null)
				return false;
		} else if (!surface.equals(other.surface))
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

//	public String getJsonFeatures() {
//		return jsonFeatures;
//	}
//
//	public void setJsonFeatures(String jsonFeatures) {
//		this.jsonFeatures = jsonFeatures;
//	}

	public boolean isKnown() {
		return isKnown;
	}

	public void setKnown(boolean isKnown) {
		this.isKnown = isKnown;
	}
}
