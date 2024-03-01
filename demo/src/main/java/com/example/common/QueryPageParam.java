package com.example.common;

import lombok.Data;

import java.util.HashMap;

/**
 * @author hzh
 * @date 2024/1/28 13:35
 */
@Data
public class QueryPageParam {//定义分页数
    private int pageSize = 10;//分页数
    private int PageNum = 1;//页面
    private HashMap param = new HashMap();
}
