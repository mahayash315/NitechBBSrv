package models.entity.bb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.model.bb.WordModelService;
import play.db.ebean.Model;

@Entity
@Table(name="bb_word2")
public class Word extends Model {

	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="base_form", length=197)
	private String baseForm;
	
	
	@Transient
	private WordModelService modelService = new WordModelService();
	
	
	public static Finder<Long,Word> find = new Finder<Long,Word>(Long.class,Word.class);
	

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
	
}
