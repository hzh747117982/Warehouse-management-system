package com.example.common;


import lombok.Data;

/**
 * @author hzh
 * @date 2024/1/28 14:17
 */
@Data
public class Result {//定义返回前端数据的格式
    private int code;//编码 200/400
    private String msg;//返回成功和失败
    private Long total;//总条数
    private Object data;//数据
    public static Result fail(){
        return result(400,"失败",0L,null);
    }

    public static Result suc(){
        return result(200,"成功",0L,null);
    }

    public static Result suc(Object data){
        return result(200,"成功",0L,data);
    }

    public static Result suc(Long total,Object data) {
        return result(200, "成功", total, data);
    }
    private static Result result(int code, String msg, Long total, Object data) {
        Result res = new Result();
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);

        res.setData(data);
        return res;
    }
}
