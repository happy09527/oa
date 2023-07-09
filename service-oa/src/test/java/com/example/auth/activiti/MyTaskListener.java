package com.example.auth.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: ZhangX
 * @createDate: 2023/7/9
 * @description:
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        if(task.getName().equals("经理审批")){
            task.setAssignee("jack");
        }else if(task.getName().equals("人事审批")){
            task.setAssignee("tom");
        }else{
            task.setAssignee("admin");
        }
    }
}
