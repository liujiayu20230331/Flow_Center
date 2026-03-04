package com.flowcenter.domain.dto.response;

public class PublishProcessResponse {
    private Long processId;
    private Integer version;
    private String deploymentId;
    private String processDefinitionId;
    private String status;
    public Long getProcessId() { return processId; }
    public void setProcessId(Long processId) { this.processId = processId; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDeploymentId() { return deploymentId; }
    public void setDeploymentId(String deploymentId) { this.deploymentId = deploymentId; }
    public String getProcessDefinitionId() { return processDefinitionId; }
    public void setProcessDefinitionId(String processDefinitionId) { this.processDefinitionId = processDefinitionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
