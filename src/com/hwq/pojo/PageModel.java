package com.hwq.pojo;

import java.util.List;

public class PageModel {
    //��ҳ����ģ��
    private int currentPage;//��ǰҳ
    private int pageSize;//ÿҳ��ʾ����
    private int allCount;//�ܼ�¼��
    private List result;// ��ŷ�ҳ�����Ҫ��ʾ�ļ�¼����

    public PageModel() {
    }

    public PageModel(int currentPage, int pageSize, int allCount, List result) {

        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.allCount = allCount;
        this.result = result;
    }

    public int getCurrentPage() {

        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    private int getAllPage(int allCount, int pageSize){
        if(allCount%pageSize==0)
            return allCount/pageSize;
        else
            return allCount/pageSize+1;
    }

}
