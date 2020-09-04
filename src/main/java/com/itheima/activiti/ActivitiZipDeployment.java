package com.itheima.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 使用ZIP压缩包部署
 */
public class ActivitiZipDeployment {

    public static void main(String[] args) throws IOException {

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        InputStream resourceAsStream = ActivitiZipDeployment.class.getClassLoader().getResourceAsStream("pic.zip");

        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);

        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).name("请假申请流程").deploy();

        zipInputStream.close();

        resourceAsStream.close();

        System.out.println("流程部署ID:" + deploy.getId());
        System.out.println("流程部署名称:" + deploy.getName());

    }

}
