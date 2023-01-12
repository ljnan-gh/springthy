package com.kdsj.common;

import java.util.List;

public class Grid {

    // 当前页显示的数据
    private List<?> rows;
    // 表中总个数
    private long total;

    public Grid(List<?> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
