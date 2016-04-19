package com.aipa.svc.common.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class PagedListRespData<T> {

	private Integer total;
	private Integer page_count;
	private Integer pageSize;
	private Integer pageNum;
	private List<T> page_list;

	/**
	 * @param page_list page页list
	 * @param pageNum
	 * @param pageSize
	 * @param total
	 */
	public PagedListRespData(List<T> page_list, int pageNum, int pageSize,
							 int total) {
		this.total = total;
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.page_list = page_list;

		page_count = (total + pageSize -1 )/ pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getPage_count() {
		return page_count;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public List<T> getPage_list() {
		return page_list;
	}
}
