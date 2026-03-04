package com.flowcenter.service;

public interface FlowableDeployService {
    DeployResult deployProcess(String processKey, String processName, String bpmnXml);

    class DeployResult {
        private String deploymentId;
        private String processDefinitionId;
        private Integer version;
        public String getDeploymentId() { return deploymentId; }
        public void setDeploymentId(String deploymentId) { this.deploymentId = deploymentId; }
        public String getProcessDefinitionId() { return processDefinitionId; }
        public void setProcessDefinitionId(String processDefinitionId) { this.processDefinitionId = processDefinitionId; }
        public Integer getVersion() { return version; }
        public void setVersion(Integer version) { this.version = version; }
    }
}
