package models.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbwordcount.BBWordCountModelService;
import models.service.bbwordcount.BBWordCountService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_word_count")
public class BBWordCount extends Model {

	@Id
	Long id;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "bb_item_id")
	BBItem item;
	
	@ManyToOne
	@JoinColumn(name = "bb_word_id")
	BBWord word;
	
	@Column(name = "count")
	int count;
	
	@Transient
	BBWordCountService bbWordCountService = new BBWordCountService();
	@Transient
	BBWordCountModelService bbWordCountModelService = new BBWordCountModelService();
	
	/* finder */
	public static Finder<Long, BBWordCount> find = new Finder<Long, BBWordCount>(Long.class, BBWordCount.class);
	
	/* コンストラクタ */
	public BBWordCount() {
		
	}
	public BBWordCount(Long id) {
		this.id = id;
	}
	public BBWordCount(BBItem item, BBWord word) {
		this.item = item;
		this.word = word;
	}
	
	/* インスタンスメソッド */
	public BBWordCount store() {
		return bbWordCountModelService.save(this);
	}
	public BBWordCount unique() {
		BBWordCount o = null;;
		if ((o = bbWordCountModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbWordCountModelService.findByItemWord(item, word)) != null) {
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
	public BBItem getItem() {
		return item;
	}
	public void setItem(BBItem item) {
		this.item = item;
	}
	public BBWord getWord() {
		return word;
	}
	public void setWord(BBWord word) {
		this.word = word;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
