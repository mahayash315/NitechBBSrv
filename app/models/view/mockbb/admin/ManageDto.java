package models.view.mockbb.admin;

import java.util.List;

import models.entity.MockBBItem;

public class ManageDto {
	public Integer currentPage;
	public String currentSortBy;
	public String currentOrder;
	public String currentFilter;
	public List<MockBBItem> items;
	public boolean hasPrevPage;
	public boolean hasNextPage;
}
