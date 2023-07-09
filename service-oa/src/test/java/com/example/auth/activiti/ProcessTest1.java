package com.example.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/7/9
 * @description:
 */
@SpringBootTest
public class ProcessTest1 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Test
    public void test1(){
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/jiaban.bpmn20.xml")
                .name("加班").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void startProcess(){
        Map<String,Object> map = new HashMap<>();
        map.put("assignee1","lucy");
        map.put("assignee2","jam");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban", map);
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }

    @Test
    public void queryTask(){
        Task task = taskService.createTaskQuery().taskAssignee("lucy").singleResult();
        System.out.println("流程实例id：" + task.getProcessInstanceId());
        System.out.println("任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
    }
}
