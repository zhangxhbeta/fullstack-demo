package top.xhbeta.fullstack.demo.domain;

import org.springframework.data.domain.Page;

public class BaseResult {
  private Object data;
  private BasePager pager;

  public BaseResult(Page page) {
    this.data = page.getContent();
    this.pager = new BasePager(page.getNumber() + 1, page.getSize(), page.getTotalElements());
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public BasePager getPager() {
    return pager;
  }

  public void setPager(BasePager pager) {
    this.pager = pager;
  }
}
