package com.itheima.test;

import com.itheima.activiti.pojo.Holiday;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试表
 */
public class ActivitiTest {

    /**
     * 生成表结构
     */
    @Test
    public void testGenTable() {
        // 1: 通过资源文件创建 ProcessEngineConfiguration
        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2: 通过ProcessEngineConfiguration 构建 ProcessEngine
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void testGenTableDefault() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(defaultProcessEngine);
    }

    ProcessEngine defaultProcessEngine;

    @Before
    public void getProcessEngine() {
        defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProceccDefinition() {
        // 定义流程KEY
        String processDefinitionKey = "holiday";
        // 获取RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 创建流程定义查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 查询返回列表
        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey(processDefinitionKey).orderByProcessDefinitionVersion().desc().list();
        // 打印输出
        list.forEach(processDefinition -> {
            System.out.println("流程部署ID:" + processDefinition.getDeploymentId());
            System.out.println("流程定义ID:" + processDefinition.getId());
            System.out.println("流程定义名称:" + processDefinition.getName());
            System.out.println("流程定义key:" + processDefinition.getKey());
            System.out.println("流程定义版本:" + processDefinition.getVersion());
        });
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteDeployment() {
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        // 部署流程ID
        String deploymentId = "1";
        // 删除流程定义,如果该流程定义已有流程实例启动则删除报错
//        repositoryService.deleteDeployment(deploymentId);
        // 设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        repositoryService.deleteDeployment(deploymentId, true);

    }

    /**
     * 获取资源信息
     *
     * @throws IOException
     */
    @Test
    public void getProcessResources() throws IOException {

        // 1:流程定义ID
        String processDefinitionId = "holiday:1:7504";

        // 2:获取RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        // 3:创建查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        // 4:查询流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionId(processDefinitionId).singleResult();

        // 5:获取资源名称
        String resourceName = processDefinition.getResourceName();

        System.out.println("bpmn名称:" + resourceName);

        // 6:获取图片名称
        String diagramResourceName = processDefinition.getDiagramResourceName();

        System.out.println("png名称:" + diagramResourceName);

        File bpmnFile = new File("E:\\dance\\activiti7\\src\\main\\resources\\cp\\holiday.bpmn");
        File pngFile = new File("E:\\dance\\activiti7\\src\\main\\resources\\cp\\holiday.png");

        // 7:根据部署ID和资源名称获取资源流
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        // 8:创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream(bpmnFile);

        // 写文件
        writeByte(resourceAsStream, fileOutputStream);

        // 获取图片
        resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);

        fileOutputStream = new FileOutputStream(pngFile);

        // 写文件
        writeByte(resourceAsStream, fileOutputStream);

        System.out.println("获取资源成功!");

    }

    /**
     * 获取历史信息
     */
    @Test
    public void testHistoric01() {

        // 获取历史信息Service
        HistoryService historyService = defaultProcessEngine.getHistoryService();

        // 启动的流程实例的ID
        String processInstanceId = "10001";

        // 根据流程实例ID查询 历史记录
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();

        // 输出信息
        list.forEach(historicActivityInstance -> {
            System.out.println("---------------------------------------------------");
            System.out.println("当前激活ID:" + historicActivityInstance.getActivityId());
            System.out.println("当前激活名称:" + historicActivityInstance.getActivityName());
            System.out.println("流程定义ID:" + historicActivityInstance.getProcessDefinitionId());
            System.out.println("流程实例ID:" + historicActivityInstance.getProcessInstanceId());
            System.out.println("---------------------------------------------------");
        });

    }

    /**
     * 绑定业务主键
     */
    @Test
    public void bindingBusinessKey() {
        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        // 启动流程实例并绑定业务主键
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday", "1001");

        // 输出业务主键信息
        System.out.println("业务主键:" + holiday.getBusinessKey());
    }

    /**
     * 流程定义的全部挂起和激活
     */
    @Test
    public void suspendOrActivateProcessDefinition() {

        // 获取RepositoryService
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        // 查询唯一的流程定义
        ProcessDefinition holiday = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holiday").singleResult();

        // 查看是否激活
        boolean suspended = holiday.isSuspended();

        String processDefinitionId = holiday.getId();

        if (suspended) {
            // 如果没激活 就激活
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义:" + processDefinitionId + "激活");
        } else {
            // 如果激活 就挂起
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义:" + processDefinitionId + "挂起");
        }


    }

    /**
     * 单个流程实例挂起操作
     */
    @Test
    public void suspendOrActiveProcessInstance(){

        // 启动的流程实例ID
        String processInstanceId = "17501";

        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        // 查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        boolean suspended = processInstance.isSuspended();

        if(suspended){
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:"+processInstanceId+"激活");
        }else{
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:"+processInstanceId+"挂起");
        }

    }

    /**
     * 使用UEL-Value动态分配任务人员
     */
    @Test
    public void uelValue(){

        // 获取RuntimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        // 设置UEL-Value表达式中的值
        Map<String,Object> variables = new LinkedHashMap<>();
        variables.put("assignee0","zs");
        variables.put("assignee1","ls");
        variables.put("assignee2","ww");

        // 启动流程实例并传入设置好的参数
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday", variables);

        System.out.println("流程实例名称:" + holiday.getName());
    }

    /**
     * 启动流程实例并设置流程变量 Global
     */
    @Test
    public void startProcessInstanceAndSetVar(){
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Holiday holiday = new Holiday();
        holiday.setId(1);
        holiday.setHolidayName("发起流程申请");
        holiday.setNum(5f);

        Map<String,Object> map = new HashMap<>();
        map.put("holiday",holiday);
        ProcessInstance holiday3 = runtimeService.startProcessInstanceByKey("holiday3", map);

        System.out.println(holiday3.getProcessInstanceId());
    }

    /**
     * 查询并提交任务
     */
    @Test
    public void completTask(){
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday3")
                .taskAssignee("zhaoliu").singleResult();
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println("任务执行完毕");
        }else{
            System.out.println("该用户没有任务");
        }
    }

    private void writeByte(InputStream resourceAsStream, OutputStream fileOutputStream) throws IOException {
        int i = -1;
        byte[] content = new byte[1024];
        while ((i = resourceAsStream.read(content)) != -1) {
            fileOutputStream.write(content, 0, i);
        }
        // 释放资源
        fileOutputStream.flush();
        fileOutputStream.close();
        resourceAsStream.close();
    }


}
