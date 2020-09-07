package com.itheima.test;

import com.itheima.activiti.pojo.Holiday;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关测试
 */
public class ActivitiGatewayTest {

    ProcessEngine defaultProcessEngine;

    @Before
    public void init() {
        defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deployment(){
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("pic/gateway/holidayGateway1.bpmn")
                .addClasspathResource("pic/gateway/holidayGateway1.png")
                .name("请假流程")
                .deploy();

        System.out.println(deploy);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Map<String,Object> map = new HashMap<>();

        Holiday holiday = new Holiday();
        holiday.setNum(5F);

        map.put("holiday",holiday);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", map);

        System.out.println(processInstance);

    }

    /**
     * 提交任务
     */
    @Test
    public void completTask(){
        String assignee = "zhaoliu";
        Task task = querySingResultByAssignee(assignee);
        TaskService taskService = defaultProcessEngine.getTaskService();
        taskService.complete(task.getId());
    }

    /**
     * 查询用户唯一任务
     * @param assignee
     * @return
     */
    public Task querySingResultByAssignee(String assignee){
        TaskService taskService = defaultProcessEngine.getTaskService();
        return taskService.createTaskQuery().processDefinitionKey("myProcess_1").taskAssignee(assignee).singleResult();
    }
}
