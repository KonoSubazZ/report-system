package com.novo.report.common;

public class Query {
    private Integer pageNo;
    private Integer pageSize;

    public Query() {
        this.pageNo = 1; // 默认第一页
        this.pageSize = 10; // 默认每页10条记录
    }

    public Query(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
