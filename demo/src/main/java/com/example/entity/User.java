package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author hzh
 * @date 2024/1/23 13:14
 */
@Data
public class User {
    private  int id;
    private  String no;
    private  String name;
    private  String password;
    private  int sex;
    private  int age;
    private  int roleId;
    private  String Phone;
    private  String isvalid;

}
