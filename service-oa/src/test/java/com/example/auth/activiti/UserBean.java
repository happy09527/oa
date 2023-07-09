package com.example.auth.activiti;

import org.springframework.stereotype.Component;

/**
 * @author: ZhangX
 * @createDate: 2023/7/9
 * @description:
 */
@Component
public class UserBean {
    public String getUsername(int id){
        if(id == 1){
            return "jack";
        }else if(id == 2){
            return "dick";
        }else{
            return "admin";
        }
    }
}
