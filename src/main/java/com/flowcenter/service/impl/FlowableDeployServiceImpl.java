package com.flowcenter.service.impl;

import com.flowcenter.exception.BizException;
import com.flowcenter.exception.ErrorCode;
import com.flowcenter.service.FlowableDeployService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class FlowableDeployServiceImpl implements FlowableDeployService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableDeployServiceImpl.class);
    private final RepositoryService repositoryService;

    public FlowableDeployServiceImpl(ObjectProvider<RepositoryService> provider) {
        this.repositoryService = provider.getIfAvailable();
    }

    @Override
    public DeployResult deployProcess(String processKey, String processName, String bpmnXml) {
        if (repositoryService == null) {
            throw new BizException(ErrorCode.PUBLISH_FAILED, "flowable engine is not enabled in current runtime");
        }
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(processName)
                    .key(processKey)
                    .addBytes(processKey + ".bpmn20.xml", bpmnXml.getBytes(StandardCharsets.UTF_8))
                    .deploy();

            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .latestVersion()
                    .singleResult();

            DeployResult r = new DeployResult();
            r.setDeploymentId(deployment.getId());
            r.setProcessDefinitionId(pd.getId());
            r.setVersion(pd.getVersion());
            return r;
        } catch (Exception e) {
            LOGGER.error("publish failed, processKey={}", processKey, e);
            throw new BizException(ErrorCode.PUBLISH_FAILED, "flowable publish failed");
        }
    }
}
