package models.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.entity.bb.Post;
import models.entity.bb.Word;
import models.service.model.NitechUserModelService;
import play.db.ebean.Model;

@Entity
@Table(name="nitech_user")
public class NitechUser extends Model {

	/* プロパティ */
	@Id
	private Long id;
	
	@Column(name = "hashed_id", length = 191)
	private String hashedId;
	
	/* サービス */
	@Transient
	private NitechUserModelService modelService = new NitechUserModelService();
	
	/* finder */
	public static Finder<Long,NitechUser> find = new Finder<Long,NitechUser>(Long.class, NitechUser.class);
	
	public NitechUser() {
	}
	public NitechUser(String hashedId) {
		this.hashedId = hashedId;
	}
	
	/* メソッド */
	public NitechUser unique() {
		NitechUser o = null;
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		if ((o = modelService.findByHashedId(hashedId)) != null) {
			return o;
		}
		return null;
	}
	public NitechUser store() {
		return modelService.save(this);
	}
	public List<Post> findPossessingPosts() {
		return modelService.findPossessingPosts(this);
	}
	public Map<Word,Double> getFeatureVector(List<Long> postIds) {
		return modelService.getFeatureVector(this, postIds);
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
	public String getHashedId() {
		return hashedId;
	}
	public void setHashedId(String hashedId) {
		this.hashedId = hashedId;
	}
}
