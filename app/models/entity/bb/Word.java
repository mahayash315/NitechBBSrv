package models.entity.bb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.model.bb.WordModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_word")
public class Word extends Model {

	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="base_form", length=197)
	private String baseForm;
	
	
	@Transient
	private WordModelService modelService = new WordModelService();
	
	
	public static Finder<Long,Word> find = new Finder<Long,Word>(Long.class,Word.class);
	
	public Word() {
		
	}
	public Word(Long id) {
		this.id = id;
	}

	public Word unique() {
		Word o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		if ((o = modelService.findByBaseForm(baseForm)) != null) {
			return o;
		}
		return null;
	}
	public Word store() {
		return modelService.save(this);
	}
	public List<Word> findList() {
		return modelService.findList();
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
	public String getBaseForm() {
		return baseForm;
	}
	public void setBaseForm(String baseForm) {
		this.baseForm = baseForm;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((baseForm == null) ? 0 : baseForm.hashCode());
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
		Word other = (Word) obj;
		if (baseForm == null) {
			if (other.baseForm != null)
				return false;
		} else if (!baseForm.equals(other.baseForm))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
