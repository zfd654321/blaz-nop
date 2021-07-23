package com.bl.nop.common.util;

import java.util.List;

public class Page<T> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize;// 页面大小
    private int currentPage;// 当前页的位置
    private int prePage;// 上一页
    private int nextPage;// 下一页
    private int totalPage;// 总页数
    private int totalCount;// 总条数
    private int start;
    /**
     * 当前页的数据
     */
    private List<T> list;

    /**
     * 获得分页内容
     *
     * @return
     */
    public List<?> getList() {
        return list;
    }

    public Page(int pageSize, int currentPage, int prePage,
            int nextPage, int totalPage, int totalCount, List<T> list) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.prePage = prePage;
        this.nextPage = nextPage;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.list = list;
    }

    /**
     * 设置分页内容
     *
     * @param list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    public Page() {
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.start = 0;
    }

    /**
     *
     * @param currentPage
     * @param pageSize
     */
    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        if(currentPage >= 1) {
        	this.prePage = currentPage - 1;
        	this.nextPage = currentPage + 1;
        } else {
        	this.prePage = 1;
        	this.nextPage = 1;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if(currentPage > 0) {
        	this.prePage = currentPage - 1;
        	this.nextPage = currentPage + 1;
        } else {
        	this.prePage = 1;
        	this.nextPage = 1;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if(totalCount > 0) {
        	int i = totalCount % pageSize;
        	int page = (totalCount / pageSize); 
        	if(i > 0) {
        		page += 1;
        	}
        	this.setTotalPage(page);
        }
    }

	public int getStart() {
		if ((currentPage - 1) > 0) {// 说明不是首页
			start = (currentPage - 1) * pageSize;
		} else {// 说是是首页
			start = 0;
			currentPage = 1;
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
