package models.view.mockbb;

import java.util.List;

import models.entity.mockbb.MockBBItem;

public class GetListDto {
	public String currentOrder;
	public String currentOrderKind;
	public boolean noReadFlag;
	public boolean onFlagFlag;
	public boolean referenceFlag;
	public List<MockBBItem> items;
}
