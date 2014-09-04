package models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import models.service.model.ConfigurationModelService;
import play.db.ebean.Model;

@Entity
@Table(name="configuration", uniqueConstraints={
	@UniqueConstraint(columnNames={"conf_key"})
})
public class Configuration extends Model {

	@Id
	private Long id;
	
	@Column(name="conf_key", length=191)
	private String key;
	
	@Lob
	@Column(name="conf_value")
	private String value;
	
	public static Finder<Long,Configuration> find = new Finder<Long,Configuration>(Long.class,Configuration.class);
	
	@Transient
	protected ConfigurationModelService modelService = new ConfigurationModelService();
	
	/**
	 * 設定値を返す
	 * @param key キー
	 * @param defaultValue デフォルト値
	 * @return 格納されている値, 存在しない場合デフォルト値
	 */
	public static String get(String key, String defaultValue) {
		Configuration o = new Configuration(key).unique();
		if (o != null) {
			return o.getValue();
		}
		return defaultValue;
	}
	public static Boolean getBoolean(String key, Boolean defaultValue) {
		String s = get(key, String.valueOf(defaultValue));
		return Boolean.valueOf(s);
	}
	public static Long getLong(String key, Long defaultValue) {
		String s = get(key, String.valueOf(defaultValue));
		return Long.valueOf(s);
	}
	
	/**
	 * 設定値を設定する
	 * @param key キー
	 * @param value 設定値
	 */
	public static void put(String key, String value) {
		Configuration o = new Configuration(key).unique();
		if (o == null) {
			o = new Configuration(key);
		}
		o.setValue(value);
		o.save();
	}
	public static void putBoolean(String key, Boolean value) {
		put(key, String.valueOf(value));
	}
	public static void putLong(String key, Long value) {
		put(key, String.valueOf(value));
	}
	
	public Configuration() {
	}
	public Configuration(Long id) {
		this.id = id;
	}
	public Configuration(String key) {
		this.key = key;
	}
	
	public Configuration unique() {
		Configuration o = null;
		if ((o = modelService.findByKey(key)) != null) {
			return o;
		}
		if ((o = modelService.findById(id)) != null) {
			return o;
		}
		return null;
	}
	@Override
	public void save() {
		modelService.save(this);
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
