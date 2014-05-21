package models.service.MockBBItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.entity.MockBBItem;
import models.entity.MockBBItem.MockBBItemPK;
import models.service.Model.ModelService;
import models.setting.MockBBSetting;
import utils.PageUtil;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlRow;

public class MockBBItemModelService implements ModelService<MockBBItemPK, MockBBItem> {

	public static final String DEFAULT_ORDER_BY_CLAUSE = "date_show desc";
	public static final String ADDITIONAL_ORDER_BY_CLAUSE = "id_date desc, id_index desc";
	
	public static MockBBItemModelService use() {
		return new MockBBItemModelService();
	}

	@Override
	public MockBBItem findById(MockBBItemPK id) {
		if (id != null) {
			return MockBBItem.find.byId(id);
		}
		return null;
	}

	@Override
	public MockBBItem save(MockBBItem entry) {
		if (entry != null) {
			// 主キー生成
			if (entry.getId() == null) {
				entry.setId(generatePK());
			}
			
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public MockBBItem update(MockBBItem entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public MockBBItem update(MockBBItem entry, MockBBItemPK id) {
		if (entry != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(MockBBItem entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	
	public List<MockBBItem> findList(String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		if (orderByClause == null || orderByClause.isEmpty()) {
			orderByClause = DEFAULT_ORDER_BY_CLAUSE;
		}
		
		ExpressionList<MockBBItem> el = generateExpressionList(orderByClause, filter, hideRead, hideReference, onlyFragged);
		
		return el.orderBy(generateOrderByClause(orderByClause)).findList();
	}
	
	
	public Page<MockBBItem> findPage(Integer pageSource, String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		if (orderByClause == null || orderByClause.isEmpty()) {
			orderByClause = DEFAULT_ORDER_BY_CLAUSE;
		}
		
		Integer page = PageUtil.rightPage(pageSource);
		ExpressionList<MockBBItem> el = generateExpressionList(orderByClause, filter, hideRead, hideReference, onlyFragged);
		
		return el.orderBy(generateOrderByClause(orderByClause)).findPagingList(MockBBSetting.PAGE_SIZE).getPage(page);
	}
	
	
	private ExpressionList<MockBBItem> generateExpressionList(String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		
		ExpressionList<MockBBItem> el = MockBBItem.find.where();
		if (filter != null && !filter.trim().isEmpty()) {
			el.add( Expr.or(Expr.ilike("author", "%"+filter+"%"), Expr.ilike("title", "%"+filter+"%")) );
		}
		if (hideRead) {
			el.add( Expr.eq("is_read", false) );
		}
		if (hideReference) {
			el.add( Expr.eq("is_reference", false) );
		}
		if (onlyFragged) {
			el.add( Expr.eq("is_flagged", true) );
		}
		return el;
	}
	
	private String generateOrderByClause(String orderByClause) {
		if (orderByClause != null && !orderByClause.trim().isEmpty()) {
			orderByClause += ", ";
		}
		return (orderByClause + ADDITIONAL_ORDER_BY_CLAUSE);
	}
	
	private MockBBItemPK generatePK() {
		String idDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Integer idIndex = null;
		SqlRow row = Ebean.createSqlQuery("SELECT (max(id_index)+1) AS next_id FROM mock_bb_item WHERE id_date=?").setParameter(1, idDate).findUnique();
		if (row != null) {
			idIndex = row.getInteger("next_id");
			if (idIndex == null) {
				idIndex = Integer.valueOf(1);
			}
			return new MockBBItemPK(idDate, idIndex);
		}
		return null;
	}

}
