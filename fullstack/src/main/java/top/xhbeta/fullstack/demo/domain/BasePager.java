package top.xhbeta.fullstack.demo.domain;

public class BasePager {
  public static final int DEF_COUNT = 20;
  protected long totalCount = 0;
  protected int pageSize = 20;
  protected int pageNo = 1;

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public BasePager(int pageNo, int pageSize, long totalCount) {
    if (totalCount <= 0) {
      this.totalCount = 0;
    } else {
      this.totalCount = totalCount;
    }
    if (pageSize <= 0) {
      this.pageSize = DEF_COUNT;
    } else {
      this.pageSize = pageSize;
    }
    if (pageNo <= 0) {
      this.pageNo = 1;
    } else {
      this.pageNo = pageNo;
    }
  }

  public long getTotalCount() {
    return totalCount;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }
}
