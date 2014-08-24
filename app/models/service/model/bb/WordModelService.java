package models.service.model.bb;

import java.util.List;

import models.entity.bb.Word;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

public class WordModelService implements ModelService<Long, Word> {
	
	private static RawSql sGenId = RawSqlBuilder
			.parse("select min(id)+1 from bb_word t1 where (select id from bb_word t2 where t2.id=t1.id+1) is null")
			.columnMapping("min(id)+1", "id").create();

	@Override
	public Word findById(Long id) {
		if (id != null) {
			return Word.find.byId(id);
		}
		return null;
	}

	@Override
	public Word save(Word entry) {
		if (entry != null) {
			if (entry.getId() == null) {
				// 新規ID生成
				Word g = Word.find.setRawSql(sGenId).findUnique();
				if (g != null) {
					entry.setId(g.getId());
				}
			}
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Word entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}


	public Word findByBaseForm(String baseForm) {
		if (baseForm != null) {
			return Word.find.where().eq("baseForm", baseForm).findUnique();
		}
		return null;
	}
	
	public List<Word> findList() {
		ExpressionList<Word> expr = Word.find.where();
		return expr.findList();
	}
	
}
