package models.service.MockBBItem;

import java.util.List;

import models.entity.MockBBItem;
import models.entity.MockBBItem.MockBBItemPK;
import models.service.Model.ModelService;
import models.setting.MockBBSetting;
import utils.PageUtil;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

public class MockBBItemModelService implements ModelService<MockBBItemPK, MockBBItem> {
	
	public static final String DEFAULT_ORDER_BY_CLAUSE = "";
	
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
	
	
	public List<MockBBItem> findList(String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		
		ExpressionList<MockBBItem> el = generateExpressionList(orderByClause, filter, hideRead, hideReference, onlyFragged);
		
		return el.orderBy(DEFAULT_ORDER_BY_CLAUSE).findList();
	}
	
	
	public Page<MockBBItem> findPage(Integer pageSource, String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		
		Integer page = PageUtil.rightPage(pageSource);
		ExpressionList<MockBBItem> el = generateExpressionList(orderByClause, filter, hideRead, hideReference, onlyFragged);
		
		return el.findPagingList(MockBBSetting.PAGE_SIZE).getPage(page);
	}
	
	
	private ExpressionList<MockBBItem> generateExpressionList(String orderByClause, String filter, boolean hideRead, boolean hideReference, boolean onlyFragged) {
		if (orderByClause == null || orderByClause.isEmpty()) {
			orderByClause = DEFAULT_ORDER_BY_CLAUSE;
		}
		
		ExpressionList<MockBBItem> el = MockBBItem.find.where();
		if (filter != null) {
			el.add( Expr.or(Expr.ilike("author", "%"+filter+"%"), Expr.ilike("title", "%"+filter+"%")) );
		}
		if (hideRead) {
			el.add( Expr.eq("is_read", false) );
		}
		if (hideReference) {
			el.add( Expr.eq("is_reference", false) );
		}
		if (onlyFragged) {
			el.add( Expr.eq("is_fragged", true) );
		}
		return el;
	}

}
