package com.manonero.ecommerce.models;


public class PaginationResponse {
	private Object obj;
	private int limit;
	private int currentPage;
	private int totalItem;

	public PaginationResponse(Object obj, int limit, int currentPage, int totalItem) {
		this.obj = obj;
		this.limit = limit;
		this.currentPage = currentPage;
		this.totalItem = totalItem;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
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
