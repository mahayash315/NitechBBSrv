package models.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.service.bbanalyzer.BBItemWordCountService;
import models.service.model.BBItemWordCountModelService;
import play.db.ebean.Model;

@Entity
@Table(name = "bb_item_word_count")
public class BBItemWordCount extends Model {

	@Id
	Long id;
	
	@OneToOne(cascade = {})
	@JoinColumn(name = "bb_item_id")
	BBItem item;
	
	@ManyToOne
	@JoinColumn(name = "bb_word_id")
	BBWord word;
	
	@Column(name = "count")
	int count;
	
	@Transient
	BBItemWordCountService bbItemWordCountService = new BBItemWordCountService();
	@Transient
	BBItemWordCountModelService bbItemWordCountModelService = new BBItemWordCountModelService();
	
	/* finder */
	public static Finder<Long, BBItemWordCount> find = new Finder<Long, BBItemWordCount>(Long.class, BBItemWordCount.class);
	
	/* コンストラクタ */
	public BBItemWordCount() {
		
	}
	public BBItemWordCount(Long id) {
		this.id = id;
	}
	public BBItemWordCount(BBItem item, BBWord word) {
		this.item = item;
		this.word = word;
	}
	
	/* インスタンスメソッド */
	public BBItemWordCount store() {
		return bbItemWordCountModelService.save(this);
	}
	public BBItemWordCount unique() {
		BBItemWordCount o = null;;
		if ((o = bbItemWordCountModelService.findById(id)) != null) {
			return o;
		} else if ((o = bbItemWordCountModelService.findByItemWord(item, word)) != null) {
			return o;
		}
		return null;
	}
	public void remove() {
		bbItemWordCountModelService.delete(this);
	}
	
	public Set<BBItemWordCount> findSetByItem(BBItem item) {
		return bbItemWordCountModelService.findSetByItem(item);
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
