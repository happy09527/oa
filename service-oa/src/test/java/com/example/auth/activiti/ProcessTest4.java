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
import java.util.List;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/7/9
 * @description:
 */
@SpringBootTest
public class ProcessTest4 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Test
    public void test1() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qinjia04.bpmn20.xml")
                .name("请假04").deploy();
        System.out.println(deploy.getId());
    }

    @Test
    public void startProcess() {
        Map<String, Object> map = new HashMap<>();
        map.put("day", 5);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qinjia04", map);
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }

    @Test
    public void queryTask1() {
        Task task = taskService.createTaskQuery().taskAssignee("zjl").singleResult();
        System.out.println("----------------------------");
        System.out.println("流程实例id：" + task.getProcessInstanceId());
        System.out.println("任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
    }

    @Test
    public void queryTask2() {
        Task task = taskService.createTaskQuery().taskAssignee("renshi").singleResult();
        if (task != null) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("zjl")  //要查询的负责人
                .singleResult();//返回一条
        taskService.complete(task.getId());
    }
}
