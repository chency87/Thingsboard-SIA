package org.thingsboard.server.bean;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 对Page<E>结果进行包装
 * <p/>
 * 新增分页的多项属性，主要参考:http://bbs.csdn.net/topics/360010907
 *
 * @author liuzh/abel533/isea533
 * @version 3.3.0
 * @since 3.2.2
 * 项目地址 : http://git.oschina.net/free/Mybatis_PageHelper
 */
public class PageInfo<T> extends PageSerializable<T> {
    //当前页
    private int currentPage;
    //总页数
    private int pageCount;
    //总条数
    private int totalRecord;
    //页面大小
    private int pageSize;
    //是否有下一页
    private boolean hasNextPage;
    //是否有上一页
    private boolean hasPrePage;
    //当前页码数组
    private int[] pageArray;
    //当前页数据
    private List<T> list;
    //下一页
    private int nextPage;
    //上一页
    private int prePage;
    //是否是第一页
    private boolean isFirstPage;
    //是否是最后一页
    private boolean isLastPage;
    //开始行数
    private int startRow;
    //结束行数
    private int endRow;
    //需要显示多少页
    private int navigatePages;


    /**
     * 包装Page对象
     *
     * @param list
     */
    //计算页码逻辑
    public void setPageInfo(List<T> list,int currentPage,int pageSize){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecord = list.size();
        this.pageCount = (int)Math.ceil((totalRecord*1.0) / pageSize);

        //判断是否有无前页或后页
        if (currentPage == 1){
            this.hasPrePage = false;
            this.prePage = -1;
            this.isFirstPage = true;
        }else {
            this.prePage = currentPage - 1;
        }
        if (currentPage == pageCount){
            this.hasNextPage = false;
            this.nextPage = -1;
            this.isLastPage = true;
        }else {
            this.nextPage = currentPage + 1;
        }

        this.pageArray = this.culArray(currentPage,pageSize,totalRecord,pageCount);
        this.startRow = (currentPage-1)*pageSize + 1;
        this.endRow = startRow+pageSize-1;
        if (endRow>totalRecord){
            endRow = totalRecord;
        }

        this.list = new ArrayList<>();
        for (int i=0;i<(endRow-startRow+1);i++){
            this.list.add(list.get(startRow-1+i));
        }
    }


    //计算显示的页数
    private int[] culArray(int currentPage,int pageSize,int totalRecord,int pageCount){
        int[] array;
        if (pageCount<=navigatePages){
            array = new int[pageCount];
            for (int i=0;i<pageCount;i++){
                array[i] = i + 1;
            }
        }else {
            array = new int[navigatePages];
            if (currentPage <= (navigatePages/2)){
                for (int i=0;i<navigatePages;i++){
                    array[i] = i + 1;
                }
            }
            else {
                int x = pageCount-(navigatePages/2);
                if (currentPage>x){
                    int start = pageCount - navigatePages + 1;
                    for (int i=0;i<navigatePages;i++){
                        array[i] = start + i;
                    }
                }else {
                    int start = currentPage - (navigatePages/2);
                    for (int i=0;i<navigatePages;i++){
                        array[i] = start + i;
                    }
                }
            }
        }
        return array;
    }
}

