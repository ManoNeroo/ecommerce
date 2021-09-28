package com.manonero.ecommerce.models;

public class PaginationResponse extends Response {
	private int limit;
	private int currentPage;
	private int totalItem;

	public PaginationResponse(Object data, Boolean isSuccess, int limit, int currentPage, int totalItem) {
		super(data, isSuccess);
		this.limit = limit;
		this.currentPage = currentPage;
		this.totalItem = totalItem;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

}
