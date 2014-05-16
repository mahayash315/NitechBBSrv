package models.service.Model;

import play.db.ebean.Model;

public interface ModelService<I extends Object, T extends Model> {
	
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
	 * entry を UPDATE する
	 * @param entry
	 * @return
	 */
	public T update(T entry);
	
	/**
	 * id に対応するエントリを entry で UPDATE する
	 * @param entry
	 * @param id
	 * @return
	 */
	public T update(T entry, I id);
	
	/**
	 * entry を DELETE する
	 * @param entry
	 */
	public void delete(T entry);
}
