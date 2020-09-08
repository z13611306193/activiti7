package com.itheima.test.testGateWay;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试包含网关
 */
public class Test {

    ProcessEngine defaultProcessEngine;

    @Before
    public void init() {
        defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
    }

    @org.junit.Test
    public void deployment(){

        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("pic/gateway/holidayGateway3.bpmn")
                .name("体检流程")
                .deploy();

        System.out.println(deploy);

    }

    @org.junit.Test
    public void startProcessInstance(){

        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Integer userType = 1;

        Map<String,Object> map = new HashMap<>();

        map.put("userType",userType);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1", map);

        System.out.println(processInstance);

    }

    /**
     * 提交任务
     */
    @org.junit.Test
    public void completTask(){
        String assignee = "sunqi";
        Task task = querySingResultByAssignee(assignee);
        System.out.println(task);
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
