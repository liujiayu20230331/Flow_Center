package com.flowcenter.domain.dto.response;

import java.util.List;

public class ProcessDetailResponse {
    private Long id;
    private String processKey;
    private String processName;
    private String bpmnXml;
    private String status;
    private Integer currentVersion;
    private String createdAt;
    private String updatedAt;
    private List<ProcessVersionItemResponse> versions;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProcessKey() { return processKey; }
    public void setProcessKey(String processKey) { this.processKey = processKey; }
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }
    public String getBpmnXml() { return bpmnXml; }
    public void setBpmnXml(String bpmnXml) { this.bpmnXml = bpmnXml; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(Integer currentVersion) { this.currentVersion = currentVersion; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public List<ProcessVersionItemResponse> getVersions() { return versions; }
    public void setVersions(List<ProcessVersionItemResponse> versions) { this.versions = versions; }
}
