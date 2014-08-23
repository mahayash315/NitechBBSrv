package models.view.mockbb.admin;

import java.util.List;

import models.entity.mockbb.MockBBItem;

public class ManageDto {
	public Integer currentPage;
	public String currentSortBy;
	public String currentOrder;
	public String currentFilter;
	public List<MockBBItem> items;
	public boolean hasPrevPage;
	public boolean hasNextPage;
}
