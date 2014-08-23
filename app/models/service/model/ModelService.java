package models.service.model;

import play.db.ebean.Model;

public interface ModelService<I, T extends Model> {
	
	/**
	 * id に対応するオブジェクトを返す (SELECT)
	 * @param id
	 * @return
	 */
	public T findById(I id);
	
	/**
	 * entry を INSERT する
	 * @param entry
	 * @return
	 */
	public T save(T entry);
	
	/**
	 * entry を DELETE する
	 * @param entry
	 */
	public void delete(T entry);
}
